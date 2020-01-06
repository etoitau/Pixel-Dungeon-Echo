/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019-2020 Kyle Chatman
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
package com.etoitau.pixeldungeon.levels.painters;

import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.levels.Room;
import com.etoitau.pixeldungeon.levels.Terrain;

public class EntrancePainter extends Painter {

    public static void paint(Level level, Room room) {

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.EMPTY);

        for (Room.Door door : room.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }

        level.entrance = room.random(1);
        set(level, level.entrance, Terrain.ENTRANCE);

        paintStorage(level, level.entrance);
    }

    public static void paintStorage(Level level, int center) {
        level.storage = -1;
        for (int cell: Level.aroundEight(center)) {
            if (level.map[cell] == Terrain.EMPTY && Actor.findChar(cell) == null)
                level.storage = cell;
        }

        if (level.storage != -1) {
            set(level, level.storage, Terrain.STORAGE);
        }
    }

}
