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
package com.etoitau.pixeldungeon.items.armor;

import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Burning;
import com.etoitau.pixeldungeon.actors.buffs.Roots;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.hero.HeroClass;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.effects.particles.ElmoParticle;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.utils.GLog;

public class MageArmor extends ClassArmor {

    private static final String AC_SPECIAL = "MOLTEN EARTH";

    private static final String TXT_NOT_MAGE = "Only mages can use this armor!";

    {
        name = "mage robe";
        image = ItemSpriteSheet.ARMOR_MAGE;
    }

    @Override
    public String special() {
        return AC_SPECIAL;
    }

    @Override
    public String desc() {
        return
                "Wearing this gorgeous robe, a mage can cast a spell of molten earth: all the enemies " +
                        "in his field of view will be set on fire and unable to move at the same time.";
    }

    @Override
    public void doSpecial() {

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.fieldOfView[mob.pos]) {
                Buff.affect(mob, Burning.class).reignite(mob);
                Buff.prolong(mob, Roots.class, 3);
            }
        }

        curUser.HP -= (curUser.HP / 3);

        curUser.spend(Actor.TICK);
        curUser.sprite.operate(curUser.pos);
        curUser.busy();

        curUser.sprite.centerEmitter().start(ElmoParticle.FACTORY, 0.15f, 4);
        Sample.INSTANCE.play(Assets.SND_READ);
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (hero.heroClass == HeroClass.MAGE) {
            return super.doEquip(hero);
        } else {
            GLog.w(TXT_NOT_MAGE);
            return false;
        }
    }
}