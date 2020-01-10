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
package com.etoitau.pixeldungeon.actors.mobs.npcs;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.buffs.Buff;

import com.etoitau.pixeldungeon.items.food.Food;
import com.etoitau.pixeldungeon.items.food.Pasty;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.etoitau.pixeldungeon.sprites.RatKingSprite;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Bundle;
import com.watabau.utils.Random;

public class RatKing extends NPC {

    private final String KEY_FED = "fed";
    private boolean fed = false;

    private final String TXT_FAVORITE = "My favorite!";
    private final String TXT_APPROVES = "It's Majesty, the Rat King approves of your tribute and " +
            "taught you a new skill";
    private final String TXT_POOR = "Poor fare for a king...";
    private final String TXT_ATE = "Rat King ate your food.";
    private final String TXT_NOT_SLEEPING = "I'm not sleeping!";
    private final String TXT_MORE = "Does my loyal subject have another offering?";
    private final String TXT_NONSENSE = "What is it? I have no time for this nonsense. " +
            "My kingdom won't rule itself!";
    private final String TXT_HINT = "Such impudence! Not even bearing gifts!";
    private final String TXT_DESCRIPTION = "This rat is a little bigger than a regular marsupial " +
            "rat and it's wearing a tiny crown on its head.";

    {
        name = "rat king";
        spriteClass = RatKingSprite.class;

        state = SLEEPING;
    }

    public void feed(Food food) {
        sprite.turnTo(pos, Dungeon.hero.pos);

        if (food instanceof Pasty) {
            this.sprite.showStatus(CharSprite.NEUTRAL, TXT_FAVORITE);

            if (!fed) {
                fed = true;
                GLog.p(TXT_APPROVES);
                Dungeon.hero.heroSkills.unlockSkill();
                state = FOLLOWING;
            }
        } else {
            this.sprite.showStatus(CharSprite.NEUTRAL, TXT_POOR);
            GLog.w(TXT_ATE);
            state = WANDERING;
        }

    }

    @Override
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    @Override
    public float speed() {
        return fed? 1f: 2f;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public void add(Buff buff) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public void interact() {
        sprite.turnTo(pos, Dungeon.hero.pos);
        if (state == SLEEPING) {
            notice();
            yell(TXT_NOT_SLEEPING);
            state = WANDERING;
        } else if (fed) {
            yell(TXT_MORE, true);
        } else if (Random.Float() > 0.2) {
            yell(TXT_NONSENSE);
        } else {
            yell(TXT_HINT);
        }
    }

    @Override
    public String description() {
        return TXT_DESCRIPTION;
    }

    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);

        bundle.put(KEY_FED, fed);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {

        super.restoreFromBundle(bundle);

        if (bundle.contains(KEY_FED)) {
            fed = bundle.getBoolean(KEY_FED);
        } else {
            fed = false;
        }

    }

    @Override
    protected AiState defaultAwakeState() {
        return FOLLOWING;
    }
}
