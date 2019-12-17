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



public class Regeneration extends PassiveSkillA2 {


    {
        name = "Regeneration";
        tier = 2;
        image = 2;
    }


    @Override
    public int healthRegenerationBonus() {
        return level;
    }

    @Override
    protected boolean upgrade() {
        return true;
    }

    @Override
    public String info() {
        return "Regenerate health faster.\n"
                + costUpgradeInfo();
    }

}
