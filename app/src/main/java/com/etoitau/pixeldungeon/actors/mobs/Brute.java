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
import com.etoitau.pixeldungeon.actors.buffs.Terror;
import com.etoitau.pixeldungeon.items.Gold;
import com.etoitau.pixeldungeon.sprites.BruteSprite;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Bundle;
import com.watabau.utils.Random;

public class Brute extends Mob {

    private static final String TXT_ENRAGED = "%s becomes enraged!";

    {
        name = "gnoll brute";
        spriteClass = BruteSprite.class;

        HP = HT = 40;
        defenseSkill = 15;

        EXP = 8;
        maxLvl = 15;

        loot = Gold.class;
        lootChance = 0.5f;

        HT *= Dungeon.currentDifficulty.mobHPModifier();
        HP = HT;
    }

    private boolean enraged = false;

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        enraged = HP < HT / 4;
    }

    @Override
    public int damageRoll() {
        return enraged ?
                Random.NormalIntRange(10, 40) :
                Random.NormalIntRange(8, 18);
    }

    @Override
    public int attackSkill(Char target) {
        return 20;
    }

    @Override
    public int dr() {
        return 8;
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);

        if (isAlive() && !enraged && HP < HT / 4) {
            enraged = true;
            spend(TICK);
            if (Dungeon.visible[pos]) {
                GLog.w(TXT_ENRAGED, name);
                sprite.showStatus(CharSprite.NEGATIVE, "enraged");
            }
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        champEffect(enemy, damage);
        return damage;
    }

    @Override
    public String description() {
        return
                "Brutes are the largest, strongest and toughest of all gnolls. When severely wounded, " +
                        "they go berserk, inflicting even more damage to their enemies.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        IMMUNITIES.add(Terror.class);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
