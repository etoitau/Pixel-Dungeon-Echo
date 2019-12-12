/*
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
package com.etoitau.pixeldungeon.items.scrolls;

import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Invisibility;
import com.etoitau.pixeldungeon.actors.buffs.Rage;
import com.etoitau.pixeldungeon.actors.mobs.Mimic;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.effects.Speck;
import com.etoitau.pixeldungeon.items.Heap;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.utils.GLog;

public class ScrollOfChallenge extends Scroll {

    {
        name = "Scroll of Challenge";
    }

    @Override
    protected void doRead() {

        challengeMobs(curUser.pos);

        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.MIMIC) {
                Mimic m = Mimic.spawnAt(heap.pos, heap.items);
                if (m != null) {
                    m.beckon(curUser.pos);
                    heap.destroy();
                }
            }
        }

        GLog.w("The scroll emits a challenging roar that echoes throughout the dungeon!");
        setKnown();

        curUser.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);
        Invisibility.dispel();

        readAnimation();
    }

    @Override
    public String desc() {
        return
                "When read aloud, this scroll will unleash a challenging roar " +
                        "that will awaken all monsters and alert them to the reader's location.";
    }

    public static void challengeMobs(int pos) {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            mob.beckon(pos);
            if (Dungeon.visible[mob.pos]) {
                Buff.affect(mob, Rage.class, Level.distance(pos, mob.pos));
            }
        }
    }
}
