/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
 *
 * Based on:
 *
 * Skillful Pixel Dungeon
 * Copyright (C) 2017 Moussa
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

package com.etoitau.pixeldungeon.actors.skills;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.particles.ElmoParticle;
import com.etoitau.pixeldungeon.items.weapon.missiles.Arrow;
import com.etoitau.pixeldungeon.ui.StatusPane;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class AimedShot extends ActiveSkill1 {


    boolean cast;

    {
        name = "Aimed Shot";
        castText = "I see him";
        image = 93;
        tier = 1;
        mana = 3;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(Skill.AC_ACTIVATE)) {
            hero.heroSkills.active2.active = false; // Disable Double shot
            hero.heroSkills.active3.active = false; // Disable Bombvoyage
        }
    }

    @Override
    public float rangedDamageModifier() {
        float toReturn = 1f;
        toReturn += cast ? 0.2f * level : 0;
        cast = false;
        return toReturn;
    }

    @Override
    public boolean aimedShot() {
        if (!active || Dungeon.hero.MP < getManaCost()) {
            cast = false;
            return false;
        }

        cast = true;

        castTextYell();
        Dungeon.hero.MP -= getManaCost();
        StatusPane.manaDropping += getManaCost();
        return true;
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Stronger ranged attack that never misses.\n"
                + costUpgradeInfo();
    }
}
