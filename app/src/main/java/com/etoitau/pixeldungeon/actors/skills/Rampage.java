/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
 *
 * Based on:
 *
 * Skillful Pixel Dungeon
 * Copyright (C) 2017 Moussa
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
import com.etoitau.pixeldungeon.ui.StatusPane;


public class Rampage extends ActiveSkill3 {


    {
        name = "Rampage";
        castText = "Rampage!";
        tier = 3;
        image = 19;
        mana = 5;
    }

    @Override
    public float damageModifier() {
        if (!active || Dungeon.hero.MP < getManaCost())
            return 1f;
        else {
            return 0.4f + 0.2f * level;
        }
    }

    @Override
    public boolean AoEDamage() {
        if (!active || Dungeon.hero.MP < getManaCost())
            return false;
        else {
            castTextYell();
            Dungeon.hero.MP -= getManaCost();
            StatusPane.manaDropping += getManaCost();
            return true;
        }
    }


    @Override
    public boolean execute(Hero hero, String action) {
        if (action == Skill.AC_ACTIVATE) {
            hero.heroSkills.active1.active = false; // Disable Smash
            hero.heroSkills.active2.active = false; // Disable Knockback
        }
        return super.execute(hero, action);
    }

    @Override
    public int getManaCost() {
        return (int) Math.ceil(mana * (1 + 1 * level));
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Less damage but hits all enemies around you.\n"
                + costUpgradeInfo();
    }

}
