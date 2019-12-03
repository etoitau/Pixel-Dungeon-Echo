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
    private static final String TXT_ECHO_ON_GITHUB = "Source code is available on GitHub";
    private static final String LNK_ECHO = "github.com/etoitau/Pixel-Dungeon-Echo";

    private static final String TXT_BASED_ON_VANILLA = "Based on Pixel Dungeon";
    private static final String TXT_BY_VANILLA = "by: Watabou";
    private static final String LNK_VANILLA = "github.com/watabou/pixel-dungeon";
    private static final String LNK_WATABOU = "pixeldungeon.watabou.ru";

    private static final String TXT_SPD_CONTENT = "With content from Skillful Pixel Dungeon";
    private static final String TXT_BY_SPD = "by: BilbolDev";
    private static final String LNK_SPD = "github.com/bilbolPrime/SPD";

    private static final String TXT_MUSIC = "Music: Cube_Code";


    private static float GAP = 2;

    @Override
    public void create() {
        super.create();

        // wata image
        Image wata = Icons.WATA.get();

        // where should next item be
        float pos = GAP * 4;

        // title
        BitmapTextMultiline textTitle = createMultiline(TXT_TITLE, 8);
        textTitle.hardlight(Window.TITLE_COLOR);
        textTitle.maxWidth = Math.min(Camera.main.width, 120);
        textTitle.measure();
        add(textTitle);

        textTitle.x = align((Camera.main.width - textTitle.width()) / 2);
        textTitle.y = pos;

        pos = textTitle.y + textTitle.height() + GAP;

        // by Kyle
        BitmapTextMultiline textEchoBy = createMultiline(TXT_BY, 8);
        textEchoBy.maxWidth = Math.min(Camera.main.width, 120);
        textEchoBy.measure();
        add(textEchoBy);

        textEchoBy.x = align((Camera.main.width - textEchoBy.width()) / 2);
        textEchoBy.y = pos;

        pos = textEchoBy.y + textEchoBy.height() + GAP;

        // src available
        BitmapTextMultiline textEchoSrc = createMultiline(TXT_ECHO_ON_GITHUB, 8);
        textEchoSrc.maxWidth = Math.min(Camera.main.width, 120);
        textEchoSrc.measure();
        add(textEchoSrc);

        textEchoSrc.x = align((Camera.main.width - textEchoSrc.width()) / 2);
        textEchoSrc.y = pos;

        pos = textEchoSrc.y + textEchoSrc.height() + GAP;

        // echo src link
        BitmapTextMultiline linkEcho = createMultiline(LNK_ECHO, 8);
        linkEcho.maxWidth = Math.min(Camera.main.width, 120);
        linkEcho.measure();
        linkEcho.hardlight(Window.TITLE_COLOR);
        add(linkEcho);

        TouchArea hotAreaEcho = new TouchArea(linkEcho) {
            @Override
            protected void onClick(Touch touch) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + LNK_ECHO));
                Game.instance.startActivity(intent);
            }
        };
        add(hotAreaEcho);

        linkEcho.x = align((Camera.main.width - linkEcho.width()) / 2);
        linkEcho.y = pos;

        pos = linkEcho.y + linkEcho.height() + GAP * 4;


        // Pixel Dungeon
        // based on...
        BitmapTextMultiline basedOnVanilla = createMultiline(TXT_BASED_ON_VANILLA, 8);
        basedOnVanilla.maxWidth = Math.min(Camera.main.width, 120);
        basedOnVanilla.measure();
        add(basedOnVanilla);

        basedOnVanilla.x = align((Camera.main.width - basedOnVanilla.width()) / 2);
        basedOnVanilla.y = pos;

        pos = basedOnVanilla.y + basedOnVanilla.height() + GAP;

        // by Watabou
        BitmapTextMultiline byWatabou = createMultiline(TXT_BY_VANILLA, 8);
        byWatabou.maxWidth = Math.min(Camera.main.width, 120);
        byWatabou.measure();
        add(byWatabou);

        byWatabou.x = align((Camera.main.width - byWatabou.width()) / 2);
        byWatabou.y = pos;

        pos = byWatabou.y + byWatabou.height() + GAP;

        // vanilla src link
        BitmapTextMultiline linkVanilla = createMultiline(LNK_VANILLA, 8);
        linkVanilla.maxWidth = Math.min(Camera.main.width, 120);
        linkVanilla.measure();
        linkVanilla.hardlight(Window.TITLE_COLOR);
        add(linkVanilla);

        TouchArea hotAreaVanilla = new TouchArea(linkVanilla) {
            @Override
            protected void onClick(Touch touch) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + LNK_VANILLA));
                Game.instance.startActivity(intent);
            }
        };
        add(hotAreaVanilla);

        linkVanilla.x = align((Camera.main.width - linkVanilla.width()) / 2);
        linkVanilla.y = pos;

        pos = linkVanilla.y + linkVanilla.height() + GAP;

        // watbou page link
        BitmapTextMultiline linkWatabou = createMultiline(LNK_WATABOU, 8);
        linkWatabou.maxWidth = Math.min(Camera.main.width, 120);
        linkWatabou.measure();
        linkWatabou.hardlight(Window.TITLE_COLOR);
        add(linkWatabou);

        TouchArea hotAreaWatabou = new TouchArea(linkWatabou) {
            @Override
            protected void onClick(Touch touch) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + LNK_WATABOU));
                Game.instance.startActivity(intent);
            }
        };
        add(hotAreaWatabou);

        linkWatabou.x = align((Camera.main.width - linkWatabou.width()) / 2);
        linkWatabou.y = pos;

        pos = linkWatabou.y + linkWatabou.height() + GAP;

        BitmapTextMultiline music = createMultiline(TXT_MUSIC, 8);
        music.maxWidth = Math.min(Camera.main.width, 120);
        music.measure();
        add(music);

        music.x = align((Camera.main.width - music.width()) / 2);
        music.y = pos;

        pos = music.y + music.height() + GAP * 4;

        // SPD
        // with content from SPD
        BitmapTextMultiline contentSPD = createMultiline(TXT_SPD_CONTENT, 8);
        contentSPD.maxWidth = Math.min(Camera.main.width, 120);
        contentSPD.measure();
        add(contentSPD);

        contentSPD.x = align((Camera.main.width - contentSPD.width()) / 2);
        contentSPD.y = pos;

        pos = contentSPD.y + contentSPD.height() + GAP;

        // by bilboldev
        BitmapTextMultiline byBilboldev = createMultiline(TXT_BY_SPD, 8);
        byBilboldev.maxWidth = Math.min(Camera.main.width, 120);
        byBilboldev.measure();
        add(byBilboldev);

        byBilboldev.x = align((Camera.main.width - byBilboldev.width()) / 2);
        byBilboldev.y = pos;

        pos = byBilboldev.y + byBilboldev.height() + GAP;

        // link SPD
        BitmapTextMultiline linkSPD = createMultiline(LNK_SPD, 8);
        linkSPD.maxWidth = Math.min(Camera.main.width, 120);
        linkSPD.measure();
        linkSPD.hardlight(Window.TITLE_COLOR);
        add(linkSPD);

        linkSPD.x = align((Camera.main.width - linkSPD.width()) / 2);
        linkSPD.y = pos;

        TouchArea hotAreaSPD = new TouchArea(linkSPD) {
            @Override
            protected void onClick(Touch touch) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + LNK_SPD));
                Game.instance.startActivity(intent);
            }
        };
        add(hotAreaSPD);

        pos = linkSPD.y + linkSPD.height() + GAP * 4;

        // locate wata icon at end
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

    @Override
    protected void onBackPressed() {
        PixelDungeon.switchNoFade(TitleScene.class);
    }
}
