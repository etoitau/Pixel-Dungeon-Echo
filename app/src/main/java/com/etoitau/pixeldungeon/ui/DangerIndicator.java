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
package com.etoitau.pixeldungeon.ui;

import com.watabau.noosa.BitmapText;
import com.watabau.noosa.Camera;
import com.watabau.noosa.Image;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.scenes.PixelScene;

/**
 * Shows a red button in upper right of ui when enemies are in sight.
 * Clicking it centers camera on each in turn and shows their HealthIndicator
 */
public class DangerIndicator extends Tag {

    public static final int COLOR = 0xFF4C4C;

    private BitmapText number;
    private Image icon;

    private int enemyIndex = 0;

    private int lastNumber = -1;

    public DangerIndicator() {
        super(0xFF4C4C);

        setSize(24, 16);

        visible = false;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        number = new BitmapText(PixelScene.font1x);
        add(number);

        icon = Icons.SKULL.get();
        add(icon);
    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = right() - 10;
        icon.y = y + (height - icon.height) / 2;

        placeNumber();
    }

    private void placeNumber() {
        number.x = right() - 11 - number.width();
        number.y = PixelScene.align(y + (height - number.baseLine()) / 2);
    }

    @Override
    public void update() {

        if (Dungeon.hero.isAlive()) {
            int v = Dungeon.hero.visibleEnemies();
            if (v != lastNumber) {
                lastNumber = v;
                if (visible = lastNumber > 0) {
                    number.text(Integer.toString(lastNumber));
                    number.measure();
                    placeNumber();

                    flash();
                }
            }
        } else {
            visible = false;
        }

        super.update();
    }

    @Override
    protected void onClick() {

        // next mob
        Mob target = Dungeon.hero.visibleEnemy(enemyIndex++);

        // show health
        HealthIndicatorManager.instance.target(target, HealthIndicatorManager.PEEK_DURATION);

        // move camera to target
        Camera.main.target = null;
        Camera.main.focusOn(target.sprite);
    }
}
