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

import java.util.ArrayList;


public class Smash extends ActiveSkill1 {


    {
        name = "Smash";
        castText = "Smash!";
        tier = 1;
        image = 17;
        mana = 3;
    }

    @Override
    public boolean execute(Hero hero, String action) {
        if (action == Skill.AC_ACTIVATE) {
            hero.heroSkills.active2.active = false; // Disable Knockback
            hero.heroSkills.active3.active = false; // Disable Rampage
        }
        return super.execute(hero, action);
    }


    @Override
    public int getManaCost() {
        return (int) Math.ceil(mana * (1 + 0.55 * level));
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public float damageModifier() {
        if (active == false || Dungeon.hero.MP < getManaCost())
            return 1f;
        else {
            castTextYell();
            Dungeon.hero.MP -= getManaCost();
            StatusPane.manaDropping += getManaCost();
            return 1f + 0.1f * level;
        }
    }

    @Override
    public String info() {
        return "Hits target for more damage.\n"
                + costUpgradeInfo();
    }

}
