/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019-2020 Kyle Chatman
 *
 * Based on:
 *
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * With content from:
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
package com.etoitau.pixeldungeon.ui;

import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.items.potions.Potion;
import com.etoitau.pixeldungeon.items.rings.Ring;
import com.etoitau.pixeldungeon.items.scrolls.Scroll;
import com.etoitau.pixeldungeon.items.wands.Wand;
import com.watabau.noosa.BitmapText;
import com.watabau.noosa.Image;
import com.watabau.noosa.ui.Button;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.armor.Armor;
import com.etoitau.pixeldungeon.items.weapon.Weapon;
import com.etoitau.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.etoitau.pixeldungeon.scenes.PixelScene;
import com.etoitau.pixeldungeon.sprites.ItemSprite;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.utils.Utils;

public class ItemSlot extends Button {

    public static final int DEGRADED = 0xFF4444;
    public static final int UPGRADED = 0x44FF44;
    public static final int WARNING = 0xFF8800;

    private static final float ENABLED = 1.0f;
    private static final float DISABLED = 0.3f;

    protected ItemSprite icon;
    protected BitmapText topLeft; // quantity
    protected BitmapText topRight; // strength req
    protected BitmapText bottomRight; // upgrade level
    protected Image bottomRightIcon; // consumable type
    protected boolean bottomRightIconVisible = true; // consumable icon visible

    private static final String TXT_STRENGTH = ":%d";
    private static final String TXT_TYPICAL_STR = "%d?";

    private static final String TXT_LEVEL = "%+d";
    private static final String TXT_CURSED = "";//"-";

    // Special "virtual items"
    public static final Item CHEST = new Item() {
        public int image() {
            return ItemSpriteSheet.CHEST;
        }

        ;
    };
    public static final Item LOCKED_CHEST = new Item() {
        public int image() {
            return ItemSpriteSheet.LOCKED_CHEST;
        }

        ;
    };
    public static final Item TOMB = new Item() {
        public int image() {
            return ItemSpriteSheet.TOMB;
        }

        ;
    };
    public static final Item SKELETON = new Item() {
        public int image() {
            return ItemSpriteSheet.BONES;
        }

        ;
    };

    public ItemSlot() {
        super();
    }

    public ItemSlot(Item item) {
        this();
        item(item);
    }

    @Override
    protected void createChildren() {

        super.createChildren();

        icon = new ItemSprite();
        add(icon);

        topLeft = new BitmapText(PixelScene.font1x);
        add(topLeft);

        topRight = new BitmapText(PixelScene.font1x);
        add(topRight);

        bottomRight = new BitmapText(PixelScene.font1x);
        add(bottomRight);
    }

    @Override
    protected void layout() {
        super.layout();

        icon.x = x + (width - icon.width) / 2;
        icon.y = y + (height - icon.height) / 2;

        if (topLeft != null) {
            topLeft.x = x;
            topLeft.y = y;
        }

        if (topRight != null) {
            topRight.x = x + (width - topRight.width());
            topRight.y = y;
        }

        if (bottomRight != null) {
            bottomRight.x = x + (width - bottomRight.width());
            bottomRight.y = y + (height - bottomRight.height());

            if (bottomRightIcon != null) {
                // if both bottom right text and icon, shift text over
                bottomRight.x -= bottomRightIcon.width() + 2;
            }
        }

        if (bottomRightIcon != null) {
            bottomRightIcon.x = x + (width - bottomRightIcon.width()) -1;
            bottomRightIcon.y = y + (height - bottomRightIcon.height());
        }
    }

    public void item(Item item) {
        if (bottomRightIcon != null){
            remove(bottomRightIcon);
            bottomRightIcon = null;
        }

        if (item == null) {
            active = false;
            icon.visible = topLeft.visible = topRight.visible = bottomRight.visible = false;
            return;

        } else {

            active = true;
            icon.visible = topLeft.visible = topRight.visible = bottomRight.visible = true;
        }

        icon.view(item.image(), item.glowing());

        topLeft.text(item.status());

        boolean isArmor = item instanceof Armor;
        boolean isWeapon = item instanceof Weapon;
        boolean isScroll = item instanceof Scroll;
        boolean isPotion = item instanceof Potion;
        boolean isRing = item instanceof Ring;
        boolean isWand = item instanceof Wand;

        if (isArmor || isWeapon) {

            if (item.levelKnown || (isWeapon && !(item instanceof MeleeWeapon))) {

                int str = isArmor ? ((Armor) item).STR : ((Weapon) item).STR();
                topRight.text(Utils.format(TXT_STRENGTH, str));
                if (str > Dungeon.hero.STR()) {
                    topRight.hardlight(DEGRADED);
                } else {
                    topRight.resetColor();
                }

            } else {

                topRight.text(Utils.format(TXT_TYPICAL_STR, isArmor ?
                        ((Armor) item).typicalSTR() :
                        ((MeleeWeapon) item).typicalSTR()));
                topRight.hardlight(WARNING);

            }
            topRight.measure();

        } else {

            topRight.text(null);

        }

        int level = item.visiblyUpgraded();

        if (level != 0 || (item.cursed && item.cursedKnown)) {
            bottomRight.text(item.levelKnown ? Utils.format(TXT_LEVEL, level) : TXT_CURSED);
            bottomRight.measure();
            bottomRight.hardlight(level > 0 ? (item.isBroken() ? WARNING : UPGRADED) : DEGRADED);
        } else {
            bottomRight.text(null);
        }

        if (item.isIdentified() || (isWand && ((Wand) item).isKnown())) {
            Integer iconInt = null, top = null;

            if (isPotion) {
                iconInt = Potion.iconKey.get(item.getClass());
                top = 0;
                bottomRight.text(null);
            } else if (isScroll) {
                iconInt = Scroll.iconKey.get(item.getClass());
                top = 8;
                bottomRight.text(null);
            } else if (isWand) {
                iconInt = Wand.iconKey.get(item.getClass());
                top = 16;
            } else if (isRing) {
                iconInt = Ring.iconKey.get(item.getClass());
                top = 24;
            }

            if (iconInt != null && bottomRightIconVisible) {
                bottomRightIcon = new Image(Assets.SUB_ICONS);
                bottomRightIcon.frame(iconInt * 7, top, 7, 8);
                add(bottomRightIcon);
            }
        } else {
            bottomRight.text(null);
        }

        layout();

    }

    public void enable(boolean value) {

        active = value;

        float alpha = value ? ENABLED : DISABLED;
        icon.alpha(alpha);
        topLeft.alpha(alpha);
        topRight.alpha(alpha);
        bottomRight.alpha(alpha);
        if (bottomRightIcon != null) bottomRightIcon.alpha( alpha );
    }

    public void showParams(boolean value) {
        if (value) {
            add(topRight);
            add(bottomRight);
        } else {
            remove(topRight);
            remove(bottomRight);
        }
        bottomRightIconVisible = value;
    }
}
