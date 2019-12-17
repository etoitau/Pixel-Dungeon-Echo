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

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class WarriorPassiveB extends BranchSkill {


    {
        name = "Melee";
        image = 8;
        level = 0;
    }


    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<String>();
        if (canUpgrade())
            actions.add(AC_ADVANCE);
        return actions;
    }

    @Override
    public boolean execute(Hero hero, String action) {
        if (action == Skill.AC_ADVANCE)
            return hero.heroSkills.advance(CurrentSkills.BRANCHES.PASSIVEB);
        return super.execute(hero, action);
    }

    @Override
    public String info() {
        return "Warriors excel in melee combat.\n"
                + "You have invested a total of " + totalSpent() + " points in this branch.\n"
                + (canUpgrade() ? "Next advancement will cost you " + nextUpgradeCost() + " skill point.\n" : "You can no longer advance in this line");
    }

    @Override
    protected int totalSpent() {
        return Dungeon.hero.heroSkills.totalSpent(CurrentSkills.BRANCHES.PASSIVEB);
    }

    @Override
    protected int nextUpgradeCost() {
        return Dungeon.hero.heroSkills.nextUpgradeCost(CurrentSkills.BRANCHES.PASSIVEB);
    }

    @Override
    protected boolean canUpgrade() {
        return Dungeon.hero.heroSkills.canUpgrade(CurrentSkills.BRANCHES.PASSIVEB);
    }
}
