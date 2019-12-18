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


public class TripleShot extends ActiveSkill2 {


    {
        name = "Triple Shot";
        castText = "Catch!";
        image = 92;
        tier = 2;
        mana = 5;
    }

    private int count = 0; // prevent infinite loop

    @Override
    public boolean execute(Hero hero, String action) {
        if (action == Skill.AC_ACTIVATE) {
            hero.heroSkills.active3.active = false; // Disable Bombvoyage
            hero.heroSkills.active1.active = false; // Disable Aimed Shot
        }
        return super.execute(hero, action);
    }

    @Override
    public boolean doubleShot() {
        if (active == false || Dungeon.hero.MP < getManaCost())
            return false;
        else if (count < 2) {
            count++;
            if (count == 1) {
                castTextYell();
                Dungeon.hero.MP -= getManaCost();
                StatusPane.manaDropping += getManaCost();
            }
            return true;
        }
        count = 0;
        return false;
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Shoots three arrows at the same time.\n"
                + costUpgradeInfo();
    }
}
