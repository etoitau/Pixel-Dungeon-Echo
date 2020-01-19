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
package com.etoitau.pixeldungeon.effects;

import android.opengl.GLES20;

import com.etoitau.pixeldungeon.sprites.CharSprite;

import javax.microedition.khronos.opengles.GL10;

public class ChampHalo extends Halo {

    private CharSprite target;

    public static final int RED = 0xb70202;
    public static final int YELLOW = 0xe4ff00;
    public static final int WHITE = 0xffffff;
    public static final int BLACK = 0xcccccc;


    public ChampHalo(CharSprite sprite, int color) {
        super(20, color, 0.25f);
        target = sprite;
        visible = target.visible; // only show halo if champ is visible
        scale.set(radius / RADIUS);
        am = brightness;
    }


    @Override
    public void update() {
        super.update();

        visible = target.visible; // only show halo if champ is visible

        point(target.x + target.width / 2, target.y + target.height / 2);
    }

    @Override
    public void draw() {
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
        super.draw();
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void putOut() {
        killAndErase();
    }
}
