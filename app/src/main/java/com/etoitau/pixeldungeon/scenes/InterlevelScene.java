/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.etoitau.pixeldungeon.scenes;

import android.util.Log;

import java.io.FileNotFoundException;

import com.watabau.noosa.BitmapText;
import com.watabau.noosa.Camera;
import com.watabau.noosa.Game;
import com.watabau.noosa.audio.Music;
import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.Statistics;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.mobs.ColdGirl;
import com.etoitau.pixeldungeon.items.Generator;
import com.etoitau.pixeldungeon.levels.Campaigns.FirstWave;
import com.etoitau.pixeldungeon.levels.FrostLevel;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.levels.MovieLevel;
import com.etoitau.pixeldungeon.ui.GameLog;
import com.etoitau.pixeldungeon.utils.GLog;
import com.etoitau.pixeldungeon.windows.WndError;
import com.etoitau.pixeldungeon.windows.WndStory;

public class InterlevelScene extends PixelScene {

    private static final float TIME_TO_FADE = 0.3f;

    private static final String TXT_DESCENDING = "Descending...";
    private static final String TXT_ASCENDING = "Ascending...";
    private static final String TXT_LOADING = "Loading...";
    private static final String TXT_RESURRECTING = "Resurrecting...";
    private static final String TXT_RETURNING = "Returning...";
    private static final String TXT_FALLING = "Falling...";
    private static final String TXT_TELEPORTING = "A weird portal sucks you in...";

    private static final String ERR_FILE_NOT_FOUND = "File not found. For some reason.";
    private static final String ERR_GENERIC = "Something went wrong...";

    public enum Mode {
        DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, NONE, TELEPORT, TELEPORT_BACK, MOVIE, MOVIE_OUT, MISSION
    }

    public static Mode mode;

    public static int returnDepth;
    public static int returnPos;

    public static boolean noStory = false;

    public static boolean fallIntoPit;

    private enum Phase {
        FADE_IN, STATIC, FADE_OUT
    }

    ;
    private Phase phase;
    private float timeLeft;

    private BitmapText message;

    private Thread thread;
    private String error = null;

    @Override
    public void create() {
        super.create();

        String text = "";
        switch (mode) {
            case DESCEND:
                text = TXT_DESCENDING;
                break;
            case ASCEND:
                text = TXT_ASCENDING;
                break;
            case CONTINUE:
                text = TXT_LOADING;
                break;
            case RESURRECT:
                text = TXT_RESURRECTING;
                break;
            case RETURN:
                text = TXT_RETURNING;
                break;
            case FALL:
                text = TXT_FALLING;
                break;
            case TELEPORT:
            case TELEPORT_BACK:
                text = TXT_TELEPORTING;
                break;
            case MOVIE:
                text = "10 years ago...";
                break;
            case MOVIE_OUT:
                text = "The true story has yet to begin";
                break;
            case MISSION:
                text = "10 years ago...";
                break;
            default:
        }

        message = PixelScene.createText(text, 9);
        message.measure();
        message.x = (Camera.main.width - message.width()) / 2;
        message.y = (Camera.main.height - message.height()) / 2;
        add(message);

        phase = Phase.FADE_IN;
        timeLeft = TIME_TO_FADE;

        if (mode == Mode.MOVIE || mode == Mode.MOVIE_OUT || mode == Mode.MISSION)
            timeLeft = 10 * TIME_TO_FADE;

        thread = new Thread() {
            @Override
            public void run() {

                try {

                    Generator.reset();

                    switch (mode) {
                        case MISSION:
                            runMission();
                            break;
                        case MOVIE:
                            runMovie();
                            break;
                        case MOVIE_OUT:
                            endMovie();
                            break;
                        case DESCEND:
                            descend();
                            break;
                        case ASCEND:
                            ascend();
                            break;
                        case CONTINUE:
                            restore();
                            break;
                        case RESURRECT:
                            resurrect();
                            break;
                        case RETURN:
                            returnTo();
                            break;
                        case FALL:
                            fall();
                            break;
                        case TELEPORT:
                            teleport();
                            break;
                        case TELEPORT_BACK:
                            teleport_back();
                            break;
                        default:
                    }

                    if ((Dungeon.depth % 5) == 0) {
                        Sample.INSTANCE.load(Assets.SND_BOSS);
                    }

                } catch (FileNotFoundException e) {

                    error = ERR_FILE_NOT_FOUND;

                } catch (Exception e) {

                    error = ERR_GENERIC + " in " + mode + "\n" + e;

                }

                if (phase == Phase.STATIC && error == null) {
                    phase = Phase.FADE_OUT;
                    timeLeft = TIME_TO_FADE;
                }
            }
        };
        thread.start();
    }

    @Override
    public void update() {
        super.update();

        float p = timeLeft / TIME_TO_FADE;
        if (mode == Mode.MOVIE || mode == Mode.MOVIE_OUT || mode == Mode.MISSION)
            p /= 10;


        switch (phase) {

            case FADE_IN:
                message.alpha(1 - p);
                if ((timeLeft -= Game.elapsed) <= 0) {
                    if (!thread.isAlive() && error == null) {
                        phase = Phase.FADE_OUT;
                        timeLeft = TIME_TO_FADE;
                    } else {
                        phase = Phase.STATIC;
                    }
                }
                break;

            case FADE_OUT:
                message.alpha(p);


                if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
                    Music.INSTANCE.volume(p);
                }
                if ((timeLeft -= Game.elapsed) <= 0) {
                    if (mode == Mode.MOVIE || mode == Mode.MISSION)
                        //Game.switchScene( TitleScene.class );
                        Game.switchScene(MissionScene.class);
                    else if (mode == Mode.MOVIE_OUT)
                        Game.switchScene(TitleScene.class);
                    else
                        Game.switchScene(GameScene.class);
                }
                break;

            case STATIC:
                if (error != null) {
                    if (mode != Mode.MOVIE && mode != Mode.MOVIE_OUT) {
                        add(new WndError(error) {
                            public void onBackPressed() {
                                super.onBackPressed();
                                Game.switchScene(StartScene.class);
                            }

                            ;
                        });
                        error = null;
                    } else {
                        add(new WndError("Something went wrong with your movie... but the game continues") {
                            public void onBackPressed() {
                                super.onBackPressed();
                                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                                Game.switchScene(InterlevelScene.class);
                            }

                            ;
                        });
                        error = null;
                    }
                }
                break;
        }
    }

    private void runMission() throws Exception {

        try {
            GameLog.wipe();
        } catch (Exception ex) {
            // Could have been causing issues
        }


        if (Dungeon.hero == null)
            Dungeon.initLegend();

        MissionScene.scenePause = true;
        Level level = new FirstWave();
        Dungeon.level = level;
        level.create();
        Dungeon.switchLevel(level, level.randomRespawnCell());
    }

    private void runMovie() throws Exception {

        try {
            GameLog.wipe();
        } catch (Exception ex) {
            // Could have been causing issues
        }


        if (Dungeon.hero == null)
            Dungeon.initLegend();


        Level level = new MovieLevel();
        Dungeon.level = level;
        level.create();
        Dungeon.switchLevel(level, level.randomRespawnCell());
    }

    private void endMovie() throws Exception {

        //Actor.fixTime();
        // Game.switchScene(TitleScene.class);
    }

    private void descend() throws Exception {

        Actor.fixTime();
        if (Dungeon.hero == null) {
            Dungeon.init();
            if (noStory) {
                Dungeon.chapters.add(WndStory.ID_SEWERS);
                noStory = false;
            }
            GameLog.wipe();
        } else {
            Dungeon.saveLevel();
        }

        Level level;
        if (Dungeon.depth >= Statistics.deepestFloor) {
            level = Dungeon.newLevel();
        } else {
            Dungeon.depth++;
            level = Dungeon.loadLevel(Dungeon.hero.heroClass);
        }
        Dungeon.switchLevel(level, level.entrance);
    }

    private void fall() throws Exception {

        Actor.fixTime();
        Dungeon.saveLevel();

        Level level;
        if (Dungeon.depth >= Statistics.deepestFloor) {
            level = Dungeon.newLevel();
        } else {
            Dungeon.depth++;
            level = Dungeon.loadLevel(Dungeon.hero.heroClass);
        }
        Dungeon.switchLevel(level, fallIntoPit ? level.pitCell() : level.randomRespawnCell());
    }

    private void teleport() throws Exception {

        Actor.fixTime();
        Dungeon.saveLevel();

        Dungeon.depth = ColdGirl.FROST_DEPTH - 1;
        Level level = Dungeon.newLevel();
        int pos = level.randomRespawnCell();
        Dungeon.switchLevel(level, level.randomRespawnCell());
    }

    private void teleport_back() throws Exception {
        Actor.fixTime();

        Dungeon.saveLevel();
        Dungeon.depth = ColdGirl.cameFrom;
        Level level = Dungeon.loadLevel(Dungeon.hero.heroClass);
        Dungeon.switchLevel(level, ColdGirl.cameFromPos);
    }

    private void ascend() throws Exception {
        Actor.fixTime();

        Dungeon.saveLevel();
        Dungeon.depth--;
        Level level = Dungeon.loadLevel(Dungeon.hero.heroClass);
        Dungeon.switchLevel(level, level.exit);
    }

    private void returnTo() throws Exception {

        Actor.fixTime();

        Dungeon.saveLevel();
        Dungeon.depth = returnDepth;
        Level level = Dungeon.loadLevel(Dungeon.hero.heroClass);
        Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(returnPos) : returnPos);
    }

    private void restore() throws Exception {

        Actor.fixTime();

        GameLog.wipe();
        Dungeon.loadGame(StartScene.curClass);
        if (Dungeon.depth == -1) {
            Dungeon.depth = Statistics.deepestFloor;
            Dungeon.switchLevel(Dungeon.loadLevel(StartScene.curClass), -1);
        } else {
            Level level = Dungeon.loadLevel(StartScene.curClass);
            Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(Dungeon.hero.pos) : Dungeon.hero.pos);
        }
    }

    private void resurrect() throws Exception {

        Actor.fixTime();

        if (Dungeon.bossLevel()) {
            Dungeon.hero.resurrect(Dungeon.depth);
            Dungeon.depth--;
            Level level = Dungeon.newLevel();
            Dungeon.switchLevel(level, level.entrance);
        } else {
            Dungeon.hero.resurrect(-1);
            Dungeon.resetLevel();
        }
    }

    @Override
    protected void onBackPressed() {
        // Do nothing
    }
}