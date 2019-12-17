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

package com.etoitau.pixeldungeon.actors.skills;


import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.etoitau.pixeldungeon.windows.WndStory;
import com.watabau.utils.Bundle;

import java.util.ArrayList;


public class Skill {

    public static final String AC_ADVANCE = "Advance";
    public static final String AC_ACTIVATE = "Activate";
    public static final String AC_DEACTIVATE = "Deactivate";

    public static final String AC_SUMMON = "Summon";
    public static final String AC_CAST = "Cast";

    public static final String SKILL_LEVEL = "LEVEL";

    public String tag = "";

    public static final int MAX_LEVEL = 3;

    public static final int STARTING_SKILL = 2;

    public static int availableSkill = STARTING_SKILL;

    public static final float TIME_TO_USE = 1f;

    public String name = "Skill";
    public String castText = "";
    public int level = 0; // level is 1, 2, or 3 - number of times this skill has been upgraded
    public int tier = 1;
    public int mana = 0;
    public int image = 0;

    public boolean active = false;

    public boolean multiTargetActive = false;

    public boolean requestUpgrade() {
        if (availableSkill >= tier && level < MAX_LEVEL) {
            if (upgrade()) {
                level++;
                availableSkill -= tier;
                return true;
            }
        } else {

//            WndStory.showStory(FAIL_ADVANCE);
        }

        return false;
    }

    protected boolean upgrade() {
        return false;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float incomingDamageModifier() {
        return 1f;
    }

    public float damageModifier() {
        return 1f;
    }

    public int damageBonus(int hp) {
        return damageBonus(hp, false);
    }

    public int damageBonus(int hp, boolean castText) {
        return 0;
    }

    public int toHitBonus() {
        return 0;
    }

    public int healthRegenerationBonus() {
        return 0;
    }

    public int weaponLevelBonus() {
        return 0;
    }

    public int fletching() {
        return 0;
    }

    public int hunting() {
        return 0;
    }

    public boolean knocksBack() {
        return false;
    }

    public boolean AoEDamage() {
        return false;
    }

    public int manaRegenerationBonus() {
        return 0;
    }

    public int incomingDamageReduction(int damage) {
        return 0;
    }

    public int image() {
        return image;
    }

    public String info() {
        return "";
    }

    // this returns empty list, but other classes (e.g. RoguePassiveA) extend and override and do something
    public ArrayList<String> actions(Hero hero) {
        return new ArrayList<>();
    }

    public boolean execute(Hero hero, String action) {
        return true;
    }

    public float getAlpha() {
        return 0.1f + level * 0.3f;
    }

    public int upgradeCost() {
        return tier;
    }

    protected int totalSpent() {
        return 0;
    }

    protected int nextUpgradeCost() {
        return 0;
    }

    protected boolean canUpgrade() {
        return false;
    }

    public String costUpgradeInfo() {
        return name + " is at level " + level + ".\n"
                + (level < Skill.MAX_LEVEL ? "It costs " + upgradeCost() + " skill points to advance in " + name + "." : name + " is maxed out.")
                + (level > 0 && mana > 0 ? "\nUsing " + name + " costs " + getManaCost() + " mana." : "");
    }

    public int getManaCost() {
        return mana;
    }

    public void castTextYell() {
        if (!castText.equals(""))
            Dungeon.hero.sprite.showStatus(CharSprite.NEUTRAL, castText);
    }

    public float wandRechargeSpeedReduction() {
        return 1f;
    }

    public int summoningLimitBonus() {
        return 0;
    }

    public float wandDamageBonus() {
        return 1f;
    }

    public int lootBonus(int gold) {
        return 0;
    }

    public int stealthBonus() {
        return 0;
    }

    public boolean disableTrap() {
        return false;
    }

    public int venomousAttack() {
        return 0;
    }

    public int venomBonus() {
        return 0;
    }

    public boolean instantKill() {
        return false;
    }

    public boolean dodgeChance() {
        return false;
    }

    public float toHitModifier() {
        return 1f;
    }

    public boolean cripple() {
        return false;
    }

    public int passThroughTargets(boolean shout) {
        return 0;
    }

    // can hero apply AimedShot?
    public boolean aimedShot() {
        return false;
    }

    // damage modifier from AimedShot
    public float rangedDamageModifier() {
        return 1f;
    }

    public boolean doubleShot() {
        return false;
    }

    public boolean doubleStab() {
        return false;
    }

    // aka Bombvoyage
    public boolean arrowToBomb() {
        return false;
    }

    public boolean goToSleep() {
        return false;
    }

    public void storeInBundle(Bundle bundle) {
        bundle.put(SKILL_LEVEL + " " + tag, level); // e.g. Rampage could be key: "LEVEL A3" and value: 2
    }

    public void restoreInBundle(Bundle bundle) {
        level = bundle.getInt(SKILL_LEVEL + " " + tag);
    }
}
