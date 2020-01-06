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
package com.etoitau.pixeldungeon.actors.mobs;

import java.util.HashSet;

import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.ResultDescriptions;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.items.Generator;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.weapon.enchantments.Death;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.sprites.SkeletonSprite;
import com.etoitau.pixeldungeon.utils.GLog;
import com.etoitau.pixeldungeon.utils.Utils;
import com.watabau.utils.Random;

public class Skeleton extends Mob {

    private static final String TXT_HERO_KILLED = "You were killed by the explosion of bones...";

    {
        name = "skeleton";
        spriteClass = SkeletonSprite.class;

        HP = HT = 25;
        defenseSkill = 9;

        EXP = 5;
        maxLvl = 10;

        HT *= Dungeon.currentDifficulty.mobHPModifier();
        HP = HT;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(3, 8);
    }

    @Override
    public void die(Object cause) {

        super.die(cause);

        boolean heroKilled = false;
        for (int cell: Level.aroundEight(pos)) {
            Char ch = findChar(cell);
            if (ch != null && ch.isAlive()) {
                int damage = Math.max(0, damageRoll() - Random.IntRange(0, ch.dr() / 2));
                if (ch == Dungeon.hero)
                    ch.damage(damage - Dungeon.hero.heroSkills.passiveA3.incomingDamageReduction(damage), this);
                else
                    ch.damage(damage, this);
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
            }
        }

        if (Dungeon.visible[pos]) {
            Sample.INSTANCE.play(Assets.SND_BONES);
        }

        if (heroKilled) {
            Dungeon.fail(Utils.format(ResultDescriptions.MOB, Utils.indefinite(name), Dungeon.depth));
            GLog.n(TXT_HERO_KILLED);
        }
    }

    @Override
    protected void dropLoot() {
        if (Random.Int(5) == 0) {
            Item loot = Generator.random(Generator.Category.WEAPON);
            for (int i = 0; i < 2; i++) {
                Item l = Generator.random(Generator.Category.WEAPON);
                if (l.level() < loot.level()) {
                    loot = l;
                }
            }
            Dungeon.level.drop(loot, pos).sprite.drop();
        }
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int dr() {
        return 5;
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        champEffect(enemy, damage);
        return damage;
    }

    @Override
    public String description() {
        return
                "Skeletons are composed of corpses bones from unlucky adventurers and inhabitants of the dungeon, " +
                        "animated by emanations of evil magic from the depths below. After they have been " +
                        "damaged enough, they disintegrate in an explosion of bones.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        IMMUNITIES.add(Death.class);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
