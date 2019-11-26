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
package com.etoitau.pixeldungeon.items.scrolls;

import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.items.wands.WandOfBlink;
import com.etoitau.pixeldungeon.utils.GLog;

public class ScrollOfHome extends Scroll {


    {
        name = "Scroll of Refuge";
    }

    @Override
    protected void doRead() {

        Sample.INSTANCE.play(Assets.SND_READ);

        if (Dungeon.bossLevel() == false) {
            WandOfBlink.appear(curUser, Dungeon.level.entrance);
            GLog.i("The scroll takes you back to the level entrance");
        } else
            GLog.w("Strong magic aura of this place prevents you from teleporting!");

        Dungeon.observe();

        setKnown();

        curUser.spendAndNext(TIME_TO_READ);

    }


    @Override
    public String desc() {
        return
                "Opens a magical portal that takes you back to the entrance of this dungeon level.";
    }

    @Override
    public int price() {
        return isKnown() ? 40 * quantity : super.price();
    }
}
