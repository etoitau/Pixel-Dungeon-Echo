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
package com.etoitau.pixeldungeon.items.wands;

import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.effects.MagicMissile;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Callback;

public class WandOfTeleportation extends Wand {

    private static final String TXT_TELEPORTED =
            "In a blink of an eye you were teleported to another location of the level.";

    private static final String TXT_NO_TELEPORT =
            "Strong magic aura of this place prevents you from teleporting!";

    {
        name = "Wand of Teleportation";
    }

    @Override
    protected void onZap(int cell) {

        Char ch = Actor.findChar(cell);

        if (ch != null) {
            teleportChar(ch);
        } else {
            GLog.i("nothing happened");
        }

        if (ch == curUser) {
            setKnown();
        }

//            int count = 10;
//            int pos;
//            do {
//                pos = Dungeon.level.randomRespawnCell();
//                if (count-- <= 0) {
//                    break;
//                }
//            } while (pos == -1);
//
//            if (pos == -1) {
//
//                GLog.w(ScrollOfTeleportation.TXT_NO_TELEPORT);
//
//            } else {
//
//                ch.pos = pos;
//                ch.sprite.place(ch.pos);
//                ch.sprite.visible = Dungeon.visible[pos];
//                GLog.i(curUser.name + " teleported " + ch.name + " to somewhere");
//
//            }


    }

    protected void fx(int cell, Callback callback) {
        MagicMissile.coldLight(curUser.sprite.parent, curUser.pos, cell, callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    public static void teleportChar(Char ch) {

        // randomRespawnCell will give a valid open space unless overloaded by boss level to return -1
        int pos = Dungeon.level.randomRespawnCell();

        if (pos == -1) {
            GLog.w(TXT_NO_TELEPORT);
        } else {
            WandOfBlink.appear(ch, pos);
            Dungeon.level.press(pos, ch);

            if (ch instanceof Hero) {
                Dungeon.observe();
                GLog.i(TXT_TELEPORTED);
            } else {
                //ch.pos = pos;
                //ch.sprite.place(ch.pos);
                ch.sprite.visible = Dungeon.visible[pos];
                GLog.i(curUser.name + " teleported " + ch.name + " to somewhere");
            }
        }

    }

    @Override
    public String desc() {
        return
                "A blast from this wand will teleport a creature against " +
                        "its will to a random place on the current level.";
    }
}
