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
package com.etoitau.pixeldungeon.windows;

import android.graphics.RectF;

import com.watabau.gltextures.TextureCache;
import com.watabau.noosa.BitmapText;
import com.watabau.noosa.ColorBlock;
import com.watabau.noosa.Image;
import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.PixelDungeon;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.hero.Storage;
import com.etoitau.pixeldungeon.items.Gold;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.armor.Armor;
import com.etoitau.pixeldungeon.items.bags.Bag;
import com.etoitau.pixeldungeon.items.bags.Keyring;
import com.etoitau.pixeldungeon.items.bags.ScrollHolder;
import com.etoitau.pixeldungeon.items.bags.SeedPouch;
import com.etoitau.pixeldungeon.items.bags.WandHolster;
import com.etoitau.pixeldungeon.items.wands.Wand;
import com.etoitau.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.etoitau.pixeldungeon.items.weapon.missiles.Boomerang;
import com.etoitau.pixeldungeon.plants.Plant.Seed;
import com.etoitau.pixeldungeon.scenes.PixelScene;
import com.etoitau.pixeldungeon.ui.Icons;
import com.etoitau.pixeldungeon.ui.ItemSlot;
import com.etoitau.pixeldungeon.utils.Utils;

public class WndStorage extends WndTabbed {

    public enum Mode {
        ALL,
        UNIDENTIFED,
        UPGRADEABLE,
        QUICKSLOT,
        FOR_SALE,
        WEAPON,
        ARMOR,
        ENCHANTABLE,
        WAND,
        SEED
    }

    protected static final int COLS_P = 4;
    protected static final int COLS_L = 6;

    protected static final int SLOT_SIZE = 28;
    protected static final int SLOT_MARGIN = 1;

    protected static final int TITLE_HEIGHT = 12;

    private int nCols;
    private int nRows;

    protected int count;
    protected int col;
    protected int row;

    public boolean noDegrade = !PixelDungeon.itemDeg();

    public WndStorage(Storage bag) {

        super();

        nCols = PixelDungeon.landscape() ? COLS_L : COLS_P;
        nRows = (5) / nCols + ((5) % nCols > 0 ? 1 : 0);

        int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
        int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1);

        BitmapText txtTitle = PixelScene.createText(Utils.capitalize(bag.backpack.name()), 9);
        txtTitle.hardlight(TITLE_COLOR);
        txtTitle.measure();
        txtTitle.x = (int) (slotsWidth - txtTitle.width()) / 2;
        txtTitle.y = (int) (TITLE_HEIGHT - txtTitle.height()) / 2;
        add(txtTitle);

        placeItems(bag);

        resize(slotsWidth, slotsHeight + TITLE_HEIGHT);

    }


    protected void placeItems(Storage container) {


        boolean backpack = (container == Dungeon.hero.storage);
        if (!backpack) {
            count = nCols;
            col = 0;
            row = 1;
        }

        // Items in the bag
        for (Item item : container.backpack.items) {
            placeItem(item);
        }

        // Free space
        while (count - (backpack ? 0 : nCols) < 5) {
            placeItem(null);
        }

    }

    protected void placeItem(final Item item) {

        int x = col * (SLOT_SIZE + SLOT_MARGIN);
        int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN);

        add(new ItemButton(item).setPos(x, y));

        if (++col >= nCols) {
            col = 0;
            row++;
        }

        count++;
    }

    @Override
    public void onMenuPressed() {
        hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onClick(Tab tab) {
        hide();
    }

    @Override
    protected int tabHeight() {
        return 20;
    }

    private class ItemButton extends ItemSlot {

        private static final int NORMAL = 0xFF4A4D44;
        private static final int EQUIPPED = 0xFF63665B;

        private static final int NBARS = 3;

        private Item item;
        private ColorBlock bg;

        private ColorBlock durability[];

        public ItemButton(Item item) {

            super(item);

            this.item = item;
            if (item instanceof Gold) {
                bg.visible = false;
            }

            width = height = SLOT_SIZE;
        }

        @Override
        protected void createChildren() {
            bg = new ColorBlock(SLOT_SIZE, SLOT_SIZE, NORMAL);
            add(bg);

            super.createChildren();
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;

            if (noDegrade == true)
                durability = null; // no durability

            if (durability != null) {
                for (int i = 0; i < NBARS; i++) {
                    durability[i].x = x + 1 + i * 3;
                    durability[i].y = y + height - 3;
                }
            }

            super.layout();
        }

        @Override
        public void item(Item item) {

            super.item(item);
            if (item != null) {

                bg.texture(TextureCache.createSolid(item.isEquipped(Dungeon.hero) ? EQUIPPED : NORMAL));
                if (item.cursed && item.cursedKnown) {
                    bg.ra = +0.2f;
                    bg.ga = -0.1f;
                } else if (!item.isIdentified()) {
                    bg.ra = 0.1f;
                    bg.ba = 0.1f;
                }


                enable(item.name() != null);

            } else {
                bg.color(NORMAL);
            }
        }

        @Override
        protected void onTouchDown() {
            bg.brightness(1.5f);
            Sample.INSTANCE.play(Assets.SND_CLICK, 0.7f, 0.7f, 1.2f);
        }

        protected void onTouchUp() {
            bg.brightness(1.0f);
        }

        @Override
        protected void onClick() {
            WndStorage.this.add(new WndItemStorage(WndStorage.this, item));
        }

        @Override
        protected boolean onLongClick() {
            return false;
        }
    }

    public interface Listener {
        void onSelect(Item item);
    }
}
