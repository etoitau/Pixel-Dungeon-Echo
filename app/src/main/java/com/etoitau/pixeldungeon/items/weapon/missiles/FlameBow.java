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

import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Burning;
import com.etoitau.pixeldungeon.actors.buffs.Frost;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Random;

public class FlameBow extends Bow {

	{
		name = "flame bow";
		image = ItemSpriteSheet.FlameBow;


        stackable = false;
	}



	public FlameBow() {
		this( 1 );
	}

	public FlameBow(int number) {
		super();
		quantity = number;
	}


	@Override
	public String desc() {
		return 
			"A magically imbued bow that has a chance to set targets on fire.";
	}
	
	@Override
	public Item random() {
		quantity = 1;
		return this;
	}
	
	@Override
	public int price() {
		return quantity * 55;
	}



    @Override
    public void bowSpecial(Char target)
    {
        try
        {
            if (Random.Int(2) == 1)
            {
                Buff.affect(target, Burning.class).reignite(target);
                GLog.p("Target catches fire!");

                target.sprite.showStatus( CharSprite.NEUTRAL, "Hot!" );
            }
        }
        catch (Exception ex)
        {

        }
    }
}
