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
package com.etoitau.pixeldungeon.items;

import java.util.ArrayList;
import java.util.List;

import com.watabau.noosa.audio.Sample;
import com.watabau.noosa.tweeners.AlphaTweener;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.mobs.npcs.Bee;
import com.etoitau.pixeldungeon.effects.Pushing;
import com.etoitau.pixeldungeon.effects.Splash;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabau.utils.Random;

public class Honeypot extends Item {

    public static final String AC_SHATTER = "SHATTER";

    {
        name = "honeypot";
        image = ItemSpriteSheet.HONEYPOT;
        defaultAction = AC_THROW;
        stackable = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SHATTER);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action) {
        if (action.equals(AC_SHATTER)) {

            hero.sprite.zap(hero.pos);
            shatter(hero.pos);

            detach(hero.belongings.backpack);
            hero.spendAndNext(TIME_TO_THROW);

        } else {
            super.execute(hero, action);
        }
    }

    @Override
    protected void onThrow(int cell) {
        if (Level.pit[cell]) {
            super.onThrow(cell);
        } else {
            shatter(cell);
        }
    }

    private void shatter(int pos) {
        Sample.INSTANCE.play(Assets.SND_SHATTER);

        if (Dungeon.visible[pos]) {
            Splash.at(pos, 0xffd500, 5);
        }

        int newPos = pos;
        if (Actor.findChar(pos) != null) {
            List<Integer> candidates = Level.aroundCell(pos, 1, Level.NEIGHBOURS4, true);
            newPos = candidates.size() > 0 ? candidates.get(0) : -1;
        }

        if (newPos != -1) {
            Bee bee = new Bee();
            bee.spawn(Dungeon.depth);
            bee.HP = bee.HT;
            bee.pos = newPos;

            GameScene.add(bee);
            Actor.addDelayed(new Pushing(bee, pos, newPos), -1);

            bee.sprite.alpha(0);
            bee.sprite.parent.add(new AlphaTweener(bee.sprite, 1, 0.15f));

            Sample.INSTANCE.play(Assets.SND_BEE);
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int price() {
        return 50 * quantity;
    }

    @Override
    public String info() {
        return
                "There is not much honey in this small honeypot, but there is a golden bee there and it doesn't want to leave it.";
    }
}
