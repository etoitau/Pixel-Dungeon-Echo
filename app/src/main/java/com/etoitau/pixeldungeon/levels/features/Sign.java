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
package com.etoitau.pixeldungeon.levels.features;

import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.particles.ElmoParticle;
import com.etoitau.pixeldungeon.levels.DeadEndLevel;
import com.etoitau.pixeldungeon.levels.Terrain;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.utils.GLog;
import com.etoitau.pixeldungeon.windows.WndMessage;
import com.watabau.utils.Bundle;
import com.watabau.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sign {

    private static final String TXT_DEAD_END =
            "What are you doing here?!";

    private static final String[] SHOP_SIGN = {
            "Pixel-Mart. The best prices in town.",
            "Pixel-Mart. All you need for successful adventure!",
            "Pixel-Mart. Spend money. Live longer.",
            "Pixel-Mart. A safer life in the dungeon.",
            "Pixel-Mart. Special prices for demon hunters!"
    };

    private static final String[] BOSS_SIGN = {
            "Beware of Goo!",
            "Don't let The Tengu out!",
            "DANGER! Heavy machinery can cause injury, loss of limbs or death!",
            "No weapons allowed in the presence of His Majesty!",
    };

    private static final String[] SEWER_SIGN = {
            "Don't overestimate your strength. Use weapons and armor you can handle.",
            "Not all doors in the dungeon are visible at first sight. If you are stuck, search for hidden doors.",
            "Upgrading a piece of equipment will make it more effective, and also lighter.",
            "After you've thrown enough seeds in, an alchemy pot will randomly pick one and " +
                    "you'll get the potion associated with that seed.",
            "Every dungeon is different. An indigo potion, or a holly wand, might not do what it " +
                    "did the last time you explored.",
            "Ration your food, there's not much to be found in the dungeon."
    };
    private static List<Integer> sewerIndexes;


    private static final String[] PRISON_SIGN = {
            "Identify your potions and scrolls as soon as possible. Don't put it off to the moment " +
                    "when you actually need them.",
            "Being hungry doesn't hurt, but starving does hurt.",
            "Surprise attack has a better chance to hit. For example, you can ambush your enemy behind " +
                    "a closed door when you know it is approaching.",
            "You can fill your dew vial by throwing it into a well of health.",
            "If you have a full dew vial when you take a mortal blow, it will automatically " +
                    "heal you to offset it.",
            "Mystery meat is risky unless you can cook or freeze it"
    };
    private static List<Integer> prisonIndexes;

    private static final String[] CAVE_SIGN = {
            "When you're attacked by several monsters at the same time, try to retreat behind a door.",
            "If you are burning, you can't put out the fire in the water while levitating.",
            "There is no sense in possessing more than one Ankh at the same time, because you will " +
                    "lose them upon resurrecting.",
            "A scroll of upgrade or enchantment will also fix partially degraded item.",
            "If you have two of the same item, the blacksmith can upgrade the higher-level one.",
            "Picking on someone much lower level than you doesn't give you any experience."
    };
    private static List<Integer> caveIndexes;

    private static final String[] CITY_SIGN = {
            "When you upgrade an enchanted weapon, there is a chance to destroy that enchantment.",
            "Weapons and armors deteriorate faster than wands and rings, but there are more ways to fix them.",
            "There is a powerful scroll that can only be obtained through sacrifice to the dungeon " +
                    "spirits. Look for a mysterious alter - the mark it gives spreads through violence.",
            "Wearing armour lighter than your strength makes it easier to dodge hits.",
            "Enchanted armour always has a cost, and can even kill you",
            "Make sure you find the ambitious imp. It's a valuable friend to have...",
            "Magical attacks are much harder to dodge than those from normal weapons."
    };
    private static List<Integer> cityIndexes;

    private static final String TXT_BURN =
            "As you try to read the sign it bursts into greenish flames.";

    private static final String SIGN_SEED_KEY = "sign_seed";

    private static int randSeed = 1; 

    public static void initSigns() {
        randSeed = Random.Int(217);
        getShuffle();
    }

    private static void getShuffle() {
        sewerIndexes = randIndexes(SEWER_SIGN, randSeed);
        prisonIndexes = randIndexes(PRISON_SIGN, randSeed);
        caveIndexes = randIndexes(CAVE_SIGN, randSeed);
        cityIndexes = randIndexes(CITY_SIGN, randSeed);
    }

    public static void save(Bundle bundle) {
        bundle.put(SIGN_SEED_KEY, randSeed);
    }

    public static void restore(Bundle bundle) {
        if (bundle.contains(SIGN_SEED_KEY)) {
            randSeed = bundle.getInt(SIGN_SEED_KEY);
        } else {
            randSeed = 1;
        }
        getShuffle();
    }


    public static void read(int pos) {
        if (Dungeon.level instanceof DeadEndLevel) {
            GameScene.show(new WndMessage(TXT_DEAD_END));
            return;

        }
        String signText = null;
        switch (Dungeon.depth) {
            case 1:
                signText = SHOP_SIGN[0];
                break;
            case 2:
            case 3:
            case 4:
                signText = SEWER_SIGN[sewerIndexes.get(Dungeon.depth - 2)];
                break;
            case 5:
                signText = BOSS_SIGN[0];
                break;
            case 6:
                signText = SHOP_SIGN[1];
                break;
            case 7:
            case 8:
            case 9:
                signText = PRISON_SIGN[prisonIndexes.get(Dungeon.depth - 7)];
                break;
            case 10:
                signText = BOSS_SIGN[1];
                break;
            case 11:
                signText = SHOP_SIGN[2];
                break;
            case 12:
            case 13:
            case 14:
                signText = CAVE_SIGN[caveIndexes.get(Dungeon.depth - 12)];
                break;
            case 15:
                signText = BOSS_SIGN[2];
                break;
            case 16:
                signText = SHOP_SIGN[3];
                break;
            case 17:
            case 18:
            case 19:
                signText = CITY_SIGN[cityIndexes.get(Dungeon.depth - 17)];
                break;
            case 20:
                signText = BOSS_SIGN[3];
                break;
            case 21:
                signText = SHOP_SIGN[4];
                break;
            default:
                burnSign(pos);
                break;
        }

        if (signText != null) {
            GameScene.show(new WndMessage(signText));
        }
    }

    private static void burnSign(int pos) {
        Dungeon.level.destroy(pos);
        GameScene.updateMap(pos);
        GameScene.discoverTile(pos, Terrain.SIGN);

        CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
        Sample.INSTANCE.play(Assets.SND_BURNING);

        GLog.w(TXT_BURN);
    }

    private static <T> List<Integer> randIndexes(T[] array, int seed) {
        ArrayList<Integer> indexes = new ArrayList<>(array.length);
        for (int i = 0; i < array.length; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes, new java.util.Random(seed));
        return indexes;
    }
}
