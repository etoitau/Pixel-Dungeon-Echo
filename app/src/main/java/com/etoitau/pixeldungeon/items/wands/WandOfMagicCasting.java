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
package com.etoitau.pixeldungeon.items.wands;

import com.watabau.noosa.audio.Sample;
import com.watabau.noosa.tweeners.AlphaTweener;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.Char;
import com.etoitau.pixeldungeon.actors.buffs.Blindness;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Invisibility;
import com.etoitau.pixeldungeon.actors.buffs.Poison;
import com.etoitau.pixeldungeon.actors.hero.Legend;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.actors.mobs.npcs.NPC;
import com.etoitau.pixeldungeon.actors.mobs.npcs.SummonedPet;
import com.etoitau.pixeldungeon.effects.MagicMissile;
import com.etoitau.pixeldungeon.effects.Pushing;
import com.etoitau.pixeldungeon.effects.Speck;
import com.etoitau.pixeldungeon.effects.particles.ShadowParticle;
import com.etoitau.pixeldungeon.items.weapon.missiles.NinjaBomb;
import com.etoitau.pixeldungeon.mechanics.Ballistica;
import com.etoitau.pixeldungeon.scenes.CellSelector;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.scenes.MissionScene;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.etoitau.pixeldungeon.sprites.WraithSprite;
import com.etoitau.pixeldungeon.ui.QuickSlot;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Callback;
import com.watabau.utils.Random;

public class WandOfMagicCasting extends Wand {

    {
        name = "Wand of Hax";
    }

    public void castSpell(CAST_TYPES casting) {
        this.casting = casting;
        MissionScene.selectCell(zapper);
    }


    public void castSpellCost() {
        switch (casting) {
            case DARK_BOLT:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.passiveB2.getManaCost();
                Dungeon.hero.heroSkills.passiveB2.castTextYell();
                break;
            case DOMINANCE:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.passiveB3.getManaCost();
                Dungeon.hero.heroSkills.passiveB3.castTextYell();
                break;
            case SOUL_SPARK:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.active2.getManaCost();
                Dungeon.hero.heroSkills.active2.castTextYell();
                break;
            case SPARK:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.active2.getManaCost();
                Dungeon.hero.heroSkills.active2.castTextYell();
                break;
            case NINJA_BOMB:
                Dungeon.hero.MP -= Dungeon.hero.heroSkills.active2.getManaCost();
                Dungeon.hero.heroSkills.active2.castTextYell();
                break;
        }
    }

    public enum CAST_TYPES {DARK_BOLT, DOMINANCE, SOUL_SPARK, SPARK, NINJA_BOMB}

    ;
    public CAST_TYPES casting = CAST_TYPES.DARK_BOLT;

    protected static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                curUser = Dungeon.hero;
                final int cell = Ballistica.cast(curUser.pos, target, true, true);
                curUser.sprite.zap(cell);


                final Wand curWand = Legend.haxWand;

                ((WandOfMagicCasting) curWand).castSpellCost();
                curUser.busy();

                curWand.fx(cell, new Callback() {
                    @Override
                    public void call() {
                        curWand.onZap(cell);
                        curWand.wandUsed();
                    }
                });

                Invisibility.dispel();


            }
        }

        @Override
        public String prompt() {
            return "Choose direction to cast";
        }
    };

    @Override
    protected void onZap(int cell) {
        Char ch = Actor.findChar(cell);
        if (ch != null) {
            if (ch instanceof NPC && casting != CAST_TYPES.SOUL_SPARK)
                return;

            if (casting == CAST_TYPES.DARK_BOLT) {
                ch.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                Sample.INSTANCE.play(Assets.SND_CURSED);
                SummonedPet minion = new SummonedPet(WraithSprite.class);
                minion.name = "Consumed Soul";
                minion.screams = false;
                minion.HT = ch.HT;
                minion.HP = minion.HT;
                minion.defenseSkill = 5;
                minion.pos = cell;
                GameScene.add(minion);
                minion.sprite.alpha(0);
                minion.sprite.parent.add(new AlphaTweener(minion.sprite, 1, 0.15f));

                ch.die(null);
            } else if (casting == CAST_TYPES.DOMINANCE) {
                ch.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                Sample.INSTANCE.play(Assets.SND_CURSED);
                SummonedPet minion = new SummonedPet(ch.sprite.getClass());
                minion.name = "Enslaved " + ch.name;
                minion.screams = false;
                minion.HT = ch.HT;
                minion.HP = minion.HT;
                minion.defenseSkill = ch.defenseSkill(Dungeon.hero);
                minion.pos = cell;
                GameScene.add(minion);
                minion.sprite.alpha(0);
                minion.sprite.parent.add(new AlphaTweener(minion.sprite, 1, 0.15f));
                ch.sprite.visible = false;
                ch.die(null);
            } else if (casting == CAST_TYPES.SOUL_SPARK) {
                ch.HP = ch.HT;
                ch.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
            } else if (casting == CAST_TYPES.SPARK) {
                ch.damage(Random.Int(Dungeon.hero.heroSkills.active2.level + Dungeon.hero.lvl / (6 - Dungeon.hero.heroSkills.active2.level), Dungeon.hero.heroSkills.active2.level * 3 + Dungeon.hero.lvl / (5 - Dungeon.hero.heroSkills.active2.level)), Dungeon.hero);
                if (Random.Int(Dungeon.hero.heroSkills.active2.level) > 1) {
                    Buff.prolong(ch, Blindness.class, Random.Int(1, 2));
                    ch.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 4);
                    ch.sprite.showStatus(CharSprite.WARNING, "Blinded!");
                }
            }

        } else {

            GLog.i("nothing happened");

        }
    }

    protected void fx(int cell, Callback callback) {
        if (casting == CAST_TYPES.DARK_BOLT)
            MagicMissile.shadow(curUser.sprite.parent, curUser.pos, cell, callback, 1);
        else if (casting == CAST_TYPES.DOMINANCE)
            MagicMissile.shadow(curUser.sprite.parent, curUser.pos, cell, callback, 3);
        else if (casting == CAST_TYPES.SOUL_SPARK)
            MagicMissile.whiteLight(curUser.sprite.parent, curUser.pos, cell, callback);
        else if (casting == CAST_TYPES.SPARK)
            MagicMissile.blueLight(curUser.sprite.parent, curUser.pos, cell, callback);

        if (casting != CAST_TYPES.NINJA_BOMB) {
            Sample.INSTANCE.play(Assets.SND_ZAP);
        }

        if (casting == CAST_TYPES.NINJA_BOMB) {
            NinjaBomb nb = new NinjaBomb();
            nb.strength = Dungeon.hero.heroSkills.active2.level;
            nb.cast(curUser, cell);
        }
    }

    @Override
    public String desc() {
        return
                "The vile blast of this twisted bit of wood will imbue its target " +
                        "with a deadly venom. A creature that is poisoned will suffer periodic " +
                        "damage until the effect ends. The duration of the effect increases " +
                        "with the level of the staff.";
    }
}
