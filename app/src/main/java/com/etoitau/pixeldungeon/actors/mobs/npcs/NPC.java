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
package com.etoitau.pixeldungeon.actors.mobs.npcs;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.items.Heap;
import com.etoitau.pixeldungeon.levels.Level;
import com.watabau.utils.Random;

import java.util.Collections;
import java.util.List;

public abstract class NPC extends Mob {

    {
        HP = HT = 1;
        EXP = 0;

        hostile = false;
        state = PASSIVE;
    }

    protected void throwItem() {
        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            int n = pos;

            List<Integer> cells = Level.aroundEight(pos);
            Collections.shuffle(cells);

            for (int cell: cells) {
                if (Level.passable[cell] || Level.avoid[cell]) {
                    n = cell;
                    break;
                }
            }

            Dungeon.level.drop(heap.pickUp(), n).sprite.drop(pos);
        }
    }

    @Override
    public void beckon(int cell) {
    }

    abstract public void interact();
}
