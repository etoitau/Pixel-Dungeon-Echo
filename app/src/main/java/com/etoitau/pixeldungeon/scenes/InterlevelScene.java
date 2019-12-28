/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
 *
 * Based on:
 *
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

import java.io.FileNotFoundException;

import com.etoitau.pixeldungeon.BuildConfig;
import com.etoitau.pixeldungeon.TimeMachine;
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
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.ui.GameLog;
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
    private static final String ERR_GENERIC = "Sorry, something went wrong.";

    public static Exception lastException = null;

    public enum Mode {
        DESCEND,
        ASCEND,
        CONTINUE,
        RESURRECT,
        RESURRECT_ANKH,
        RESURRECT_CRACKED,
        RETURN,
        FALL,
        NONE,
        TELEPORT,
        TELEPORT_BACK,
        BACK_IN_TIME
    }

    public static Mode mode;

    public static int returnDepth;
    public static int returnPos;

    public static boolean noStory = false;

    public static boolean fallIntoPit;

    private enum Phase {
        FADE_IN, STATIC, FADE_OUT
    }

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
            case RESURRECT_ANKH:
            case RESURRECT_CRACKED:
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
            default:
        }

        message = PixelScene.createText(text, 9);
        message.measure();
        message.x = (Camera.main.width - message.width()) / 2;
        message.y = (Camera.main.height - message.height()) / 2;
        add(message);

        phase = Phase.FADE_IN;
        timeLeft = TIME_TO_FADE;

        thread = new Thread() {
            @Override
            public void run() {

                try {

                    Generator.reset();

                    switch (mode) {
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
                        case RESURRECT_ANKH:
                        case RESURRECT_CRACKED:
                            resurrect();
                            break;
                        case RETURN:
                            returnTo();
                            break;
                        case FALL:
                            fall();
                            break;
                        case TELEPORT:
                            // if here to test error
                            if (BuildConfig.DEBUG) {
                                Dungeon.hero = null;
                            }
                            teleport();
                            break;
                        case TELEPORT_BACK:
                            teleport_back();
                            break;
                        case BACK_IN_TIME:
                            backInTime();
                            break;
                        default:
                    }

                    if ((Dungeon.depth % 5) == 0) {
                        Sample.INSTANCE.load(Assets.SND_BOSS);
                    }

                } catch (FileNotFoundException e) {
                    lastException = e;
                    error = ERR_FILE_NOT_FOUND;

                } catch (Exception e) {
                    lastException = e;
                    error = ERR_GENERIC;

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
                    Game.switchScene(GameScene.class);
                }
                break;

            case STATIC:
                // calls error window
                if (error != null) {
                    add(new WndError(error, lastException) {
                        public void onBackPressed() {
                            super.onBackPressed();
                            Game.switchScene(StartScene.class);
                        }
                    });
                    error = null;
                }
                break;
        }
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

    private void resurrect() {

        Actor.fixTime();

        if (Dungeon.bossLevel()) {
            // reset hero
            Dungeon.hero.resurrect(mode);
            // then kick up to prev level
            Dungeon.depth--;
            // recreate fresh boss level
            Level level = Dungeon.newLevel();
            // go to entrance of fresh boss level
            Dungeon.switchLevel(level, level.entrance);
        } else {
            // refresh mobs, go to entrance
            Dungeon.resetLevel();
            // reset hero
            Dungeon.hero.resurrect(mode);
        }

    }

    // Using ankh to go back in time
    private void backInTime() {
        Actor.clear();
        GameLog.wipe();
        // use TimeMachine to set Dungeon and level state to old snapshot
        TimeMachine.goBack();
        // start up dungeon in this state
        Dungeon.switchLevel(Dungeon.level, Dungeon.hero.pos);
    }

    @Override
    protected void onBackPressed() {
        // Do nothing
    }
}
