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
package com.etoitau.pixeldungeon.windows;

import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabau.noosa.BitmapTextMultiline;
import com.watabau.noosa.Game;
import com.etoitau.pixeldungeon.Rankings;
import com.etoitau.pixeldungeon.Statistics;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.items.Ankh;
import com.etoitau.pixeldungeon.scenes.InterlevelScene;
import com.etoitau.pixeldungeon.scenes.PixelScene;
import com.etoitau.pixeldungeon.sprites.ItemSprite;
import com.etoitau.pixeldungeon.ui.RedButton;
import com.etoitau.pixeldungeon.ui.Window;

public class WndResurrect extends Window {

    private static final String TXT_CONTINUE_TITLE = "Continue?";
    private static final String TXT_ANKH_MESSAGE = "You died, but you were given another chance " +
            "to win this dungeon. Will you take it?";
    private static final String TXT_CONTINUE_MESSAGE = "You died, but your quest isn't over yet. " +
            "Will you continue?";
    private static final String TXT_YES = "Yes, I will fight!";
    private static final String TXT_NO = "No, I give up";

    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 20;
    private static final float GAP = 2;

    public static WndResurrect instance;
    public static Object causeOfDeath;

    // if not resurrected by ankh (i.e. permadeath turned off) ankh will be null
    public WndResurrect(final Ankh ankh, Object causeOfDeath) {
        super();

        instance = this;
        WndResurrect.causeOfDeath = causeOfDeath;

        // set up titlebar
        IconTitle titlebar = new IconTitle();
        if (ankh == null) {
            titlebar.icon(new ItemSprite(ItemSpriteSheet.BONES, null));
            titlebar.label(TXT_CONTINUE_TITLE);
        } else {
            titlebar.icon(new ItemSprite(ankh.image(), null));
            titlebar.label(ankh.name());
        }
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        BitmapTextMultiline message = (ankh == null) ?
                PixelScene.createMultiline(TXT_CONTINUE_MESSAGE, 6):
                PixelScene.createMultiline(TXT_ANKH_MESSAGE, 6);

        message.maxWidth = WIDTH;
        message.measure();
        message.y = titlebar.bottom() + GAP;
        add(message);

        RedButton btnYes = new RedButton(TXT_YES) {
            @Override
            protected void onClick() {
                hide();

                if (ankh != null)
                    Statistics.ankhsUsed++;

                InterlevelScene.mode = InterlevelScene.Mode.RESURRECT;
                Game.switchScene(InterlevelScene.class);
            }
        };
        btnYes.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
        add(btnYes);

        RedButton btnNo = new RedButton(TXT_NO) {
            @Override
            protected void onClick() {
                hide();

                Rankings.INSTANCE.submit(false);
                Hero.reallyDie(WndResurrect.causeOfDeath);
            }
        };
        btnNo.setRect(0, btnYes.bottom() + GAP, WIDTH, BTN_HEIGHT);
        add(btnNo);

        resize(WIDTH, (int) btnNo.bottom());
    }

    @Override
    public void destroy() {
        super.destroy();
        instance = null;
    }

    @Override
    public void onBackPressed() {
    }
}
