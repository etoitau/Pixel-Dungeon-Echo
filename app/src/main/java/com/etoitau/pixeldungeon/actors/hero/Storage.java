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
package com.etoitau.pixeldungeon.actors.hero;

import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.bags.Bag;
import com.watabau.utils.Bundle;

import java.util.Iterator;

public class Storage implements Iterable<Item> {

    public static final int BACKPACK_SIZE = 5;

    // key for storage and retrieval from bundle
    private static final String BUNDLE_KEY = "inventory2";

    // not really a backpack, but field is exposed and used by services that work on backpack and this
    public Bag backpack;


    public Storage(Hero owner) {

        backpack = new Bag() {{
            name = "Storage";
            size = BACKPACK_SIZE;
        }};
        backpack.owner = owner;
    }


    public void storeInBundle(Bundle bundle) {
        backpack.storeInBundle(bundle, BUNDLE_KEY);
    }

    public void restoreFromBundle(Bundle bundle) {
        backpack.clear();
        backpack.restoreFromBundle(bundle, BUNDLE_KEY);
    }

    @Override
    public Iterator<Item> iterator() {
        return new ItemIterator();
    }

    private class ItemIterator implements Iterator<Item> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Item next() {
            return null;
        }

        @Override
        public void remove() {
        }
    }
}
