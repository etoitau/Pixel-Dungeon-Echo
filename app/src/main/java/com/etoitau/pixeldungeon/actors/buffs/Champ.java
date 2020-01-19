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
package com.etoitau.pixeldungeon.actors.buffs;

import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.effects.ChampHalo;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.watabau.utils.Bundle;
import com.watabau.utils.Random;

public class Champ extends Buff {

    private static final String TYPE = "type";
    private static final String BONUS_APPLIED = "bonusApplied";

    public static final int CHAMP_CHIEF = 1;
    public static final int CHAMP_CURSED = 2;
    public static final int CHAMP_FOUL = 3;
    public static final int CHAMP_VAMPERIC = 4;

    private Integer[] types = new Integer[]{CHAMP_CHIEF, CHAMP_CURSED, CHAMP_FOUL, CHAMP_VAMPERIC};

    public int type = Random.element(types);

    private boolean bonusApplied = false;


    @Override
    public boolean attachTo(Char target) {
        boolean toReturn = super.attachTo(target);

        if (!bonusApplied) {
            permanentChanges();
        }

        toRestore();

        return toReturn;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TYPE, type);
        bundle.put(BONUS_APPLIED, bonusApplied);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        type = bundle.getInt(TYPE);
        bonusApplied = bundle.getBoolean(BONUS_APPLIED);
    }

    @Override
    public boolean act() {

        spend(TICK);

        if (!target.isAlive()) {
            detach();
        }
        return true;
    }

    private void permanentChanges() {
        // only make these changes once, they'll be saved in mob's bundle
        bonusApplied = true;

        switch (type) {
            case CHAMP_VAMPERIC:
            case CHAMP_CURSED:
            case CHAMP_FOUL:
                this.target.HT *= 1.5;
                this.target.HP = this.target.HT;
                break;
            case CHAMP_CHIEF:
                this.target.HT *= 2;
                this.target.HP = this.target.HT;
                break;
        }
    }

    // called by Mob.sprite()
    public void spriteChanges(CharSprite sprite) {
        // add halo to sprite
        if (sprite.champHalo == null) {
            switch (type) {
                case CHAMP_VAMPERIC: //red
                    sprite.champHalo = new ChampHalo(sprite, ChampHalo.RED);
                    break;
                case CHAMP_CHIEF: //white
                    sprite.champHalo = new ChampHalo(sprite, ChampHalo.WHITE);
                    break;
                case CHAMP_CURSED: //black
                    sprite.champHalo = new ChampHalo(sprite, ChampHalo.BLACK);
                    break;
                case CHAMP_FOUL: //yellow
                    sprite.champHalo = new ChampHalo(sprite, ChampHalo.YELLOW);
                    break;
            }
        }
        sprite.add(CharSprite.State.CHAMP);
    }

    private void toRestore() {
        // these are not stored in mob's bundle, restore them
        this.target.champ = type;

        switch (type) {
            case CHAMP_VAMPERIC: //red
                this.target.name = "Vampiric " + this.target.name;
                ((Mob) this.target).defenseSkill *= 1.1;
                break;
            case CHAMP_CHIEF: //white
                this.target.name = "Chief " + this.target.name;
                ((Mob) this.target).defenseSkill *= 1.3;
                break;
            case CHAMP_CURSED: //black
                this.target.name = "Cursed " + this.target.name;
                ((Mob) this.target).defenseSkill *= 1.15;
                break;
            case CHAMP_FOUL: //yellow
                this.target.name = "Foul " + this.target.name;
                ((Mob) this.target).defenseSkill *= 1.2;
                break;
        }
    }
}
