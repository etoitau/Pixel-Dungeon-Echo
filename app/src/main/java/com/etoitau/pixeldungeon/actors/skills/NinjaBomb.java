/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
 *
 * Based on:
 *
 * Work by Moussa 2017 for Skillful Pixel Dungeon
 *
 * a mod of :
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
import com.etoitau.pixeldungeon.items.wands.WandOfMagicCasting;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */

// used by rogue
public class NinjaBomb extends ActiveSkill2 {

    {
        name = "Ninja Bomb";
        castText = "Go to sleep";
        tier = 2;
        image = 65;
        mana = 8;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<String>();
        if (level > 0 && hero.MP >= getManaCost())
            actions.add(AC_CAST);
        return actions;
    }


    @Override
    public boolean execute(Hero hero, String action) {
        if (action == Skill.AC_CAST && hero.MP >= getManaCost()) {
            Hero.haxWand.castSpell(WandOfMagicCasting.CAST_TYPES.NINJA_BOMB);
            Dungeon.hero.heroSkills.lastUsed = this;
        }
        return true;
    }

    @Override
    public int getManaCost() {
        return (int) Math.ceil(mana * (1 + 0.5 * level));
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Throws a bomb that emits sleeping gas on impact.\n"
                + costUpgradeInfo();
    }
}
