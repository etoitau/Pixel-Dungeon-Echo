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


import com.watabau.noosa.tweeners.AlphaTweener;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.mobs.npcs.SummonedPet;
import com.etoitau.pixeldungeon.effects.Pushing;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.ui.StatusPane;
import com.watabau.utils.Random;

import java.util.ArrayList;
import java.util.List;


public class SummonCrab extends ActiveSkill2 {


    {
        name = "Summon Crab";
        castText = "Fight for me!";
        tier = 2;
        image = 42;
        mana = 3;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<String>();
        if (level > 0 && hero.MP >= getManaCost())
            actions.add(AC_SUMMON);
        return actions;
    }

    @Override
    public boolean execute(Hero hero, String action) {
        if (action == Skill.AC_SUMMON) {
            boolean spawned = false;
            for (int nu = 0; nu < 1; nu++) {
                int newPos = hero.pos;
                if (Actor.findChar(newPos) != null) {
                    List<Integer> candidates = Level.aroundCell(hero.pos, 1, Level.NEIGHBOURS4, true);
                    newPos = candidates.size() > 0 ? candidates.get(0) : -1;
                    if (newPos != -1) {
                        spawned = true;
                        SummonedPet crab = new SummonedPet(SummonedPet.PET_TYPES.CRAB);
                        crab.spawn(level);
                        crab.pos = newPos;
                        GameScene.add(crab);
                        Actor.addDelayed(new Pushing(crab, hero.pos, newPos), -1);
                        crab.sprite.alpha(0);
                        crab.sprite.parent.add(new AlphaTweener(crab.sprite, 1, 0.15f));
                    }
                }
            }

            if (spawned) {
                hero.MP -= getManaCost();
                StatusPane.manaDropping += getManaCost();
                castTextYell();
                hero.spend(TIME_TO_USE);
                hero.busy();
                hero.sprite.operate(hero.pos);
            }
            Dungeon.hero.heroSkills.lastUsed = this;
        }
        return true;
    }

    @Override
    public int getManaCost() {
        return (int) Math.ceil(mana * (1 + 0.55 * level));
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Summons Crabs for your service.\n"
                + costUpgradeInfo();
    }

}
