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
package com.etoitau.pixeldungeon.ui;

import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.watabau.gltextures.TextureCache;
import com.watabau.noosa.Image;
import com.watabau.noosa.ui.Component;

public class HealthIndicator extends Component {

    private static final float HEIGHT = 2;

    private Char target;

    private Image bg;
    private Image level;

    private HealthIndicatorTimer timer = new HealthIndicatorTimer();

    @Override
    protected void createChildren() {
        bg = new Image(TextureCache.createSolid(0xFFcc0000));
        bg.scale.y = HEIGHT;
        add(bg);

        level = new Image(TextureCache.createSolid(0xFF00cc00));
        level.scale.y = HEIGHT;
        add(level);
    }

    @Override
    public void update() {
        super.update();

        // if target is gone, clean up
        if (target == null || !target.isAlive()) {
            HealthIndicatorManager.instance.remove(target);
            Actor.remove(timer);
            visible = false;
            return;
        }

        // if timer is up, remove it and hide indicator
        if (!timer.hasTime) {
            Actor.remove(timer);
            visible = false;
            return;
        }

        // if hero can see target, and indicator has time, show it
        if (target.sprite.visible) {
            CharSprite sprite = target.sprite;
            bg.scale.x = sprite.width;
            level.scale.x = sprite.width * target.HP / target.HT;
            bg.x = level.x = sprite.x;
            bg.y = level.y = sprite.y - HEIGHT - 1;

            visible = true;
        } else {
            visible = false;
        }
    }

    public void target(Char ch, float duration) {
        if (ch == null || !ch.isAlive()) { return; }

        target = ch;

        if (!timer.hasTime) {
            Actor.remove(timer);
            timer = new HealthIndicatorTimer();
            Actor.add(timer);
        }

        timer.setTimer(duration);

    }
}

