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

package com.etoitau.pixeldungeon.items;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;

public class AnkhCracked extends Ankh {

    {
        name = "Cracked Ankh";
        image = ItemSpriteSheet.CRACKED_ANKH;
    }


    @Override
    public void usedTimeMachine() {
        detach(Dungeon.hero.belongings.backpack);
    }

    @Override
    public String info() {
        return
                "This once-great artifact may grant power over life and time, " +
                        "but it doesn't look so good... It can probably undo a mistake, " +
                        "but more than that might be asking too much.";
    }

    @Override
    public int price() {
        return 30 * quantity;
    }
}
