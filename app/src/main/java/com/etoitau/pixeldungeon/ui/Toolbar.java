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
package com.etoitau.pixeldungeon.ui;

import com.etoitau.pixeldungeon.BuildConfig;
import com.etoitau.pixeldungeon.PixelDungeon;
import com.etoitau.pixeldungeon.scenes.InterlevelScene;
import com.watabau.noosa.Game;
import com.watabau.noosa.Gizmo;
import com.watabau.noosa.Image;
import com.watabau.noosa.ui.Button;
import com.watabau.noosa.ui.Component;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.DungeonTilemap;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.mobs.ColdGirl;
import com.etoitau.pixeldungeon.actors.mobs.Mob;
import com.etoitau.pixeldungeon.items.Heap;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.plants.Plant;
import com.etoitau.pixeldungeon.scenes.CellSelector;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.ItemSprite;
import com.etoitau.pixeldungeon.windows.WndCatalogus;
import com.etoitau.pixeldungeon.windows.WndHero;
import com.etoitau.pixeldungeon.windows.WndInfoCell;
import com.etoitau.pixeldungeon.windows.WndInfoItem;
import com.etoitau.pixeldungeon.windows.WndInfoMob;
import com.etoitau.pixeldungeon.windows.WndInfoPlant;
import com.etoitau.pixeldungeon.windows.WndBag;
import com.etoitau.pixeldungeon.windows.WndMessage;
import com.etoitau.pixeldungeon.windows.WndSkills;
import com.etoitau.pixeldungeon.windows.WndTradeItem;

public class Toolbar extends Component {

    private Tool btnWait;
    private Tool btnSkill;
    private Tool btnLastUsed;
    private Tool btnInfoSearch;
    private Tool btnInventory;
    private Tool btnQuick1;
    private Tool btnQuick2;

    private PickedUpItem pickedUp;

    private boolean lastEnabled = true;

    public static boolean tapAgainToSearch = false;

    private static Toolbar instance;


    public Toolbar() {
        super();

        instance = this;

        height = btnInventory.height();
    }

    @Override
    protected void createChildren() {

        add(btnWait = new Tool(0, 7, 20, 25) {
            @Override
            protected void onClick() {
                Dungeon.hero.rest(false);
            }

            protected boolean onLongClick() {
                Dungeon.hero.rest(true);
                return true;
            }

        });

        add(btnSkill = new Tool(20, 7, 20, 25) {
            @Override
            protected void onClick() {
                GameScene.show(new WndSkills(null, null));
            }

            protected boolean onLongClick() {
                GameScene.show(new WndSkills(null, null));
                return true;
            }

        });

        int imgX;
        switch (Dungeon.hero.heroClass) {
            case HUNTRESS:
                imgX = 158;
                break;
            case WARRIOR:
                imgX = 181;
                break;
            case MAGE:
                imgX = 204;
                break;
            case ROGUE:
                imgX = 227;
                break;
            default:
                imgX = 40;
                break;
        }

        add(btnLastUsed = new Tool(imgX, 7, 20, 25) {
            @Override
            protected void onClick() {
                Dungeon.hero.heroSkills.showLastUsed();
            }

            protected boolean onLongClick() {
                Dungeon.hero.heroSkills.showLastUsed();
                return true;
            }

        });

        add(btnInfoSearch = new Tool(107, 7, 20, 25) {
            @Override
            protected void onClick() {
                if (tapAgainToSearch == false) {
                    GameScene.selectCell(informer);
                } else {
                    Dungeon.hero.search(true);
                }
                tapAgainToSearch = !tapAgainToSearch;
            }

            @Override
            protected boolean onLongClick() {
                Dungeon.hero.search(true);
                return true;
            }
        });

        add(btnInventory = new Tool(60, 7, 23, 25) {
            private GoldIndicator gold;

            @Override
            protected void onClick() {
                GameScene.show(new WndBag(Dungeon.hero.belongings.backpack, null, WndBag.Mode.ALL, null));
            }

            protected boolean onLongClick() {
                GameScene.show(new WndCatalogus());
                return true;
            }

            @Override
            protected void createChildren() {
                super.createChildren();
                gold = new GoldIndicator();
                add(gold);
            }

            @Override
            protected void layout() {
                super.layout();
                gold.fill(this);
            }

        });

        add(btnQuick1 = new QuickslotTool(83, 7, 22, 25, true));
        add(btnQuick2 = new QuickslotTool(83, 7, 22, 25, false));
        btnQuick2.visible = PixelDungeon.secondQuickslot();

        add(pickedUp = new PickedUpItem());
    }

    @Override
    protected void layout() {
        btnInfoSearch.setPos(0, 40);
        btnWait.setPos(x, y);
        btnSkill.setPos(btnWait.right(), y);
        btnLastUsed.setPos(btnSkill.right(), y);
        btnQuick1.setPos(width - btnQuick1.width(), y);
        if (btnQuick2.visible) {
            btnQuick2.setPos(btnQuick1.left() - btnQuick2.width(), y);
            btnInventory.setPos(btnQuick2.left() - btnInventory.width(), y);
        } else {
            btnInventory.setPos(btnQuick1.left() - btnInventory.width(), y);
        }
    }

    @Override
    public void update() {
        super.update();

        if (lastEnabled != Dungeon.hero.ready) {
            lastEnabled = Dungeon.hero.ready;

            for (Gizmo tool : members) {
                if (tool instanceof Tool) {
                    ((Tool) tool).enable(lastEnabled);

                    if (tool == btnLastUsed)
                        tool.visible = Dungeon.hero.heroSkills.lastUsed != null;
                }
            }
        }

        if (!Dungeon.hero.isAlive()) {
            btnInventory.enable(true);
        }
    }

    public void pickup(Item item) {
        pickedUp.reset(item,
                btnInventory.centerX(),
                btnInventory.centerY());
    }

    public static boolean secondQuickslot() {
        return instance.btnQuick2.visible;
    }

    public static void secondQuickslot(boolean value) {
        instance.btnQuick2.visible =
                instance.btnQuick2.active =
                        value;
        instance.layout();
    }

    private static CellSelector.Listener informer = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {

            tapAgainToSearch = false;

            if (cell == null) {
                return;
            }

            if (cell < 0 || cell > Level.LENGTH || (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
                GameScene.show(new WndMessage("You don't know what is there."));
                // in debug mode can manually throw an error by inspecting unexplored tile
                if (BuildConfig.DEBUG) {
                    InterlevelScene.mode = InterlevelScene.Mode.TELEPORT;
                    Game.switchScene(InterlevelScene.class);
                }
                return;
            }

            if (!Dungeon.visible[cell]) {
                GameScene.show(new WndInfoCell(cell));
                return;
            }

            if (cell == Dungeon.hero.pos) {
                GameScene.show(new WndHero());
                return;
            }

            Mob mob = (Mob) Actor.findChar(cell);
            if (mob != null) {
                GameScene.show(new WndInfoMob(mob));
                return;
            }

            Heap heap = Dungeon.level.heaps.get(cell);
            if (heap != null && heap.type != Heap.Type.HIDDEN) {
                if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
                    GameScene.show(new WndTradeItem(heap, false));
                } else {
                    GameScene.show(new WndInfoItem(heap));
                }
                return;
            }

            Plant plant = Dungeon.level.plants.get(cell);
            if (plant != null) {
                GameScene.show(new WndInfoPlant(plant));
                return;
            }

            GameScene.show(new WndInfoCell(cell));
        }

        @Override
        public String prompt() {
            return "Select a cell to examine.\nTap again to search.";
        }
    };

    private static class Tool extends Button {

        private static final int BGCOLOR = 0x7B8073;

        protected Image base;

        public Tool(int x, int y, int width, int height) {
            super();

            base.frame(x, y, width, height);

            this.width = width;
            this.height = height;
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            base = new Image(Assets.TOOLBAR);
            add(base);
        }

        @Override
        protected void layout() {
            super.layout();

            base.x = x;
            base.y = y;
        }

        @Override
        protected void onTouchDown() {
            base.brightness(1.4f);
        }

        @Override
        protected void onTouchUp() {
            if (active) {
                base.resetColor();
            } else {
                base.tint(BGCOLOR, 0.7f);
            }
        }

        public void enable(boolean value) {
            if (value != active) {
                if (value) {
                    base.resetColor();
                } else {
                    base.tint(BGCOLOR, 0.7f);
                }
                active = value;
            }
        }
    }

    private static class QuickslotTool extends Tool {

        private QuickSlot slot;

        public QuickslotTool(int x, int y, int width, int height, boolean primary) {
            super(x, y, width, height);
            if (primary) {
                slot.primary();
            } else {
                slot.secondary();
            }
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            slot = new QuickSlot();
            add(slot);
        }

        @Override
        protected void layout() {
            super.layout();
            slot.setRect(x + 1, y + 2, width - 2, height - 2);
        }

        @Override
        public void enable(boolean value) {
            slot.enable(value);
            super.enable(value);
        }
    }

    private static class PickedUpItem extends ItemSprite {

        private static final float DISTANCE = DungeonTilemap.SIZE;
        private static final float DURATION = 0.2f;

        private float dstX;
        private float dstY;
        private float left;

        public PickedUpItem() {
            super();

            originToCenter();

            active =
                    visible =
                            false;
        }

        public void reset(Item item, float dstX, float dstY) {
            view(item.image(), item.glowing());

            active =
                    visible =
                            true;

            this.dstX = dstX - ItemSprite.SIZE / 2;
            this.dstY = dstY - ItemSprite.SIZE / 2;
            left = DURATION;

            x = this.dstX - DISTANCE;
            y = this.dstY - DISTANCE;
            alpha(1);
        }

        @Override
        public void update() {
            super.update();

            if ((left -= Game.elapsed) <= 0) {

                visible =
                        active =
                                false;

            } else {
                float p = left / DURATION;
                scale.set((float) Math.sqrt(p));
                float offset = DISTANCE * p;
                x = dstX - offset;
                y = dstY - offset;
            }
        }
    }
}
