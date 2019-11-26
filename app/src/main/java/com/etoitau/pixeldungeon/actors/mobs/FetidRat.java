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
package com.etoitau.pixeldungeon.actors.mobs;

import java.util.HashSet;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.blobs.Blob;
import com.etoitau.pixeldungeon.actors.blobs.ParalyticGas;
import com.etoitau.pixeldungeon.actors.buffs.Paralysis;
import com.etoitau.pixeldungeon.items.quest.RatSkull;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.FetidRatSprite;
import com.watabau.utils.Random;

public class FetidRat extends Mob {

    {
        name = "fetid rat";
        spriteClass = FetidRatSprite.class;

        HP = HT = 15;
        defenseSkill = 5;

        EXP = 3;
        maxLvl = 5;

        state = WANDERING;

        HT *= Dungeon.currentDifficulty.mobHPModifier();
        HP = HT;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(2, 6);
    }

    @Override
    public int attackSkill(Char target) {
        return 12;
    }

    @Override
    public int dr() {
        return 2;
    }

    @Override
    public String defenseVerb() {
        return "evaded";
    }

    @Override
    public int defenseProc(Char enemy, int damage) {

        GameScene.add(Blob.seed(pos, 20, ParalyticGas.class));

        return super.defenseProc(enemy, damage);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        Dungeon.level.drop(new RatSkull(), pos).sprite.drop();
    }

    @Override
    public String description() {
        return
                "This marsupial rat is much larger than a regular one. It is surrounded by a foul cloud.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        IMMUNITIES.add(Paralysis.class);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
