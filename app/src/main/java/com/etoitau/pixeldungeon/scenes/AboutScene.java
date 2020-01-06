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
package com.etoitau.pixeldungeon.scenes;

import android.content.Intent;
import android.net.Uri;

import com.watabau.input.Touchscreen.Touch;
import com.watabau.noosa.BitmapTextMultiline;
import com.watabau.noosa.Camera;
import com.watabau.noosa.Game;
import com.watabau.noosa.Image;
import com.watabau.noosa.TouchArea;
import com.etoitau.pixeldungeon.PixelDungeon;
import com.etoitau.pixeldungeon.effects.Flare;
import com.etoitau.pixeldungeon.ui.Archs;
import com.etoitau.pixeldungeon.ui.ExitButton;
import com.etoitau.pixeldungeon.ui.Icons;
import com.etoitau.pixeldungeon.ui.Window;

public class AboutScene extends PixelScene {

    private static final String TXT_TITLE = "Pixel Dungeon Echo";
    private static final String TXT_BY = "by: Kyle Chatman";
    private static final String TXT_ECHO_ON_GITHUB = "Open source on GitHub:";
    private static final String LNK_ECHO = "github.com/etoitau/Pixel-Dungeon-Echo";

    private static final String TXT_BASED_ON_VANILLA = "Based on Pixel Dungeon";
    private static final String TXT_BY_VANILLA = "by: Watabou";
    private static final String LNK_VANILLA = "github.com/watabou/pixel-dungeon";
    private static final String LNK_WATABOU = "pixeldungeon.watabou.ru";

    private static final String TXT_SPD_CONTENT_1 = "With content from";
    private static final String TXT_SPD_CONTENT_2 = "Skillful Pixel Dungeon";
    private static final String TXT_BY_SPD = "by: BilbolDev";
    private static final String LNK_SPD = "github.com/bilbolPrime/SPD";

    private static final String TXT_MUSIC = "Music: Cube_Code";


    private static float GAP = 2;
    private static float TITLE_SIZE = 9;
    private static float TXT_SIZE = 7;
    private static int MAX_WIDTH = 120;

    private float pos = GAP * 2; // tracking where next element should go

    @Override
    public void create() {
        super.create();

        // title
        addText(TXT_TITLE, TITLE_SIZE, MAX_WIDTH, 1, true);

        // by Kyle
        addText(TXT_BY, TXT_SIZE, MAX_WIDTH, 1, false);

        // src available
        addText(TXT_ECHO_ON_GITHUB, TXT_SIZE, MAX_WIDTH, 1, false);

        // echo src link
        addLink("https://", LNK_ECHO, TXT_SIZE, MAX_WIDTH, 4);


        // Pixel Dungeon
        // based on...
        addText(TXT_BASED_ON_VANILLA, TXT_SIZE, MAX_WIDTH, 1, false);

        // by Watabou
        addText(TXT_BY_VANILLA, TXT_SIZE, MAX_WIDTH, 1, false);

        // vanilla src link
        addLink("https://", LNK_VANILLA, TXT_SIZE, MAX_WIDTH, 1);

        // watbou page link
        addLink("http://", LNK_WATABOU, TXT_SIZE, MAX_WIDTH, 1);

        // music by
        addText(TXT_MUSIC, TXT_SIZE, MAX_WIDTH, 4, false);

        // SPD
        // with content from SPD
        addText(TXT_SPD_CONTENT_1, TXT_SIZE, MAX_WIDTH, 1, false);
        addText(TXT_SPD_CONTENT_2, TXT_SIZE, MAX_WIDTH, 1, false);

        // by bilboldev
        addText(TXT_BY_SPD, TXT_SIZE, MAX_WIDTH, 1, false);

        // link SPD
        addLink("https://", LNK_SPD, TXT_SIZE, MAX_WIDTH, 2);

        // wata image
        Image wata = Icons.WATA.get();
        wata.x = align((Camera.main.width - wata.width) / 2);
        wata.y = pos + wata.height;
        add(wata);

        new Flare(7, 64).color(0x112233, true).show(wata, 0).angularSpeed = +20;

        // background
        Archs archs = new Archs();
        archs.setSize(Camera.main.width, Camera.main.height);
        addToBack(archs);

        // exit button
        ExitButton btnExit = new ExitButton();
        btnExit.setPos(Camera.main.width - btnExit.width(), 0);
        add(btnExit);

        fadeIn();
    }

    private void addText(String text, float size, int maxWidth, int gaps, boolean isTitle) {
        BitmapTextMultiline line = createMultiline(text, size);
        if (isTitle)
            line.hardlight(Window.TITLE_COLOR);
        line.maxWidth = Math.min(Camera.main.width, maxWidth);
        line.measure();
        add(line);

        line.x = align((Camera.main.width - line.width()) / 2);
        line.y = pos;

        pos = line.y + line.height() + GAP * gaps;
    }

    private void addLink(final String prefix, final String url, float size, int maxWidth, int gaps) {
        BitmapTextMultiline link = createMultiline(url, size);
        link.maxWidth = Math.min(Camera.main.width, maxWidth);
        link.measure();
        link.hardlight(Window.TITLE_COLOR);
        add(link);

        TouchArea hotAreaLink = new TouchArea(link) {
            @Override
            protected void onClick(Touch touch) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(prefix + url));
                Game.instance.startActivity(intent);
            }
        };
        add(hotAreaLink);

        link.x = align((Camera.main.width - link.width()) / 2);
        link.y = pos;

        pos = link.y + link.height() + GAP * gaps;
    }

    @Override
    protected void onBackPressed() {
        PixelDungeon.switchNoFade(TitleScene.class);
    }
}
