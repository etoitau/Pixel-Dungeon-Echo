/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
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
package com.etoitau.pixeldungeon.items.wands;

import java.util.ArrayList;
import java.util.HashSet;

import com.etoitau.pixeldungeon.actors.blobs.SacrificialFire;
import com.etoitau.pixeldungeon.ui.HealthIndicatorManager;
import com.watabau.noosa.Camera;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.ResultDescriptions;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.Lightning;
import com.etoitau.pixeldungeon.effects.particles.SparkParticle;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.levels.traps.LightningTrap;
import com.etoitau.pixeldungeon.utils.GLog;
import com.etoitau.pixeldungeon.utils.Utils;
import com.watabau.utils.Callback;
import com.watabau.utils.Random;

public class WandOfLightning extends Wand {

    {
        name = "Wand of Lightning";
    }

    private ArrayList<Char> affected = new ArrayList<Char>();

    private int[] points = new int[20];
    private int nPoints;

    @Override
    protected void onZap(int cell) {
        // Everything is processed in fx() method
        if (!curUser.isAlive()) {
            Dungeon.fail(Utils.format(ResultDescriptions.WAND, name, Dungeon.depth));
            GLog.n("You killed yourself with your own Wand of Lightning...");
        }
    }

    private void hit(Char ch, int damage) {

        if (damage < 1) {
            return;
        }

        if (ch == Dungeon.hero) {
            Camera.main.shake(2, 0.3f);
        } else if (ch instanceof Mob) {
            damage *= Dungeon.hero.heroSkills.passiveB2.wandDamageBonus(); // <---- Mage Sorcerer if present

            SacrificialFire.Marked.spreadFire(Dungeon.hero, ch);
            HealthIndicatorManager.instance.target(ch);
        }

        affected.add(ch);
        ch.damage(Level.water[ch.pos] && !ch.flying ? (int) (damage * 2) : damage, LightningTrap.LIGHTNING);

        ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
        ch.sprite.flash();

        points[nPoints++] = ch.pos;

        HashSet<Char> ns = new HashSet<Char>();
        for (int cell: Level.aroundEight(ch.pos)) {
            Char n = Actor.findChar(cell);
            if (n != null && !affected.contains(n)) {
                ns.add(n);
            }
        }

        if (ns.size() > 0) {
            hit(Random.element(ns), Random.Int(damage / 2, damage));
        }
    }

    @Override
    protected void fx(int cell, Callback callback) {

        nPoints = 0;
        points[nPoints++] = Dungeon.hero.pos;

        Char ch = Actor.findChar(cell);
        if (ch != null) {

            affected.clear();
            int lvl = power();

            hit(ch, Random.Int(5 + lvl / 2, 10 + lvl));

        } else {

            points[nPoints++] = cell;
            CellEmitter.center(cell).burst(SparkParticle.FACTORY, 3);

        }
        curUser.sprite.parent.add(new Lightning(points, nPoints, callback));
    }

    @Override
    public String desc() {
        return
                "This wand conjures forth deadly arcs of electricity, which deal damage " +
                        "to several creatures standing close to each other.";
    }
}
