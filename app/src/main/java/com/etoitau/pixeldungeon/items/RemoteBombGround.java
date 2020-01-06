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
package com.etoitau.pixeldungeon.items;

import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Paralysis;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.particles.BlastParticle;
import com.etoitau.pixeldungeon.effects.particles.SmokeParticle;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.levels.Terrain;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Bundle;
import com.watabau.utils.Random;

public class RemoteBombGround extends Item {

    {
        name = "remote bomb";
        image = ItemSpriteSheet.RemoteBomb;
        defaultAction = AC_THROW;
        stackable = true;

    }

    public int pos = 0;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("pos", pos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        pos = bundle.getInt("pos");
    }

    @Override
    protected void onThrow(int cell) {
        if (Level.pit[cell]) {
            super.onThrow(cell);
        }


    }


    @Override
    public boolean doPickUp(Hero hero) {
        GLog.i("Cannot be retrieved anymore...");
        return false;
    }


    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public Item random() {
        quantity = 1;
        return this;
    }

    @Override
    public int price() {
        return 10 * quantity;
    }

    @Override
    public String info() {
        return
                "This small bomb will explode as soon as a signal is sent from a trigger beacon.";
    }

    public void explode() {
        Sample.INSTANCE.play(Assets.SND_BLAST, 2);

        if (Dungeon.visible[pos]) {
            CellEmitter.center(pos).burst(BlastParticle.FACTORY, 30);
        }

        boolean terrainAffected = false;
        for (int c: Level.aroundNine(pos)) {
            if (c >= 0 && c < Level.LENGTH) {
                if (Dungeon.visible[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                }

                if (Level.flamable[c]) {
                    Level.set(c, Terrain.EMBERS);
                    GameScene.updateMap(c);
                    terrainAffected = true;
                }

                Char ch = Actor.findChar(c);
                if (ch != null) {
                    int dmg = Random.Int(1 + Dungeon.depth, 10 + Dungeon.depth * 2) - Random.Int(ch.dr());
                    if (dmg > 0) {
                        ch.damage(dmg, this);
                        if (ch.isAlive()) {
                            Buff.prolong(ch, Paralysis.class, 2);
                        }
                    }
                }
            }
        }


    }
}
