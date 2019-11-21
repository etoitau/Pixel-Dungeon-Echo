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

import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Charm;
import com.etoitau.pixeldungeon.actors.buffs.Paralysis;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.mobs.Bestiary;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.Speck;
import com.etoitau.pixeldungeon.effects.particles.BlastParticle;
import com.etoitau.pixeldungeon.effects.particles.SmokeParticle;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.levels.Terrain;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabau.utils.Random;

import java.util.ArrayList;

public class CupidArrow extends Arrow {

	{
		name = "cupid arrow";
		image = ItemSpriteSheet.CupidArrow;

        stackable = true;
	}

	public CupidArrow() {
		this( 1 );
	}

	public CupidArrow(int number) {
		super();
		quantity = number;
	}

    @Override
    public Item random() {
        quantity = Random.Int(3, 5);
        return this;
    }

    @Override
    public void arrowEffect(Char attacker, Char defender)
    {
        if(Bestiary.isBoss(defender))
            return;
        int duration = Random.IntRange( 5, 10 );
        Buff.affect( defender, Charm.class, Charm.durationFactor( defender ) * duration ).object = attacker.id();
        defender.sprite.centerEmitter().start( Speck.factory(Speck.HEART), 0.2f, 5 );
    }

	@Override
	public String desc() {
		return 
			"An arrow believed to belong to cupid. Careful who you aim at.";
	}
	

	@Override
	public int price() {
		return quantity * 15;
	}

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if(Dungeon.hero.belongings.bow != null) {
            if(actions.contains(AC_THROW) == false)
            actions.add(AC_THROW);
        }
        else
            actions.remove( AC_THROW );
        actions.remove(AC_EQUIP);

        return actions;
    }
}
