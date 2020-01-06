/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019-2020 Kyle Chatman
 *
 * Based on:
 *
 * Skillful Pixel Dungeon by BilbolDev
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


import com.etoitau.pixeldungeon.items.Ankh;
import com.etoitau.pixeldungeon.items.AnkhCracked;
import com.etoitau.pixeldungeon.items.armor.PlateArmor;
import com.etoitau.pixeldungeon.items.bags.PotionBelt;
import com.etoitau.pixeldungeon.items.bags.ScrollHolder;
import com.etoitau.pixeldungeon.items.bags.SeedPouch;
import com.etoitau.pixeldungeon.items.bags.WandHolster;
import com.etoitau.pixeldungeon.items.food.Food;
import com.etoitau.pixeldungeon.items.potions.Potion;
import com.etoitau.pixeldungeon.items.potions.PotionOfExperience;
import com.etoitau.pixeldungeon.items.potions.PotionOfHealing;
import com.etoitau.pixeldungeon.items.potions.PotionOfStrength;
import com.etoitau.pixeldungeon.items.rings.Ring;
import com.etoitau.pixeldungeon.items.scrolls.Scroll;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfFrostLevel;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.etoitau.pixeldungeon.items.wands.Wand;
import com.etoitau.pixeldungeon.items.wands.WandOfAvalanche;
import com.etoitau.pixeldungeon.items.wands.WandOfDisintegration;
import com.etoitau.pixeldungeon.items.wands.WandOfFirebolt;
import com.etoitau.pixeldungeon.items.wands.WandOfLightning;
import com.etoitau.pixeldungeon.items.wands.WandOfMagicMissile;
import com.etoitau.pixeldungeon.items.wands.WandOfPoison;
import com.etoitau.pixeldungeon.items.weapon.melee.WarHammer;
import com.etoitau.pixeldungeon.items.weapon.missiles.SoulCrystal;

import java.util.ArrayList;


public enum Difficulties {

    NORMAL(0), EASY(1), HARD(2), HELL(3), SUICIDE(4), JUSTKILLME(5), SUPEREASY(6);

    private int difficulty;

    private int championOffset = 0;
    public float hpOffset = 0;
    public float attOffset = 0;
    public float defOffset = 0;
    public float defenceOffset = 0;

    // todo remove?
    private ArrayList<Integer> disabledChampions = new ArrayList<>();

    Difficulties(int difficulty) {
        this.difficulty = difficulty;
        championOffset = 0;
        hpOffset = 0;
        attOffset = 0;
        defOffset = 0;
        disabledChampions.clear();
    }

    public static final String[] SUPER_EASY_DESC = {
            "- Start with 5 extra rations.",
            "- Start with 5 potions of healing.",
            "- Start with 500 Gold.",
            "- Start with 3 soul crystals.",
            "- Mobs do 50% less damage, take 50% more damage and have 50% less HP.",
            "- Hunger is reduced by 40%",
            "- Champion spawn rate set to 10%.",
            "- Always day time"
    };

    public static final String[] EASY_DESC = {
            "- Start with 2 extra rations.",
            "- Start with 2 potions of healing.",
            "- Start with 200 Gold.",
            "- Start with 2 soul crystals.",
            "- Bonus to discovering hidden doors and traps.",
            "- Mobs do 25% less damage, take 10% more damage and have 15% less HP.",
            "- Hunger is reduced by 20%",
            "- Champion spawn rate set to 10%.",
            "- Night is 6 hours long"
    };

    public static final String[] NORMAL_DESC = {
            "- Mobs are standard.",
            "- Champion spawn rate set to 20%.",
            "- Night is 8 hours long"
    };

    public static final String[] HARD_DESC = {
            "- Potion of healing heals 75% max hp.",
            "- Mobs do 10% extra damage, take 10% less damage and have 20% more HP.",
            "- Champion spawn rate set to 30%.",
            "- Night is 10 hours long"
    };

    public static final String[] HELL_DESC = {
            "- Potion of healing heals 50% max hp.",
            "- Mobs do 25% more damage, take 20% less damage and have 35% more HP.",
            "- Champion spawn rate set to 40%.",
            "- Hero starts with 4 less maxHP.",
            "- Hero gains 1 less maxHP on leveling.",
            "- Hero starts with 4 skill points.",
            "- Night is 12 hours long"
    };

    public static final String[] SUICIDE_DESC = {
            "- Potion of healing heals 25% max hp.",
            "- Mobs do 45% more damage, take 30% less damage and have 60% more HP.",
            "- Champion spawn rate set to 50%.",
            "- Hero starts with 8 less maxHP.",
            "- Hero gains 3 less maxHP on leveling.",
            "- Hero starts with 6 skill points.",
            "- Night is 18 hours long"
    };

    public static final String[] JUST_KILL_ME_DESC = {
            "- Potion of healing heals 10% max hp.",
            "- Mobs do 60% more damage, take 40% less damage and have 75% more HP.",
            "- Champion spawn rate set to 100%.",
            "- Hero starts with 8 less maxHP.",
            "- Hero gains 3 less maxHP on leveling.",
            "- Hero starts with 8 skill points.",
            "- Always night"
    };


    public String title() {

        switch (this) {
            case SUPEREASY:
                return "Super Easy";
            case EASY:
                return "Easy";
            case NORMAL:
                return "Normal";
            case HARD:
                return "Hard";
            case HELL:
                return "Hell!";
            case SUICIDE:
                return "Suicide!!";
            case JUSTKILLME:
                return "Just Kill Me";
        }
        return "";
    }

    public String description() {

        switch (this) {
            case SUPEREASY:
                return join("\n", SUPER_EASY_DESC);
            case EASY:
                return join("\n", EASY_DESC);
            case NORMAL:
                return join("\n", NORMAL_DESC);
            case HARD:
                return join("\n", HARD_DESC);
            case HELL:
                return join("\n", HELL_DESC);
            case SUICIDE:
                return join("\n", SUICIDE_DESC);
            case JUSTKILLME:
                return join("\n", JUST_KILL_ME_DESC);
        }
        return "";
    }

    public static String description(int difficulty) {
        try {
            return Difficulties.values()[difficulty].description();
        } catch (Exception ex) {
            return ""; // Invalid difficulty
        }
    }

    public static String title(int difficulty) {
        try {
            return Difficulties.values()[difficulty].title();
        } catch (Exception ex) {
            return ""; // Invalid difficulty
        }
    }

    public int championChance() {
        return championChanceNatural() + championOffset;
    }

    public int championChanceNatural() {

        switch (this) {
            case SUPEREASY:
            case EASY:
                return 1;
            case NORMAL:
                return 2;
            case HARD:
                return 3;
            case HELL:
                return 4;
            case SUICIDE:
                return 5;
            case JUSTKILLME:
                return 10;
        }
        return 0;
    }

    public float damageModifier() {
        return naturalDamageModifier() + attOffset;
    }

    public float naturalDamageModifier() {

        switch (this) {
            case SUPEREASY:
                return 0.5f;
            case EASY:
                return 0.75f;
            case NORMAL:
                return 1f;
            case HARD:
                return 1.1f;
            case HELL:
                return 1.25f;
            case SUICIDE:
                return 1.45f;
            case JUSTKILLME:
                return 1.6f;
        }
        return 1f;
    }


    public float mobDefenceModifier() {

        return naturalMobDefenceModifier() + defenceOffset;
    }

    public float naturalMobDefenceModifier() { // dmg *= this

        switch (this) {
            case SUPEREASY:
                return 1.5f;
            case EASY:
                return 1.1f;
            case NORMAL:
                return 1f;
            case HARD:
                return 0.9f;
            case HELL:
                return 0.8f;
            case SUICIDE:
                return 0.7f;
            case JUSTKILLME:
                return 0.6f;
        }
        return 1f;
    }

    public float mobHPModifier() {

        return naturalMobHPModifier() + hpOffset;
    }

    public float naturalMobHPModifier() {

        switch (this) {
            case SUPEREASY:
                return 0.5f;
            case EASY:
                return 0.85f;
            case NORMAL:
                return 1f;
            case HARD:
                return 1.2f;
            case HELL:
                return 1.35f;
            case SUICIDE:
                return 1.6f;
            case JUSTKILLME:
                return 1.75f;
        }
        return 1f;
    }

    // add reduction in hunger accrual for lower difficulties
    public float naturalHungerModifier() {

        switch (this) {
            case SUPEREASY:
                return 4f;
            case EASY:
                return 2f;
            case NORMAL:
            case HARD:
            case HELL:
            case SUICIDE:
            case JUSTKILLME:
                return 0;
        }
        return 0;

    }

    // night is shorter for easy mode, longer for hard
    public float naturalNightFactor() {
        // return fraction of whole day that is night
        switch (this) {
            case SUPEREASY:
                return 0f;
            case EASY:
                return 6 / 24f;
            case NORMAL:
                return 8 / 24f;
            case HARD:
                return 10 / 24f;
            case HELL:
                return 12 / 24f;
            case SUICIDE:
                return 18 / 24f;
            case JUSTKILLME:
                return 1;
        }
        return 8 / 24f;
    }

    // factor to apply to secret detection ability, factor it modifies ranges from 0.1 to 0.48
    // and secret is detected if greater than rand 0-1
    public float searchModifier() {
        switch (this) {
            case SUPEREASY:
                return 2f;
            case EASY:
                return 1.5f;
            default:
                return 1f;
        }
    }


    public int healingPotionLimit() {

        switch (this) {
            case SUPEREASY:
            case EASY:
            case NORMAL:
                return 100;
            case HARD:
                return 75;
            case HELL:
                return 50;
            case SUICIDE:
                return 25;
            case JUSTKILLME:
                return 15;
        }
        return 100;
    }

    public String healingPotionMessage() {

        switch (this) {
            case SUPEREASY:
            case EASY:
            case NORMAL:
                return "Your wounds heal completely.";

        }
        return "Your wounds are partially healed.";
    }


    public int difficultyHPLevelPenalty() {
        switch (this) {
            case SUPEREASY:
            case EASY:
            case NORMAL:
            case HARD:
                return 0;
            case HELL:
                return 1;
            case SUICIDE:
                return 3;
            case JUSTKILLME:
                return 3;
        }
        return 0;
    }

    public int difficultyHPStartPenalty() {
        switch (this) {
            case SUPEREASY:
            case EASY:
            case NORMAL:
            case HARD:
                return 0;
            case HELL:
                return 4;
            case SUICIDE:
                return 8;
            case JUSTKILLME:
                return 8;
        }
        return 0;
    }

    public int difficultySkillStartBonus() {
        switch (this) {
            case SUPEREASY:
            case EASY:
            case NORMAL:
            case HARD:
                return 0;
            case HELL:
                return 2;
            case SUICIDE:
                return 4;
            case JUSTKILLME:
                return 6;
        }
        return 0;
    }

    public void difficultyStartItemBonus() {
        switch (this) {
            case SUPEREASY:
                new PotionOfHealing().identify().collect();
                new PotionOfHealing().identify().collect();
                new PotionOfHealing().identify().collect();
                new PotionOfHealing().identify().collect();
                new PotionOfHealing().identify().collect();
                new Food().identify().collect();
                new Food().identify().collect();
                new Food().identify().collect();
                new Food().identify().collect();
                new Food().identify().collect();
                new SoulCrystal().identify().collect();
                new SoulCrystal().identify().collect();
                new SoulCrystal().identify().collect();
                new Ankh().collect();
                new Ankh().collect();
                Dungeon.gold = 500;
                break;
            case EASY:
                new PotionOfHealing().identify().collect();
                new PotionOfHealing().identify().collect();
                new Food().identify().collect();
                new Food().identify().collect();
                new SoulCrystal().identify().collect();
                new SoulCrystal().identify().collect();
                new Ankh().collect();
                Dungeon.gold = 200;
                break;
            case NORMAL:
                // god mode for debugging
                if (BuildConfig.DEBUG) {

                    new PotionBelt().collect();
                    new ScrollHolder().collect();
                    new WandHolster().collect();
                    new SeedPouch().collect();

                    for (int i = 0; i < 12; i++) {
                        new ScrollOfUpgrade().identify().collect();
                    }

                    for (int i = 0; i < 60; i++) {
                        new ScrollOfIdentify().collect();
                    }

                    try {
                        for (Class scrollClass: Scroll.getUnknown()) {
                            Scroll scroll = (Scroll) scrollClass.newInstance();
                            if (!(scroll instanceof ScrollOfFrostLevel))
                                scroll.identify().collect();
                        }
                        for (Class potionClass: Potion.getUnknown()) {
                            Potion potion = (Potion) potionClass.newInstance();
                            potion.identify().collect();
                        }
                        for (Class ringClass: Ring.getUnknown()) {
                            Ring ring = (Ring) ringClass.newInstance();
                            ring.identify().collect();
                        }
                        for (Class wandClass: Wand.getUnknown()) {
                            Wand wand = (Wand) wandClass.newInstance();
                            wand.identify().collect();
                        }
                    } catch (Exception e) {
                        // do nothing
                    }



//                    for (int i = 0; i < 25; i++) {
//                        new PotionOfExperience().identify().collect();
//                        new PotionOfStrength().identify().collect();
//                        new ScrollOfMagicMapping().identify().collect();
//                        new ScrollOfEnchantment().identify().collect();
//                    }
                    new PlateArmor().identify().collect();
                    new WarHammer().identify().collect();
                    for (int i = 0; i < 5; i++) {
                        new PotionOfHealing().identify().collect();
                        new Food().identify().collect();
                    }
                    //new WandOfFirebolt().identify().collect();
                    new Ankh().collect();
                    new AnkhCracked().collect();

                }
                break;

        }
    }


    public void reset() {
        championOffset = 0;
        defenceOffset = 0;
        hpOffset = 0;
        attOffset = 0;
        disabledChampions.clear();
    }

    public void changeDefenceOffset(float change) {
        defenceOffset += change;
        if (mobDefenceModifier() > naturalMobDefenceModifier())
            defenceOffset = 0;
        if (mobDefenceModifier() < 0.1f)
            defenceOffset = 0.1f - naturalMobDefenceModifier();
    }

    public void changeChampionOffset(int change) {
        championOffset += change;
        if (championChance() < championChanceNatural())
            championOffset = 0;
        if (championChance() > 10)
            championOffset = 10 - championChanceNatural();
    }

    public void changeHPOffset(float change) {
        hpOffset += change;
        if (mobHPModifier() < naturalMobHPModifier())
            hpOffset = 0;
        if (mobHPModifier() > 2f)
            hpOffset = 2f - naturalMobHPModifier();
    }

    public void changeDamageOffset(float change) {
        attOffset += change;
        if (damageModifier() < naturalDamageModifier())
            attOffset = 0;
        if (damageModifier() > 2f)
            attOffset = 2f - naturalDamageModifier();
    }


    // can input a sensible order of difficulty and get the code number used internally here
    // NORMAL( 0 ), EASY( 1 ), HARD( 2 ), HELL( 3 ), SUICIDE( 4 ) , JUSTKILLME( 5 ), SUPEREASY( 6 )
    public static int getNormalizedDifficulty(int diff) {
        switch (diff) {
            case 0:
                return 6;
            case 1:
                return 1;
            case 2:
                return 0;
            case 3:
                return 2;
            default:
                return 0;
        }
    }

    static String join(String delim, String... data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);
            if (i >= data.length - 1) {
                break;
            }
            sb.append(delim);
        }
        return sb.toString();
    }
}
