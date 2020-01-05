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
package com.etoitau.pixeldungeon.items.scrolls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.etoitau.pixeldungeon.Badges;
import com.etoitau.pixeldungeon.actors.buffs.Blindness;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.ItemStatusHandler;
import com.etoitau.pixeldungeon.items.potions.PotionOfExperience;
import com.etoitau.pixeldungeon.items.potions.PotionOfFrost;
import com.etoitau.pixeldungeon.items.potions.PotionOfHealing;
import com.etoitau.pixeldungeon.items.potions.PotionOfInvisibility;
import com.etoitau.pixeldungeon.items.potions.PotionOfLevitation;
import com.etoitau.pixeldungeon.items.potions.PotionOfLiquidFlame;
import com.etoitau.pixeldungeon.items.potions.PotionOfMana;
import com.etoitau.pixeldungeon.items.potions.PotionOfMight;
import com.etoitau.pixeldungeon.items.potions.PotionOfMindVision;
import com.etoitau.pixeldungeon.items.potions.PotionOfParalyticGas;
import com.etoitau.pixeldungeon.items.potions.PotionOfPurity;
import com.etoitau.pixeldungeon.items.potions.PotionOfStrength;
import com.etoitau.pixeldungeon.items.potions.PotionOfToxicGas;
import com.etoitau.pixeldungeon.sprites.HeroSprite;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.utils.GLog;
import com.watabau.utils.Bundle;

public abstract class Scroll extends Item {

    private static final String TXT_BLINDED = "You can't read a scroll while blinded";

    public static final String AC_READ = "READ";

    protected static final float TIME_TO_READ = 1f;

    private static final Class<?>[] scrolls = {
            ScrollOfIdentify.class,
            ScrollOfMagicMapping.class,
            ScrollOfRecharging.class,
            ScrollOfRemoveCurse.class,
            ScrollOfTeleportation.class,
            ScrollOfChallenge.class,
            ScrollOfTerror.class,
            ScrollOfLullaby.class,
            ScrollOfPsionicBlast.class,
            ScrollOfMirrorImage.class,
            ScrollOfUpgrade.class,
            ScrollOfEnchantment.class,
            ScrollOfHome.class,
            ScrollOfSacrifice.class,
            ScrollOfBloodyRitual.class,
            ScrollOfSkill.class,
            ScrollOfFrostLevel.class,
            ScrollOfWipeOut.class

    };
    private static final String[] runes =
            {"KAUNAN", "SOWILO", "LAGUZ", "YNGVI", "GYFU", "RAIDO", "ISAZ", "MANNAZ",
                    "NAUDIZ", "BERKANAN", "ODAL", "TIWAZ", "", "", "", "", "", ""};// 12 labels + 6
    private static final Integer[] images = {
            ItemSpriteSheet.SCROLL_KAUNAN,
            ItemSpriteSheet.SCROLL_SOWILO,
            ItemSpriteSheet.SCROLL_LAGUZ,
            ItemSpriteSheet.SCROLL_YNGVI,
            ItemSpriteSheet.SCROLL_GYFU,
            ItemSpriteSheet.SCROLL_RAIDO,
            ItemSpriteSheet.SCROLL_ISAZ,
            ItemSpriteSheet.SCROLL_MANNAZ,
            ItemSpriteSheet.SCROLL_NAUDIZ,
            ItemSpriteSheet.SCROLL_BERKANAN,
            ItemSpriteSheet.SCROLL_ODAL,
            ItemSpriteSheet.SCROLL_TIWAZ, // 12
            ItemSpriteSheet.SCROLL_GOHOME,
            ItemSpriteSheet.SCROLL_SACRIFICE,
            ItemSpriteSheet.SCROLL_BLOODY,
            ItemSpriteSheet.SCROLL_SKILLPOINT,
            ItemSpriteSheet.SCROLL_FROST,
            ItemSpriteSheet.SCROLL_WIPE_OUT // + 6
    };

    public static final HashMap<Class<?>, Integer> iconKey = new HashMap<Class<?>, Integer>() {
        {
            put(ScrollOfIdentify.class, 0);
            put(ScrollOfLullaby.class, 1);
            put(ScrollOfMagicMapping.class, 2);
            put(ScrollOfMirrorImage.class, 3);
            put(ScrollOfPsionicBlast.class, 4);
            put(ScrollOfChallenge.class, 5);
            put(ScrollOfRecharging.class, 6);
            put(ScrollOfRemoveCurse.class, 7);
            put(ScrollOfTeleportation.class, 8);
            put(ScrollOfTerror.class, 9);
            // 10 not used
            put(ScrollOfUpgrade.class, 11);
            put(ScrollOfEnchantment.class, 12);
        }
    };

    private static ItemStatusHandler<Scroll> handler;

    private String rune;

    {
        stackable = true;
        defaultAction = AC_READ;
    }

    @SuppressWarnings("unchecked")
    public static void initLabels() {
        // exclude the explicitly labeled scrolls
        handler = new ItemStatusHandler<Scroll>((Class<? extends Scroll>[]) scrolls, runes, images, 6);
    }

    public static void save(Bundle bundle) {
        handler.save(bundle);
    }

    @SuppressWarnings("unchecked")
    public static void restore(Bundle bundle) {
        handler = new ItemStatusHandler<Scroll>((Class<? extends Scroll>[]) scrolls, runes, images, bundle);
    }

    public Scroll() {
        super();
        image = handler.image(this);
        rune = handler.label(this);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_READ)) {

            if (hero.buff(Blindness.class) != null) {
                GLog.w(TXT_BLINDED);
            } else {
                curUser = hero;
                curItem = detach(hero.belongings.backpack);
                doRead();
            }

        } else {

            super.execute(hero, action);

        }
    }

    abstract protected void doRead();

    protected void readAnimation() {
        curUser.spend(TIME_TO_READ);
        curUser.busy();
        ((HeroSprite) curUser.sprite).read();
    }

    public boolean isKnown() {
        return handler.isKnown(this);
    }

    public void setKnown() {
        if (!isKnown()) {
            handler.know(this);
        }

        Badges.validateAllScrollsIdentified();
    }

    @Override
    public Item identify() {
        setKnown();
        return super.identify();
    }

    @Override
    public String name() {
        return isKnown() ? name : "scroll \"" + rune + "\"";
    }

    @Override
    public String info() {
        return isKnown() ?
                desc() :
                "This parchment is covered with indecipherable writing, and bears a title " +
                        "of rune " + rune + ". Who knows what it will do when read aloud?";
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return isKnown();
    }

    public static HashSet<Class<? extends Scroll>> getKnown() {
        return handler.known();
    }

    public static HashSet<Class<? extends Scroll>> getUnknown() {
        return handler.unknown();
    }

    public static boolean allKnown() {
        return handler.known().size() == scrolls.length;
    }

    @Override
    public int price() {
        return 15 * quantity;
    }
}
