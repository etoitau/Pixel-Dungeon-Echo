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

public class BranchSkill extends Skill {

    CurrentSkills.BRANCHES branch = CurrentSkills.BRANCHES.ACTIVE;

    @Override
    public float getAlpha() {
        return 1f;
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
            return hero.heroSkills.advance(branch);
        return super.execute(hero, action);
    }

    @Override
    protected int totalSpent() {
        return Dungeon.hero.heroSkills.totalSpent(branch);
    }

    @Override
    protected int nextUpgradeCost() {
        return Dungeon.hero.heroSkills.nextUpgradeCost(branch);
    }

    @Override
    protected boolean canUpgrade() {
        return Dungeon.hero.heroSkills.canUpgrade(branch);
    }


    public String investMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("You have invested a total of ")
                .append(totalSpent()).append((totalSpent() != 1)? " points": " point")
                .append(" in this branch.\n");
        if (canUpgrade()) {
            sb.append("Next advancement will cost you ")
                    .append(nextUpgradeCost())
                    .append(" skill point")
                    .append((nextUpgradeCost() != 1)? "s.": ".")
                    .append("\n");
        } else {
            sb.append("You can no longer advance in this line");
        }

        return sb.toString();
    }


}
