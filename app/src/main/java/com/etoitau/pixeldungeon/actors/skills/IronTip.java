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

import com.watabau.utils.Random;


public class IronTip extends PassiveSkillB3 {


    {
        name = "Iron Tip";
        castText = "Don't forget to share...";
        image = 83;
        tier = 3;
    }

    @Override
    public int passThroughTargets(boolean shout) {
        if (!shout)
            return level;

        if (level > 0 && Random.Int(level + 1) > 0) {
            multiTargetActive = true;
            return level;
        }
        multiTargetActive = false;

        return 0;
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Uses iron arrow tips allowing some arrows to path through their targets and continue their path.\n"
                + costUpgradeInfo();
    }
}
