/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019-2020 Kyle Chatman
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
import com.etoitau.pixeldungeon.utils.GLog;

import java.util.ArrayList;


public class SpiritArmor extends PassiveSkillA3 {


    {
        name = "Spirit Armor";
        tier = 3;
        image = 27;
        level = 0;
    }

    @Override
    protected boolean upgrade() {
        return true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<String>();
        if (active == false && level > 0)
            actions.add(AC_ACTIVATE);
        else if (level > 0)
            actions.add(AC_DEACTIVATE);

        return actions;
    }

    @Override
    public boolean execute(Hero hero, String action) {
        if (action == Skill.AC_ACTIVATE) {
            active = true;
        } else if (action == Skill.AC_DEACTIVATE) {
            active = false;
        }
        return true;
    }

    @Override
    public int incomingDamageReduction(int damage) {
        if (active == false)
            return 0;
        int maxReduction = (int) (damage * 0.1f * level);
        if (maxReduction == 0 && damage > 0)
            maxReduction = 1;

        if (Dungeon.hero.MP > maxReduction)
            Dungeon.hero.MP -= maxReduction;
        else {
            maxReduction = Dungeon.hero.MP;
            Dungeon.hero.MP = 0;
        }

        if (maxReduction != 0)
            GLog.p(" (Spirit Armor absorbed " + maxReduction + " damage) ");

        return maxReduction;
    }

    @Override
    public String info() {
        return "When activated, 10% of damage per level is taken from mana when possible.\n"
                + costUpgradeInfo();
    }

    private int damageReduction() {
        if (level == 0)
            return 10;

        return level * 10;
    }
}
