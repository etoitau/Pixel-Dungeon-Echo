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
package com.etoitau.pixeldungeon.levels.traps;

import java.util.List;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.mobs.Bestiary;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.items.wands.WandOfBlink;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.watabau.utils.Random;

public class SummoningTrap {

    private static final float DELAY = 2f;

    private static final Mob DUMMY = new Mob() {
    };

    // 0x770088

    public static void trigger(int pos, Char c) {

        if (Dungeon.bossLevel()) {
            return;
        }

        if (c != null) {
            Actor.occupyCell(c);
        }

        int nMobs = 1;
        if (Random.Int(2) == 0) {
            nMobs++;
            if (Random.Int(2) == 0) {
                nMobs++;
            }
        }

        List<Integer> respawnPoints = Level.aroundCell(pos, nMobs, Level.NEIGHBOURS8, true);
        for (int point: respawnPoints) {
            DUMMY.pos = point;
            Actor.occupyCell(DUMMY);

            Mob mob = Bestiary.mob(Dungeon.depth);
            if (mob != null) {
                mob.state = mob.WANDERING;
                GameScene.add(mob, DELAY);
                WandOfBlink.appear(mob, point);
            }
        }
    }
}
