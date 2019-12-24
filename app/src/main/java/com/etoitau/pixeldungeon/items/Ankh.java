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
import com.etoitau.pixeldungeon.TimeMachine;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.items.bags.Bag;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Ankh extends Item {

    public static final String AC_GO_BACK = "GO BACK";

    {
        stackable = true;
        name = "Ankh";
        image = ItemSpriteSheet.ANKH;
    }

    public void usedTimeMachine() {
        detach(Dungeon.hero.belongings.backpack);
        new AnkhCracked().collect();
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        if (TimeMachine.isFull()) {
            actions.add(AC_GO_BACK);
        }

        return actions;
    }

    @Override
    public boolean collect(Bag container) {
        boolean collected = super.collect(container);
        if (!collected) { return false; }

        TimeMachine.setTimer();
        return collected;
    }

    @Override
    public void execute(final Hero hero, String action) {
        if (action.equals(AC_GO_BACK)) {
            TimeMachine.goBack(this);
        } else {
            super.execute(hero, action);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String info() {
        return
                "This ancient artifact has power over life and time. " +
                        "Use it to undo a mistake or even escape death.";
    }

    @Override
    public int price() {
        return 50 * quantity;
    }
}
