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
package com.etoitau.pixeldungeon.items.weapon.missiles;

import android.graphics.Color;

import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.mobs.Bestiary;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.actors.mobs.npcs.NPC;
import com.etoitau.pixeldungeon.actors.mobs.npcs.SummonedPet;
import com.etoitau.pixeldungeon.effects.Splash;
import com.etoitau.pixeldungeon.effects.particles.ShadowParticle;
import com.etoitau.pixeldungeon.items.SoulCrystalFilled;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Random;

public class SoulCrystal extends MissileWeapon {

    {
        name = "soul crystal";
        image = ItemSpriteSheet.CRYSTAL_EMPTY;
        stackable = true;
    }

    public SoulCrystal() {
        this(1);
    }

    public SoulCrystal(int number) {
        super();
        quantity = number;
    }

    @Override
    public int min() {
        return 1;
    }

    @Override
    public int max() {
        return 4;
    }

    @Override
    public String desc() {
        return
                "A magical crystal capable of capturing soul essence. Throw this at a weak or weakened foe and capture his spirit.";
    }

    @Override
    public String info() {

        StringBuilder info = new StringBuilder(desc());


        return info.toString();
    }


    @Override
    protected void onThrow(int cell) {
        if (Actor.findChar(cell) != null && Actor.findChar(cell) instanceof Mob && (!(Actor.findChar(cell) instanceof NPC) || Actor.findChar(cell) instanceof SummonedPet) && Actor.findChar(cell).champ == -1 && !Bestiary.isBoss(Actor.findChar(cell)) && (Random.Int(10, 20) > Actor.findChar(cell).HP || Actor.findChar(cell) instanceof SummonedPet)) {
            Actor.findChar(cell).sprite.emitter().burst(ShadowParticle.CURSE, 6);
            Sample.INSTANCE.play(Assets.SND_CURSED);
            GLog.p("Captured " + Actor.findChar(cell).name + "!");
            SoulCrystalFilled crystal = new SoulCrystalFilled(((Mob) Actor.findChar(cell)).spriteClass, Actor.findChar(cell).HT, ((Mob) Actor.findChar(cell)).defenseSkill, Actor.findChar(cell).name);
            Dungeon.level.drop(crystal, cell).sprite.drop();
            Actor.findChar(cell).damage(Actor.findChar(cell).HP, Dungeon.hero);

        } else if (Dungeon.visible[cell]) {
            GLog.i("The " + name + " shatters");
            Sample.INSTANCE.play(Assets.SND_SHATTER);
            Splash.at(cell, Color.parseColor("#50FFFFFF"), 5);
        }

    }

    @Override
    public int price() {
        return quantity * 12;
    }
}
