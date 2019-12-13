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

import com.watabau.gltextures.TextureCache;
import com.watabau.noosa.Gizmo;
import com.watabau.noosa.Image;
import com.watabau.noosa.ui.Component;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.sprites.CharSprite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HealthIndicatorManager extends Component {

    public static HealthIndicatorManager instance;

    private Map<Char, HealthIndicator> indicators = new HashMap<>();

    public final static float PEEK_DURATION = 1f;
    public final static float DEFAULT_DURATION = 4f;

    public HealthIndicatorManager() {
        super();

        instance = this;
    }


    public void target(Char ch) {
        target(ch, DEFAULT_DURATION);
    }

    public void target(Char ch, float duration) {
        if (ch == null || !ch.isAlive()) { return; }

        HealthIndicator hi = indicators.get(ch);
        if (hi == null) {
            hi = new HealthIndicator();
            indicators.put(ch, hi);
        }

        // add to Gizmo group
        add(hi);

        // set target
        hi.target(ch, duration);
    }

    public void remove(Char ch) {
        // set spot in members to null in gizmo group
        erase(indicators.get(ch));
        // remove from local tracking
        indicators.remove(ch);
    }
}
