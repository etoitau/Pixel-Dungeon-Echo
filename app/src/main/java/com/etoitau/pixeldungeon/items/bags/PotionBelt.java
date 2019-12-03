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
package com.etoitau.pixeldungeon.items.bags;

import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.potions.Potion;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;

public class PotionBelt extends Bag {

    {
        name = "potion belt";
        image = ItemSpriteSheet.BELT;

        size = 12;
    }

    @Override
    public boolean grab( Item item ) {
        return item instanceof Potion;
    }

    @Override
    public int price() {
        return 50;
    }

    @Override
    public String info() {
        return
                "This strong leather belt is capable of storing all your potions while protecting " +
                        "them from shattering due to frost.";
    }

}
