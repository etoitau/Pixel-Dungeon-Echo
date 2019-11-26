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
import com.etoitau.pixeldungeon.actors.buffs.Blindness;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Invisibility;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.watabau.utils.Random;

public class ScrollOfPsionicBlast extends Scroll {

    {
        name = "Scroll of Psionic Blast";
    }

    @Override
    protected void doRead() {

        GameScene.flash(0xFFFFFF);

        Sample.INSTANCE.play(Assets.SND_BLAST);
        Invisibility.dispel();

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (Level.fieldOfView[mob.pos]) {
                Buff.prolong(mob, Blindness.class, Random.Int(3, 6));
                mob.damage(Random.IntRange(1, mob.HT * 2 / 3), this);
            }
        }

        Buff.prolong(curUser, Blindness.class, Random.Int(3, 6));
        Dungeon.observe();

        setKnown();

        readAnimation();
    }

    @Override
    public String desc() {
        return
                "This scroll contains destructive energy, that can be psionically channeled to inflict a " +
                        "massive damage to all creatures within a field of view. An accompanying flash of light will " +
                        "temporarily blind everybody in the area of effect including the reader of the scroll.";
    }

    @Override
    public int price() {
        return isKnown() ? 80 * quantity : super.price();
    }
}
