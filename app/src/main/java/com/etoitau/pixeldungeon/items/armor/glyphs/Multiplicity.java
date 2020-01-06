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
package com.etoitau.pixeldungeon.items.armor.glyphs;

import java.util.List;

import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.mobs.npcs.MirrorImage;
import com.etoitau.pixeldungeon.items.armor.Armor;
import com.etoitau.pixeldungeon.items.armor.Armor.Glyph;
import com.etoitau.pixeldungeon.items.wands.WandOfBlink;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.ItemSprite;
import com.etoitau.pixeldungeon.sprites.ItemSprite.Glowing;
import com.watabau.utils.Random;

/**
 * Chance to spawn mirror images, but increase damage to defender
 */
public class Multiplicity extends Glyph {

    private static final String TXT_MULTIPLICITY = "%s of multiplicity";

    private static ItemSprite.Glowing PINK = new ItemSprite.Glowing(0xCCAA88);

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.effectiveLevel());

        if (Random.Int(level / 2 + 6) >= 5) {

            List<Integer> respawnPoints = Level.aroundCell(defender.pos, 1, Level.NEIGHBOURS8, true);

            if (respawnPoints.size() > 0) {
                MirrorImage mob = new MirrorImage();
                mob.duplicate((Hero) defender);
                GameScene.add(mob);
                WandOfBlink.appear(mob, respawnPoints.get(0));

                defender.damage(Random.IntRange(1, defender.HT / 6), this);
                checkOwner(defender);
            }

        }

        return damage;
    }

    @Override
    public String name(String weaponName) {
        return String.format(TXT_MULTIPLICITY, weaponName);
    }

    @Override
    public Glowing glowing() {
        return PINK;
    }
}
