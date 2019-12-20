/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
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
package com.etoitau.pixeldungeon.items.weapon.melee;

import com.watabau.noosa.tweeners.AlphaTweener;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.mobs.npcs.Skeleton;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.Pushing;
import com.etoitau.pixeldungeon.effects.Speck;
import com.etoitau.pixeldungeon.effects.particles.ShadowParticle;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Bundle;
import com.watabau.utils.Random;

import java.util.ArrayList;

public class NecroBlade extends MeleeWeapon {

    public static final String AC_HEAL = "Heal";
    public static final String AC_SUMMON = "Summon";
    public static final String AC_UPGRADE = "Upgrade";
    private static final int MAX_CHARGE = 100,
            CHARGE_HEAL = MAX_CHARGE * 25 / 100,
            CHARGE_SUMMON = MAX_CHARGE * 55 / 100,
            CHARGE_UPGRADE = MAX_CHARGE;


    {
        name = "necroblade";
        image = ItemSpriteSheet.NecroBlade5;
    }

    public int charge = MAX_CHARGE;
    private static final int CHARGE_PER_DAMAGE = 25;
    private static final int CHARGE_PER_HT = 15;

    public NecroBlade() {
        super(1, 0.7f, 1f);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (charge >= CHARGE_HEAL)
            actions.add(AC_HEAL);
        if (charge >= CHARGE_SUMMON)
            actions.add(AC_SUMMON);
        if (charge >= CHARGE_UPGRADE)
            actions.add(AC_UPGRADE);
        return actions;
    }


    @Override
    public void execute(Hero hero, String action) {
        switch (action) {
            case AC_HEAL:
                hero.HP += hero.HT * 0.35;
                if (hero.HP > hero.HT)
                    hero.HP = hero.HT;
                GLog.p("NecroBlade heals " + ((int) (hero.HT * 0.35)) + " HP");
                updateCharge(-CHARGE_HEAL);

                CellEmitter.center(hero.pos).burst(Speck.factory(Speck.HEALING), 1);
                break;

            case AC_SUMMON:
                int newPos = hero.pos;
                if (Actor.findChar(newPos) != null) {
                    ArrayList<Integer> candidates = new ArrayList<Integer>();
                    boolean[] passable = Level.passable;

                    for (int n : Level.NEIGHBOURS4) {
                        int c = hero.pos + n;
                        if (passable[c] && Actor.findChar(c) == null) {
                            candidates.add(c);
                        }
                    }
                    newPos = candidates.size() > 0 ? Random.element(candidates) : -1;
                }
                if (newPos != -1) {
                    updateCharge(-CHARGE_SUMMON);
                    Skeleton skel = new Skeleton();
                    int skelLevel = this.level() > 1 ? 1 + this.level() : 1;
                    if (skelLevel > 7) ;
                    skelLevel = 7;
                    skel.spawn(skelLevel);
                    skel.HP = skel.HT;
                    skel.pos = newPos;

                    GameScene.add(skel);
                    Actor.addDelayed(new Pushing(skel, hero.pos, newPos), -1);

                    skel.sprite.alpha(0);
                    skel.sprite.parent.add(new AlphaTweener(skel.sprite, 1, 0.15f));
                    CellEmitter.center(newPos).burst(ShadowParticle.UP, Random.IntRange(3, 5));
                    GLog.p("NecroBlade summoned a skeleton");
                } else {
                    GLog.i("No place to summon");
                }
                break;

            case AC_UPGRADE:
                updateCharge(-CHARGE_UPGRADE);
                this.upgrade(1);
                GLog.p("NecroBlade consumed the souls within. It looks much better now.");
                break;

            default:
                super.execute(hero, action);

                break;
        }
    }

    @Override
    public int damageRoll(Hero hero) {
        int damage = super.damageRoll(hero);
        damage += Random.Int(damageChargeBonus());
        return damage;
    }

    // return current max damage boost due to charge level
    private int damageChargeBonus() {
        return charge / CHARGE_PER_DAMAGE;
    }


    private void updateCharge(int chargeDelta) {
        charge += chargeDelta;
        if (charge > MAX_CHARGE)
            charge = MAX_CHARGE;
        else if (charge < 0)
            charge = 0;
        updateImage();
    }

    // called in Char.attack when this does mortal blow
    public boolean absorbSoul(int enemyHT) {
        boolean updated = false;
        if (charge < MAX_CHARGE) {
            int addCharge = enemyHT > CHARGE_PER_HT ? (int) Math.floor(enemyHT / NecroBlade.CHARGE_PER_HT) : 1;
            updateCharge(addCharge);
            updated = true;
        }

        return updated;
    }

    private void updateImage() {
        switch ((int) Math.floor(charge / (MAX_CHARGE / 5))) {
            case 0:
                image = ItemSpriteSheet.NecroBlade0;
                break;
            case 1:
                image = ItemSpriteSheet.NecroBlade1;
                break;
            case 2:
                image = ItemSpriteSheet.NecroBlade2;
                break;
            case 3:
                image = ItemSpriteSheet.NecroBlade3;
                break;
            case 4:
                image = ItemSpriteSheet.NecroBlade4;
                break;
            case 5:
                image = ItemSpriteSheet.NecroBlade5;
                break;
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt("CHARGE");
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("CHARGE", charge);
    }

    @Override
    public String desc() {
        return "A blade forged from dark magic. NecroBlades consume the souls of those who perish by them. The more they consume, the stronger they become.\n" +
                "NecroBlade energy at " + charge + "/" + MAX_CHARGE + "\n"
                + "The energy stored within increases damage by 0 - " + damageChargeBonus() + ".";
    }
}
