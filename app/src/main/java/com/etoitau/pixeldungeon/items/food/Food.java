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
package com.etoitau.pixeldungeon.items.food;

import java.util.ArrayList;

import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.mobs.npcs.RatKing;
import com.etoitau.pixeldungeon.scenes.CellSelector;
import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Badges;
import com.etoitau.pixeldungeon.Statistics;
import com.etoitau.pixeldungeon.actors.buffs.Hunger;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.effects.Speck;
import com.etoitau.pixeldungeon.effects.SpellSprite;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfRecharging;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.utils.GLog;

public class Food extends Item {

    private static final float TIME_TO_EAT = 3f;

    public static final String AC_EAT = "EAT";

    public float energy = Hunger.HUNGRY; // 260
    public String message = "That food tasted delicious!";

    {
        stackable = true;
        name = "ration of food";
        image = ItemSpriteSheet.RATION;

        thrower = new CellSelector.Listener() {
            @Override
            public void onSelect(Integer target) {
                if (target != null) {
                    Char thrownAt = Actor.findChar(target);
                    if (thrownAt instanceof RatKing) {
                        ((RatKing) thrownAt).feed((Food) curItem);
                    } else {
                        curItem.cast(curUser, target);
                    }
                }
            }

            @Override
            public String prompt() {
                return "Choose direction of throw";
            }
        };
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_EAT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_EAT)) {

            detach(hero.belongings.backpack);

            ((Hunger) hero.buff(Hunger.class)).satisfy(energy);
            GLog.i(message);

            switch (hero.heroClass) {
                case WARRIOR:
                    if (hero.HP < hero.HT) {
                        hero.HP = Math.min(hero.HP + 5, hero.HT);
                        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    }
                    break;
                case MAGE:
                    hero.belongings.charge(false);
                    ScrollOfRecharging.charge(hero);
                    break;
                case ROGUE:
                case HUNTRESS:
                    break;
            }

            hero.sprite.operate(hero.pos);
            hero.busy();
            SpellSprite.show(hero, SpellSprite.FOOD);
            Sample.INSTANCE.play(Assets.SND_EAT);

            hero.spend(TIME_TO_EAT);

            Statistics.foodEaten++;
            Badges.validateFoodEaten();

        } else {

            super.execute(hero, action);

        }
    }

    @Override
    public String info() {
        return
                "Nothing fancy here: dried meat, " +
                        "some biscuits - things like that.";
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
        return 10 * quantity;
    }
}
