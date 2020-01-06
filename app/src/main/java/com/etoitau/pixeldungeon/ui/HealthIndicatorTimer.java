/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019-2020 Kyle Chatman
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
package com.etoitau.pixeldungeon.ui;

import com.etoitau.pixeldungeon.actors.Actor;

public class HealthIndicatorTimer extends Actor {

    public boolean hasTime = false;

    @Override
    protected boolean act() {
        hasTime = false;
        diactivate();
        return true;
    }

    public void setTimer(float time) {
        postpone(time);
        hasTime = true;
    }

}
