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
package com.etoitau.pixeldungeon.actors.mobs;

import java.util.HashSet;
import java.util.List;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.ResultDescriptions;
import com.etoitau.pixeldungeon.Statistics;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.blobs.Blob;
import com.etoitau.pixeldungeon.actors.blobs.Fire;
import com.etoitau.pixeldungeon.actors.blobs.ToxicGas;
import com.etoitau.pixeldungeon.actors.buffs.Amok;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Burning;
import com.etoitau.pixeldungeon.actors.buffs.Charm;
import com.etoitau.pixeldungeon.actors.buffs.Invisibility;
import com.etoitau.pixeldungeon.actors.buffs.Ooze;
import com.etoitau.pixeldungeon.actors.buffs.Poison;
import com.etoitau.pixeldungeon.actors.buffs.Sleep;
import com.etoitau.pixeldungeon.actors.buffs.Terror;
import com.etoitau.pixeldungeon.actors.buffs.Vertigo;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.effects.Pushing;
import com.etoitau.pixeldungeon.effects.particles.ShadowParticle;
import com.etoitau.pixeldungeon.items.keys.SkeletonKey;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.etoitau.pixeldungeon.items.wands.WandOfBlink;
import com.etoitau.pixeldungeon.items.wands.WandOfTeleportation;
import com.etoitau.pixeldungeon.items.weapon.enchantments.Death;
import com.etoitau.pixeldungeon.levels.HallsBossLevel;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.mechanics.Ballistica;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.BurningFistSprite;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.etoitau.pixeldungeon.sprites.LarvaSprite;
import com.etoitau.pixeldungeon.sprites.RottingFistSprite;
import com.etoitau.pixeldungeon.sprites.YogSprite;
import com.etoitau.pixeldungeon.utils.GLog;
import com.etoitau.pixeldungeon.utils.Utils;
import com.watabau.utils.Bundle;
import com.watabau.utils.Random;

public class Yog extends Mob {

    {
        name = Dungeon.depth == Statistics.deepestFloor ? "Yog-Dzewa" : "echo of Yog-Dzewa";
        spriteClass = YogSprite.class;

        HT = 300;

        EXP = 50;

        state = PASSIVE;

        HT *= Dungeon.currentDifficulty.mobHPModifier();
        HP = HT;
    }

    private static final String TXT_DESC =
            "Yog-Dzewa is an Old God, a powerful entity from the realms of chaos. A century ago, the ancient dwarves " +
                    "barely won the war against its army of demons, but were unable to kill the god itself. Instead, they then " +
                    "imprisoned it in the halls below their city, believing it to be too weak to rise ever again.";

    private static int fistsCount = 0;

    public Yog() {
        super();
    }

    @Override
    protected boolean act() {
        super.act();

        boolean perceive = Random.Float() > 0.5;
        boolean spawn = Random.Float() > 0.85;


        if (perceive) {
            if (Dungeon.hero.invisible > 0) {
                yell("You cannot hide. I see all", false);
                enemySeen = true;


                Buff buff = Dungeon.hero.buff(Invisibility.class);
                if (buff != null) {
                    buff.detach();
                }
            }

            for (Mob mob : Dungeon.level.mobs) {
                if (mob instanceof YogFist || mob instanceof Larva) {
                    mob.target = Dungeon.hero.pos;
                }
            }
        }

        if (spawn) {
            spawnLarva();
        }

        return true;
    }

    public void spawnFists() {
        RottingFist fist1 = new RottingFist();
        BurningFist fist2 = new BurningFist();

        List<Integer> candidates = Level.aroundCell(pos, 2, Level.NEIGHBOURS8, true);

        if (candidates.size() > 0) {
            fist1.pos = candidates.get(0);
            if (candidates.size() > 1) {
                fist2.pos = candidates.get(1);
            } else {
                fist2.pos = pos - 1;
            }
        } else {
            fist1.pos = pos + 1;
            fist2.pos = pos - 1;
        }

        GameScene.add(fist1);
        GameScene.add(fist2);
    }

    private void spawnLarva() {
        List<Integer> spawnPoints = Level.aroundCell(pos, 1, Level.NEIGHBOURS8, true);

        if (spawnPoints.size() > 0) {
            Larva larva = new Larva();
            larva.pos = spawnPoints.get(0);

            GameScene.add(larva);
            Actor.addDelayed(new Pushing(larva, pos, larva.pos), -1);
        }
    }


    private void banishHero() {
        // send hero away
        Hero hero = Dungeon.hero;
        int cell;
        // how far is hero from Yog now
        int prevDistance = Level.distance(pos, hero.pos);

        // get random points until one is farther than current
        do {
            cell = ((HallsBossLevel)Dungeon.level).randomRespawnCellYog();
        } while (Level.distance(pos, cell) < prevDistance);

        // send hero there
        teleportHero(cell);

        yell("Begone, pest.", false);

    }

    private boolean summonHero() {
        int tempPos = Level.closeToCell(pos);
        if (tempPos == -1) {
            // if somehow nowhere valid to move hero to,
            // don't teleport, but let calling func think it did
            return true;
        }
        int closeDist = Level.distance(pos, tempPos);
        int currentDist = Level.distance(pos, Dungeon.hero.pos);
        if (currentDist > 1 && closeDist < currentDist) {
            teleportHero(tempPos);
            yell("Come, coward.", false);
            return true;
        } else {
            return false;
        }
    }

    private void teleportHero(int cell) {
        Hero hero = Dungeon.hero;
        // send hero there
        WandOfBlink.appear(hero, cell);
        Dungeon.level.press(pos, hero);
        Dungeon.observe();
    }

    @Override
    public void damage(int dmg, Object src) {
        int beforeHP = HP;

        if (fistsCount > 0) {
            // damage greatly reduced if fists are still in play
            dmg >>= fistsCount;
        }

        super.damage(dmg, src);

        // if cross a threshold of health, teleport hero
        if ((HP < HT * 0.667 && beforeHP >= HT * 0.667) ||
                (HP < HT * 0.333 && beforeHP >= HT * 0.333)) {
            if (!summonHero()) {
                banishHero();
            }
        }
    }

    @Override
    public int defenseProc(Char enemy, int damage) {

        spawnLarva();

        return super.defenseProc(enemy, damage);
    }

    @Override
    public void beckon(int cell) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void die(Object cause) {

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof YogFist) {
                mob.die(cause);
            }
        }

        GameScene.bossSlain();
        Dungeon.level.drop(new SkeletonKey(), pos).sprite.drop();
        super.die(cause);

        yell("...");
    }

    @Override
    public void notice() {
        super.notice();
        yell("Hope is an illusion...");
    }

    @Override
    public String description() {
        return TXT_DESC;

    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {

        IMMUNITIES.add(Death.class);
        IMMUNITIES.add(Terror.class);
        IMMUNITIES.add(Amok.class);
        IMMUNITIES.add(Charm.class);
        IMMUNITIES.add(Sleep.class);
        IMMUNITIES.add(Burning.class);
        IMMUNITIES.add(ToxicGas.class);
        IMMUNITIES.add(ScrollOfPsionicBlast.class);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    public static class YogFist extends Mob {

        public boolean canSwap;
        private static final String KEY_SWAP = "canSwap";

        public YogFist() {
            super();
            fistsCount++;
        }

        @Override
        public boolean act() {
            canSwap = true;
            return super.act();
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            fistsCount--;
        }

        @Override
        public int attackSkill(Char target) {
            return 36;
        }

        @Override
        public int dr() {
            return 15;
        }

        private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

        static {
            RESISTANCES.add(ToxicGas.class);
            RESISTANCES.add(Death.class);
            RESISTANCES.add(ScrollOfPsionicBlast.class);
        }

        @Override
        public HashSet<Class<?>> resistances() {
            return RESISTANCES;
        }

        private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

        static {
            IMMUNITIES.add(Amok.class);
            IMMUNITIES.add(Sleep.class);
            IMMUNITIES.add(Terror.class);
        }

        @Override
        public HashSet<Class<?>> immunities() {
            return IMMUNITIES;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(KEY_SWAP, canSwap);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            if (bundle.contains(KEY_SWAP)) {
                canSwap = bundle.getBoolean(KEY_SWAP);
            } else {
                canSwap = true;
            }
        }
    }


    public static class RottingFist extends YogFist {

        private static final int REGENERATION = 4;

        {
            name = "rotting fist";
            spriteClass = RottingFistSprite.class;

            HP = HT = 300;
            defenseSkill = 25;

            EXP = 0;

            state = WANDERING;
        }

        static {
            IMMUNITIES.add(Poison.class);
            IMMUNITIES.add(Vertigo.class);
        }


        @Override
        public int damageRoll() {
            return Random.NormalIntRange(24, 36);
        }


        @Override
        public int attackProc(Char enemy, int damage) {
            if (Random.Int(3) == 0) {
                Buff.affect(enemy, Ooze.class);
                enemy.sprite.burst(0xFF000000, 5);
            }

            return damage;
        }

        @Override
        public boolean act() {
            if (Level.water[pos] && HP < HT) {
                sprite.emitter().burst(ShadowParticle.UP, 2);
                HP += REGENERATION;
            }

            return super.act();
        }

        @Override
        public String description() {
            return TXT_DESC;
        }
    }

    public static class BurningFist extends YogFist {

        {
            name = "burning fist";
            spriteClass = BurningFistSprite.class;

            HP = HT = 200;
            defenseSkill = 25;

            EXP = 0;

            state = WANDERING;
        }

        static {
            IMMUNITIES.add(Burning.class);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(20, 32);
        }

        @Override
        protected boolean canAttack(Char enemy) {
            return Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
        }

        @Override
        public boolean attack(Char enemy) {

            if (!Level.adjacent(pos, enemy.pos)) {
                spend(attackDelay());

                if (hit(this, enemy, true)) {

                    int dmg = damageRoll();
                    enemy.damage(dmg, this);

                    enemy.sprite.bloodBurstA(sprite.center(), dmg);
                    enemy.sprite.flash();

                    if (!enemy.isAlive() && enemy == Dungeon.hero) {
                        Dungeon.fail(Utils.format(ResultDescriptions.BOSS, name, Dungeon.depth));
                        GLog.n(TXT_KILL, name);
                    }
                    return true;

                } else {

                    enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
                    return false;
                }
            } else {
                return super.attack(enemy);
            }
        }

        @Override
        public boolean act() {

            for (int cell: Level.aroundNine(pos)) {
                GameScene.add(Blob.seed(cell, 2, Fire.class));
            }

            return super.act();
        }

        @Override
        public String description() {
            return TXT_DESC;

        }

    }

    public static class Larva extends Mob {

        {
            name = "god's larva";
            spriteClass = LarvaSprite.class;

            HP = HT = 25;
            defenseSkill = 20;

            EXP = 0;

            state = HUNTING;
        }

        @Override
        public int attackSkill(Char target) {
            return 30;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(15, 20);
        }

        @Override
        public int dr() {
            return 8;
        }

        @Override
        public String description() {
            return TXT_DESC;
        }

        @Override
        public boolean act() {
            if (Level.adjacent(pos, Dungeon.hero.pos)) {
                for (int cell: Level.aroundEight(pos)) {
                    Char ch = Actor.findChar(cell);
                    if (ch instanceof YogFist) {
                        if (!Level.adjacent(ch.pos, Dungeon.hero.pos)) {
                            if (swapWith((YogFist) ch)) {
                                break;
                            }
                        }
                    }
                }
            }
            return super.act();
        }

        private boolean swapWith(YogFist fist) {
            if (!fist.canSwap) {
                return false;
            }
            fist.canSwap = false;

            int larvaePos = pos;

            moveSprite(pos, fist.pos);
            move(fist.pos);

            fist.sprite.move(fist.pos, larvaePos);
            fist.move(larvaePos);

            this.spend(1 / this.speed());

            return true;
        }

    }
}
