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
package com.etoitau.pixeldungeon.levels.features;

import com.etoitau.pixeldungeon.Challenges;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.buffs.Barkskin;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.hero.HeroSubClass;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.particles.LeafParticle;
import com.etoitau.pixeldungeon.items.Dewdrop;
import com.etoitau.pixeldungeon.items.Generator;
import com.etoitau.pixeldungeon.items.rings.Ring;
import com.etoitau.pixeldungeon.items.rings.RingOfHerbalism;
import com.etoitau.pixeldungeon.items.rings.RingOfHerbalism.Herbalism;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.levels.Terrain;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.watabau.utils.Random;

public class HighGrass {

    public static void trample(Level level, int pos, Char ch) {

        Level.set(pos, Terrain.GRASS);
        GameScene.updateMap(pos);

        if (!Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
            int herbalismLevel = 0;

            if (ch != null) {
                for (Buff buff: ch.buffs(RingOfHerbalism.Herbalism.class)) {
                    herbalismLevel += ((Ring.RingBuff) buff).level;
                }
            }
            
            // Seed
            if (herbalismLevel >= 0 && Random.Int(18) <= Random.Int(herbalismLevel + 1)) {
                level.drop(Generator.random(Generator.Category.SEED), pos).sprite.drop();
            }

            // Dew
            if (herbalismLevel >= 0 && Random.Int(6) <= Random.Int(herbalismLevel + 1)) {
                level.drop(new Dewdrop(), pos).sprite.drop();
            }
        }

        int leaves = 4;

        // Warden's barkskin
        if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
            Buff.affect(ch, Barkskin.class).level(ch.HT / 3);
            leaves = 8;
        }

        CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, leaves);
        Dungeon.observe();
    }
}
