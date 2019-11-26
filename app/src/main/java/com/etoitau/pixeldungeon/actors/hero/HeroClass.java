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
package com.etoitau.pixeldungeon.actors.hero;

import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Badges;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.skills.CurrentSkills;
import com.etoitau.pixeldungeon.actors.skills.Skill;
import com.etoitau.pixeldungeon.items.ArmorKit;
import com.etoitau.pixeldungeon.items.SoulCrystalFilled;
import com.etoitau.pixeldungeon.items.TomeOfMastery;
import com.etoitau.pixeldungeon.items.armor.ClothArmor;
import com.etoitau.pixeldungeon.items.armor.ScaleArmor;
import com.etoitau.pixeldungeon.items.bags.Keyring;
import com.etoitau.pixeldungeon.items.bags.ScrollHolder;
import com.etoitau.pixeldungeon.items.food.Food;
import com.etoitau.pixeldungeon.items.potions.PotionOfHealing;
import com.etoitau.pixeldungeon.items.potions.PotionOfMana;
import com.etoitau.pixeldungeon.items.potions.PotionOfMindVision;
import com.etoitau.pixeldungeon.items.potions.PotionOfStrength;
import com.etoitau.pixeldungeon.items.rings.RingOfShadows;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfBloodyRitual;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfFrostLevel;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfHome;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfSacrifice;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfSkill;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfWipeOut;
import com.etoitau.pixeldungeon.items.wands.WandOfMagicMissile;
import com.etoitau.pixeldungeon.items.weapon.melee.Dagger;
import com.etoitau.pixeldungeon.items.weapon.melee.DualSwords;
import com.etoitau.pixeldungeon.items.weapon.melee.Knuckles;
import com.etoitau.pixeldungeon.items.weapon.melee.ShortSword;
import com.etoitau.pixeldungeon.items.weapon.missiles.Arrow;
import com.etoitau.pixeldungeon.items.weapon.missiles.Bow;
import com.etoitau.pixeldungeon.items.weapon.missiles.CupidArrow;
import com.etoitau.pixeldungeon.items.weapon.missiles.Dart;
import com.etoitau.pixeldungeon.items.weapon.missiles.Boomerang;
import com.etoitau.pixeldungeon.items.weapon.missiles.Shuriken;
import com.etoitau.pixeldungeon.items.weapon.missiles.SoulCrystal;
import com.etoitau.pixeldungeon.sprites.EyeSprite;
import com.etoitau.pixeldungeon.ui.QuickSlot;
import com.watabau.utils.Bundle;

public enum HeroClass {

    WARRIOR("warrior"), MAGE("mage"), ROGUE("rogue"), HUNTRESS("huntress"), HATSUNE("hatsune");

    private String title;

    private HeroClass(String title) {
        this.title = title;
    }

    public static final String[] WAR_PERKS = {
            "Warriors start with 11 points of Strength.",
            "Warriors start with a unique short sword. This sword can be later \"reforged\" to upgrade another melee weapon.",
            "Warriors are less proficient with missile weapons.",
            "Any piece of food restores some health when eaten.",
            "Potions of Strength are identified from the beginning.",
    };

    public static final String[] MAG_PERKS = {
            "Mages start with a unique Wand of Magic Missile. This wand can be later \"disenchanted\" to upgrade another wand.",
            "Mages recharge their wands faster.",
            "When eaten, any piece of food restores 1 charge for all wands in the inventory.",
            "Mages can use wands as a melee weapon.",
            "Scrolls of Identify are identified from the beginning.",
            "Master of magic."
    };

    public static final String[] ROG_PERKS = {
            "Rogues start with a Ring of Shadows+1.",
            "Rogues identify a type of a ring on equipping it.",
            "Rogues are proficient with light armor, dodging better while wearing one.",
            "Rogues are proficient in detecting hidden doors and traps.",
            "Rogues can go without food longer.",
            "Scrolls of Magic Mapping are identified from the beginning."
    };

    public static final String[] HUN_PERKS = {
            "Huntresses start with 15 points of Health.",
            "Huntresses start with a unique upgradeable boomerang.",
            "Huntresses are proficient with missile weapons and get a damage bonus for excessive strength when using them.",
            "Huntresses gain more health from dewdrops.",
            "Huntresses sense neighbouring monsters even if they are hidden behind obstacles."
    };

    public static final String[] LEGEND_PERKS = {
            "Hatsune is believed to be a descendant of an Avatar who broke the rules and interacted with mortals.",
            "She is best known for leading the failed defence of the town of Boonamai.",
            "She is the first to give birth to twin daughters instead of one. A first in a lineage of over 10 generations.",
            "She excels in tactics and has mastered both light and dark arts.",
            "Her hair has turned blue from her massive spiritual strength."
    };

    public void initHero(Hero hero) {

        hero.heroClass = this;

        initCommon(hero);

        switch (this) {
            case WARRIOR:
                initWarrior(hero);
                break;

            case MAGE:
                initMage(hero);
                break;

            case ROGUE:
                initRogue(hero);
                break;

            case HUNTRESS:
                initHuntress(hero);
                break;
        }

        if (Badges.isUnlocked(masteryBadge())) {
            new TomeOfMastery().collect();
        }

        hero.updateAwareness();
    }

    private static void initCommon(Hero hero) {
        (hero.belongings.armor = new ClothArmor()).identify();
        new Food().identify().collect();
        new Keyring().collect();

        Dungeon.hero.HP -= Dungeon.currentDifficulty.difficultyHPStartPenalty();
        Dungeon.hero.HT -= Dungeon.currentDifficulty.difficultyHPStartPenalty();
        Dungeon.currentDifficulty.difficultyStartItemBonus();

        Skill.availableSkill = Skill.STARTING_SKILL;

        Bow tmp = new Bow(1);
        tmp.collect();
        tmp.doEquip(hero);
        new Arrow(15).collect();
        new CupidArrow(5).collect();

        new ScrollOfHome().setKnown();
        new ScrollOfSacrifice().setKnown();
        new ScrollOfBloodyRitual().setKnown();
        new ScrollOfSkill().setKnown();
        new ScrollOfFrostLevel().setKnown();


        new ScrollOfHome().collect();
        // new ScrollOfSacrifice().collect();
        // new ScrollOfBloodyRitual().collect();
        new ScrollOfSkill().collect();
        // new ScrollOfMagicMapping().identify().collect();
        // new ScrollOfFrostLevel().collect();

        // new Ankh().collect();
        new PotionOfHealing().setKnown();
        new PotionOfMana().setKnown();

        new PotionOfMana().collect();
        new PotionOfMana().collect();
        new PotionOfMana().collect();

        // new ScrollOfEnchantment().identify().collect();

        new SoulCrystal(3).collect();
        new SoulCrystalFilled(EyeSprite.class, 50, 20, "Captured Evil Eye").collect();
        // new PotionOfMindVision().collect();
        //new ArmorKit().collect();
        // new ScrollHolder().collect();
    }

    public Badges.Badge masteryBadge() {
        switch (this) {
            case WARRIOR:
                return Badges.Badge.MASTERY_WARRIOR;
            case MAGE:
                return Badges.Badge.MASTERY_MAGE;
            case ROGUE:
                return Badges.Badge.MASTERY_ROGUE;
            case HUNTRESS:
                return Badges.Badge.MASTERY_HUNTRESS;
        }
        return null;
    }

    private static void initWarrior(Hero hero) {
        hero.STR = hero.STR + 1;
        hero.MP = hero.MMP = 20;
        (hero.belongings.weapon = new ShortSword()).identify();
        new Dart(8).identify().collect();

        QuickSlot.primaryValue = Dart.class;

        new PotionOfStrength().setKnown();

        hero.heroSkills = CurrentSkills.WARRIOR;
        hero.heroSkills.init();
    }

    private static void initMage(Hero hero) {
        hero.MP = hero.MMP = 40;

        (hero.belongings.weapon = new Knuckles()).identify();

        WandOfMagicMissile wand = new WandOfMagicMissile();
        wand.identify().collect();

        QuickSlot.primaryValue = wand;

        new ScrollOfIdentify().setKnown();

        hero.heroSkills = CurrentSkills.MAGE;
        hero.heroSkills.init();
    }

    private static void initRogue(Hero hero) {
        hero.MP = hero.MMP = 30;
        //(hero.belongings.weapon = new Dagger()).identify();
        (hero.belongings.weapon = new DualSwords()).identify();
        (hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
        new Dart(8).identify().collect();
        new Shuriken(10).identify().collect();
        hero.belongings.ring1.activate(hero);

        QuickSlot.primaryValue = Dart.class;

        new ScrollOfMagicMapping().setKnown();

        hero.heroSkills = CurrentSkills.ROGUE;
        hero.heroSkills.init();
    }

    private static void initHuntress(Hero hero) {
        hero.MP = hero.MMP = 35;
        hero.HP = (hero.HT -= 5);

        (hero.belongings.weapon = new Dagger()).identify();
        Boomerang boomerang = new Boomerang();
        boomerang.identify().collect();

        QuickSlot.primaryValue = boomerang;

        hero.heroSkills = CurrentSkills.HUNTRESS;
        hero.heroSkills.init();
    }

    public String title() {
        return title;
    }

    public String spritesheet() {

        switch (this) {
            case WARRIOR:
                return Assets.WARRIOR;
            case MAGE:
                return Assets.MAGE;
            case ROGUE:
                return Assets.ROGUE;
            case HUNTRESS:
                return Assets.HUNTRESS;
            case HATSUNE:
                return Assets.LEGEND;
        }

        return null;
    }

    public String[] perks() {

        switch (this) {
            case WARRIOR:
                return WAR_PERKS;
            case MAGE:
                return MAG_PERKS;
            case ROGUE:
                return ROG_PERKS;
            case HUNTRESS:
                return HUN_PERKS;
            case HATSUNE:
                return LEGEND_PERKS;
        }

        return null;
    }

    private static final String CLASS = "class";

    public void storeInBundle(Bundle bundle) {
        bundle.put(CLASS, toString());
    }

    public static HeroClass restoreInBundle(Bundle bundle) {
        String value = bundle.getString(CLASS);
        return value.length() > 0 ? valueOf(value) : ROGUE;
    }
}
