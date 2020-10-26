/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019-2020 Kyle Chatman
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
package com.etoitau.pixeldungeon;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.etoitau.pixeldungeon.levels.features.Sign;
import com.watabau.noosa.Game;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.buffs.Amok;
import com.etoitau.pixeldungeon.actors.buffs.Light;
import com.etoitau.pixeldungeon.actors.buffs.Rage;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.hero.HeroClass;
import com.etoitau.pixeldungeon.actors.mobs.ColdGirl;
import com.etoitau.pixeldungeon.actors.mobs.npcs.Blacksmith;
import com.etoitau.pixeldungeon.actors.mobs.npcs.Imp;
import com.etoitau.pixeldungeon.actors.mobs.npcs.Ghost;
import com.etoitau.pixeldungeon.actors.mobs.npcs.Wandmaker;
import com.etoitau.pixeldungeon.items.Ankh;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.potions.Potion;
import com.etoitau.pixeldungeon.items.rings.Ring;
import com.etoitau.pixeldungeon.items.scrolls.Scroll;
import com.etoitau.pixeldungeon.items.wands.Wand;
import com.etoitau.pixeldungeon.levels.CavesBossLevel;
import com.etoitau.pixeldungeon.levels.CavesLevel;
import com.etoitau.pixeldungeon.levels.CityBossLevel;
import com.etoitau.pixeldungeon.levels.CityLevel;
import com.etoitau.pixeldungeon.levels.DeadEndLevel;
import com.etoitau.pixeldungeon.levels.FrostLevel;
import com.etoitau.pixeldungeon.levels.HallsBossLevel;
import com.etoitau.pixeldungeon.levels.HallsLevel;
import com.etoitau.pixeldungeon.levels.LastLevel;
import com.etoitau.pixeldungeon.levels.LastShopLevel;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.levels.PrisonBossLevel;
import com.etoitau.pixeldungeon.levels.PrisonLevel;
import com.etoitau.pixeldungeon.levels.Room;
import com.etoitau.pixeldungeon.levels.SewerBossLevel;
import com.etoitau.pixeldungeon.levels.SewerLevel;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.scenes.StartScene;
import com.etoitau.pixeldungeon.ui.QuickSlot;
import com.etoitau.pixeldungeon.utils.BArray;
import com.etoitau.pixeldungeon.utils.Utils;
import com.etoitau.pixeldungeon.windows.WndResurrect;
import com.watabau.utils.Bundlable;
import com.watabau.utils.Bundle;
import com.watabau.utils.PathFinder;
import com.watabau.utils.Random;
import com.watabau.utils.SparseArray;

public class Dungeon {

    public static int potionOfStrength;
    public static int scrollsOfUpgrade;
    public static int scrollsOfEnchantment;
    public static boolean dewVial;        // true if the dew vial can be spawned

    public static int challenges;

    public static Hero hero;
    public static Level level;

    public static int depth;
    public static int gold;

    public static int difficulty;
    public static Difficulties currentDifficulty;

    // Reason of death
    public static String resultDescription;

    public static HashSet<Integer> chapters;

    // Hero's field of view
    public static boolean[] visible = new boolean[Level.LENGTH];

    public static final float DAY_LENGTH = 1800;
    public static boolean nightMode;

    public static SparseArray<ArrayList<Item>> droppedItems;

    public static void init() {

        challenges = PixelDungeon.challenges();

        Actor.clear();

        PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);

        Scroll.initLabels();
        Potion.initColors();
        Wand.initWoods();
        Ring.initGems();
        Sign.initSigns();
        TimeMachine.reset();

        Statistics.reset();
        Journal.reset();

        depth = 0;
        gold = 0;

        droppedItems = new SparseArray<ArrayList<Item>>();

        potionOfStrength = 0;
        scrollsOfUpgrade = 0;
        scrollsOfEnchantment = 0;
        dewVial = true;

        chapters = new HashSet<Integer>();

        Ghost.Quest.reset();
        Wandmaker.Quest.reset();
        Blacksmith.Quest.reset();
        Imp.Quest.reset();

        Room.shuffleTypes();

        QuickSlot.primaryValue = null;
        QuickSlot.secondaryValue = null;

        hero = new Hero();
        hero.difficulty = difficulty;
        hero.live();

        // reset local badges, load global
        Badges.reset();

        StartScene.curClass.initHero(hero);

    }

    public static boolean isChallenged(int mask) {
        return (challenges & mask) != 0;
    }

    public static Level newLevel() {

        Dungeon.level = null;
        Actor.clear();

        depth++;
        if (depth % ColdGirl.FROST_DEPTH > Statistics.deepestFloor) {
            Statistics.deepestFloor = depth;

            if (Statistics.qualifiedForNoKilling) {
                Statistics.completedWithNoKilling = true;
            } else {
                Statistics.completedWithNoKilling = false;
            }
        }

        Arrays.fill(visible, false);

        Level level;
        switch (depth) {
            case 1:
            case 2:
            case 3:
            case 4:
                level = new SewerLevel();
                break;
            case 5:
                level = new SewerBossLevel();
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                level = new PrisonLevel();
                break;
            case 10:
                level = new PrisonBossLevel();
                break;
            case 11:
            case 12:
            case 13:
            case 14:
                level = new CavesLevel();
                break;
            case 15:
                level = new CavesBossLevel();
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                level = new CityLevel();
                break;
            case 20:
                level = new CityBossLevel();
                break;
            case 21:
                level = new LastShopLevel();
                break;
            case 22:
            case 23:
            case 24:
                level = new HallsLevel();
                break;
            case 25:
                level = new HallsBossLevel();
                break;
            case 26:
                level = new LastLevel();
                break;
            case ColdGirl.FROST_DEPTH:
                level = new FrostLevel();
                break;
            default:
                level = new DeadEndLevel();
                Statistics.deepestFloor--;
        }

        level.create();

        Statistics.qualifiedForNoKilling = !bossLevel();

        return level;
    }

    public static void resetLevel() {

        Actor.clear();

        // reset field of view
        Arrays.fill(visible, false);

        // new mobs
        level.reset();

        // goto entrance
        switchLevel(level, level.entrance);
    }

    public static boolean shopOnLevel() {
        return depth == 1 || depth == 6 || depth == 11 || depth == 16;
    }

    public static boolean bossLevel() {
        return bossLevel(depth);
    }

    public static boolean bossLevel(int depth) {
        return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25 || depth == ColdGirl.FROST_DEPTH || depth == 0;
    }

    public static void switchLevel(final Level level, int pos) {

        // determine if night time based on in-game time and current difficulty
        float nightLength = DAY_LENGTH * currentDifficulty.naturalNightFactor();
        float timeOfDay = Statistics.duration % DAY_LENGTH;
        nightMode = timeOfDay > DAY_LENGTH / 2 && timeOfDay < DAY_LENGTH / 2 + nightLength;

        Dungeon.level = level;
        Actor.init();

        Actor respawner = level.respawner();
        if (respawner != null) {
            Actor.add(level.respawner());
        }

        hero.pos = pos != -1 ? pos : level.exit;

        Light light = hero.buff(Light.class);
        hero.viewDistance = light == null ? level.viewDistance : Math.max(Light.DISTANCE, level.viewDistance);

        observe();
    }

    public static void dropToChasm(Item item) {
        int depth = Dungeon.depth + 1;
        ArrayList<Item> dropped = (ArrayList<Item>) Dungeon.droppedItems.get(depth);
        if (dropped == null) {
            Dungeon.droppedItems.put(depth, dropped = new ArrayList<Item>());
        }
        dropped.add(item);
    }

    public static boolean posNeeded() {
        int[] quota = {4, 2, 9, 4, 14, 6, 19, 8, 24, 9};
        return chance(quota, potionOfStrength);
    }

    public static boolean souNeeded() {
        int[] quota = {5, 3, 10, 6, 15, 9, 20, 12, 25, 13};
        return chance(quota, scrollsOfUpgrade);
    }

    public static boolean soeNeeded() {
        return Random.Int(12 * (1 + scrollsOfEnchantment)) < depth;
    }

    private static boolean chance(int[] quota, int number) {

        for (int i = 0; i < quota.length; i += 2) {
            int qDepth = quota[i];
            if (depth <= qDepth) {
                int qNumber = quota[i + 1];
                return Random.Float() < (float) (qNumber - number) / (qDepth - depth + 1);
            }
        }

        return false;
    }

    private static final String RG_GAME_FILE = "game.dat";
    private static final String RG_DEPTH_FILE = "depth%d.dat";

    private static final String WR_GAME_FILE = "warrior.dat";
    private static final String WR_DEPTH_FILE = "warrior%d.dat";

    private static final String MG_GAME_FILE = "mage.dat";
    private static final String MG_DEPTH_FILE = "mage%d.dat";

    private static final String RN_GAME_FILE = "ranger.dat";
    private static final String RN_DEPTH_FILE = "ranger%d.dat";

    private static final String BACKUP_GAME_FILE = "backup%s.dat";
    private static final String BACKUP_DEPTH_FILE = "backup%s%d.dat";

    private static final String DUMMY_GAME_FILE = "dummygame.dat";
    private static final String DUMMY_DEPTH_FILE = "dummydepth%d.dat";
    private static final String VERSION = "version";
    private static final String CHALLENGES = "challenges";
    private static final String HERO = "hero";
    private static final String GOLD = "gold";
    private static final String DEPTH = "depth";
    private static final String LEVEL = "level";
    private static final String DROPPED = "dropped%d";
    private static final String POS = "potionsOfStrength";
    private static final String SOU = "scrollsOfEnhancement";
    private static final String SOE = "scrollsOfEnchantment";
    private static final String DV = "dewVial";
    private static final String CHAPTERS = "chapters";
    private static final String QUESTS = "quests";
    private static final String BADGES = "badges";

    public static String gameFile(HeroClass cl) {
        switch (cl) {
            case WARRIOR:
                return WR_GAME_FILE;
            case MAGE:
                return MG_GAME_FILE;
            case HUNTRESS:
                return RN_GAME_FILE;
            case ROGUE:
                return RG_GAME_FILE;
            default:
                return DUMMY_GAME_FILE;
        }
    }

    private static String depthFile(HeroClass cl) {
        switch (cl) {
            case WARRIOR:
                return WR_DEPTH_FILE;
            case MAGE:
                return MG_DEPTH_FILE;
            case HUNTRESS:
                return RN_DEPTH_FILE;
            case ROGUE:
                return RG_DEPTH_FILE;
            default:
                return DUMMY_DEPTH_FILE;
        }
    }

    public static Bundle saveGameToBundle() {
        return saveGameToBundle(true);
    }

    public static Bundle saveGameToBundle(boolean includeTimeMachine) {
        Bundle bundle = new Bundle();

        bundle.put(VERSION, Game.version);
        bundle.put(CHALLENGES, challenges);
        bundle.put(HERO, hero);
        bundle.put(GOLD, gold);
        bundle.put(DEPTH, depth);

        for (int d : droppedItems.keyArray()) {
            bundle.put(String.format(DROPPED, d), droppedItems.get(d));
        }

        bundle.put(POS, potionOfStrength);
        bundle.put(SOU, scrollsOfUpgrade);
        bundle.put(SOE, scrollsOfEnchantment);
        bundle.put(DV, dewVial);

        int count = 0;
        int ids[] = new int[chapters.size()];
        for (Integer id : chapters) {
            ids[count++] = id;
        }
        bundle.put(CHAPTERS, ids);

        Bundle quests = new Bundle();
        Ghost.Quest.storeInBundle(quests);
        Wandmaker.Quest.storeInBundle(quests);
        Blacksmith.Quest.storeInBundle(quests);
        Imp.Quest.storeInBundle(quests);
        bundle.put(QUESTS, quests);

        Room.storeRoomsInBundle(bundle);

        Statistics.storeInBundle(bundle);
        Journal.storeInBundle(bundle);

        QuickSlot.save(bundle);

        Scroll.save(bundle);
        Potion.save(bundle);
        Wand.save(bundle);
        Ring.save(bundle);
        Sign.save(bundle);
        if (includeTimeMachine)
            TimeMachine.save(bundle);

        Bundle badges = new Bundle();
        Badges.saveLocal(badges);
        bundle.put(BADGES, badges);

        return bundle;
    }

    public static void saveGame(String fileName) throws IOException {
        try {
            Bundle bundle = saveGameToBundle();

            OutputStream output = Game.instance.openFileOutput(fileName, Game.MODE_PRIVATE);
            Bundle.write(bundle, output);
            output.close();

        } catch (Exception e) {

            GamesInProgress.setUnknown(hero.heroClass);
        }
    }

    public static void saveLevel() throws IOException {
        saveLevel(Utils.format(depthFile(hero.heroClass), depth));
    }

    public static void saveLevel(String file) throws IOException {
        Bundle bundle = new Bundle();
        bundle.put(LEVEL, level);

        OutputStream output = Game.instance.openFileOutput(file, Game.MODE_PRIVATE);
        Bundle.write(bundle, output);
        output.close();
    }

    public static void saveAll() throws IOException {
        if (hero.isAlive()) {
            Actor.fixTime();
            saveGame(gameFile(hero.heroClass));
            saveLevel();
            GamesInProgress.set(hero.heroClass, depth, hero.lvl, challenges != 0);
        } else if (WndResurrect.instance != null) {
            WndResurrect.instance.hide();
            Hero.reallyDie(WndResurrect.causeOfDeath);
        }
    }

    public static void saveBackup(HeroClass heroClass) throws IOException {
        saveGame(Utils.format(BACKUP_GAME_FILE, heroClass.title()));
        saveLevel(Utils.format(BACKUP_DEPTH_FILE, heroClass.title(), depth));
    }

    public static void loadBackupGame(HeroClass heroClass) throws IOException {
        loadGame(Utils.format(BACKUP_GAME_FILE, heroClass.title()), true);
    }

    // used in InterlevelSchene.restore()
    public static void loadGame(HeroClass cl) throws IOException {
        loadGame(gameFile(cl), true);
    }

    // used by WndRanking
    public static void loadGame(String fileName) throws IOException {
        loadGame(fileName, false);
    }

    public static void loadGame(String fileName, boolean fullLoad) throws IOException {

        Bundle bundle = gameBundle(fileName);

        loadGameFromBundle(bundle, fullLoad);
    }

    public static void loadGameFromBundle(Bundle bundle, boolean fullLoad) {
        loadGameFromBundle(bundle, fullLoad, true);
    }

    public static void loadGameFromBundle(Bundle bundle, boolean fullLoad, boolean includeTimeMachine) {
        Dungeon.challenges = bundle.getInt(CHALLENGES);

        Dungeon.level = null;
        Dungeon.depth = -1;

        if (fullLoad) {
            PathFinder.setMapSize(Level.WIDTH, Level.HEIGHT);
        }

        Scroll.restore(bundle);
        Potion.restore(bundle);
        Wand.restore(bundle);
        Ring.restore(bundle);
        Sign.restore(bundle);
        if (includeTimeMachine)
            TimeMachine.restore(bundle);

        potionOfStrength = bundle.getInt(POS);
        scrollsOfUpgrade = bundle.getInt(SOU);
        scrollsOfEnchantment = bundle.getInt(SOE);
        dewVial = bundle.getBoolean(DV);

        if (fullLoad) {
            chapters = new HashSet<>();
            int ids[] = bundle.getIntArray(CHAPTERS);
            if (ids != null) {
                for (int id : ids) {
                    chapters.add(id);
                }
            }

            Bundle quests = bundle.getBundle(QUESTS);
            if (!quests.isNull()) {
                Ghost.Quest.restoreFromBundle(quests);
                Wandmaker.Quest.restoreFromBundle(quests);
                Blacksmith.Quest.restoreFromBundle(quests);
                Imp.Quest.restoreFromBundle(quests);
            } else {
                Ghost.Quest.reset();
                Wandmaker.Quest.reset();
                Blacksmith.Quest.reset();
                Imp.Quest.reset();
            }

            Room.restoreRoomsFromBundle(bundle);
        }

        Bundle badges = bundle.getBundle(BADGES);
        if (!badges.isNull()) {
            Badges.loadLocal(badges);
        } else {
            Badges.reset();
        }

        QuickSlot.restore(bundle);

        @SuppressWarnings("unused")
        String version = bundle.getString(VERSION);

        hero = null;
        hero = (Hero) bundle.get(HERO);
        if (hero == null)
            Log.d("", "null hero");

        gold = bundle.getInt(GOLD);
        depth = bundle.getInt(DEPTH);

        Statistics.restoreFromBundle(bundle);
        Journal.restoreFromBundle(bundle);

        droppedItems = new SparseArray<ArrayList<Item>>();
        for (int i = 2; i <= Statistics.deepestFloor + 1; i++) {
            ArrayList<Item> dropped = new ArrayList<Item>();
            for (Bundlable b : bundle.getCollection(String.format(DROPPED, i))) {
                dropped.add((Item) b);
            }
            if (!dropped.isEmpty()) {
                droppedItems.put(i, dropped);
            }
        }
    }

    public static Level loadLevel(HeroClass cl) throws IOException {
        return loadLevel(Utils.format(depthFile(cl), depth));
    }

    public static Level loadLevel(String file) throws IOException {
        Dungeon.level = null;
        Actor.clear();

        InputStream input = Game.instance.openFileInput(file);
        Bundle bundle = Bundle.read(input);
        input.close();

        return loadLevelFromBundle(bundle);
    }

    public static Level loadBackupLevel(HeroClass heroClass) throws IOException {
        return loadLevel(Utils.format(BACKUP_DEPTH_FILE, heroClass.title(), depth));
    }

    public static Level loadLevelFromBundle(Bundle bundle) {
        return (Level) bundle.get("level");
    }

    public static void deleteGame(HeroClass cl, boolean deleteLevels) {

        Game.instance.deleteFile(gameFile(cl));

        if (deleteLevels) {
            int depth = 1;
            while (Game.instance.deleteFile(Utils.format(depthFile(cl), depth))) {
                depth++;
            }
        }

        GamesInProgress.delete(cl);
    }

    public static Bundle gameBundle(String fileName) throws IOException {

        InputStream input = Game.instance.openFileInput(fileName);
        Bundle bundle = Bundle.read(input);
        input.close();

        return bundle;
    }

    public static void preview(GamesInProgress.Info info, Bundle bundle) {
        info.depth = bundle.getInt(DEPTH);
        info.challenges = (bundle.getInt(CHALLENGES) != 0);
        if (info.depth == -1) {
            info.depth = bundle.getInt("maxDepth");    // FIXME
        }
        Hero.preview(info, bundle.getBundle(HERO));
    }

    public static void fail(String desc) {
        resultDescription = desc;
        if (hero.belongings.getItem(Ankh.class) == null || Dungeon.depth == ColdGirl.FROST_DEPTH) {
            if (Dungeon.depth == ColdGirl.FROST_DEPTH)
                resultDescription = ColdGirl.TXT_DEATH;

            Rankings.INSTANCE.submit(false);
        }
    }

    public static void win(String desc) {

        hero.belongings.identify();

        if (challenges != 0) {
            Badges.validateChampion();
        }

        resultDescription = desc;
        Rankings.INSTANCE.submit(true);
    }

    public static void observe() {

        if (level == null) {
            return;
        }

        level.updateFieldOfView(hero);
        System.arraycopy(Level.fieldOfView, 0, visible, 0, visible.length);

        BArray.or(level.visited, visible, level.visited);

        GameScene.afterObserve();
    }

    public static boolean[] passable = new boolean[Level.LENGTH];

    public static int findPath(Char ch, int from, int to, boolean pass[], boolean[] visible) {

        if (Level.adjacent(from, to)) {
            return Actor.findChar(to) == null && (pass[to] || Level.avoid[to]) ? to : -1;
        }

        if (ch.flying || ch.buff(Amok.class) != null || ch.buff(Rage.class) != null) {
            BArray.or(pass, Level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Level.LENGTH);
        }

        for (Actor actor : Actor.all()) {
            if (actor instanceof Char) {
                int pos = ((Char) actor).pos;
                if (visible[pos]) {
                    passable[pos] = false;
                }
            }
        }

        return PathFinder.getStep(from, to, passable);

    }

    public static int flee(Char ch, int cur, int from, boolean pass[], boolean[] visible) {

        if (ch.flying) {
            BArray.or(pass, Level.avoid, passable);
        } else {
            System.arraycopy(pass, 0, passable, 0, Level.LENGTH);
        }

        for (Actor actor : Actor.all()) {
            if (actor instanceof Char) {
                int pos = ((Char) actor).pos;
                if (visible[pos]) {
                    passable[pos] = false;
                }
            }
        }
        passable[cur] = true;

        return PathFinder.getStepBack(cur, from, passable);

    }

}
