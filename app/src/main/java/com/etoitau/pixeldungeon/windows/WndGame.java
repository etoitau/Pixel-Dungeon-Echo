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

import java.io.IOException;

import com.etoitau.pixeldungeon.BuildConfig;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.items.Ankh;
import com.etoitau.pixeldungeon.items.armor.Armor;
import com.etoitau.pixeldungeon.items.armor.PlateArmor;
import com.etoitau.pixeldungeon.items.bags.PotionBelt;
import com.etoitau.pixeldungeon.items.bags.ScrollHolder;
import com.etoitau.pixeldungeon.items.bags.SeedPouch;
import com.etoitau.pixeldungeon.items.bags.WandHolster;
import com.etoitau.pixeldungeon.items.food.Pasty;
import com.etoitau.pixeldungeon.items.potions.Potion;
import com.etoitau.pixeldungeon.items.potions.PotionOfExperience;
import com.etoitau.pixeldungeon.items.potions.PotionOfHealing;
import com.etoitau.pixeldungeon.items.potions.PotionOfInvisibility;
import com.etoitau.pixeldungeon.items.potions.PotionOfMight;
import com.etoitau.pixeldungeon.items.potions.PotionOfMindVision;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.etoitau.pixeldungeon.items.scrolls.ScrollOfWipeOut;
import com.etoitau.pixeldungeon.items.wands.Wand;
import com.etoitau.pixeldungeon.items.wands.WandOfBlink;
import com.etoitau.pixeldungeon.items.wands.WandOfFirebolt;
import com.etoitau.pixeldungeon.items.wands.WandOfSlowness;
import com.etoitau.pixeldungeon.items.wands.WandOfTeleportation;
import com.etoitau.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.etoitau.pixeldungeon.items.weapon.melee.WarHammer;
import com.etoitau.pixeldungeon.items.weapon.missiles.BombArrow;
import com.watabau.noosa.Game;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.PixelDungeon;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.scenes.InterlevelScene;
import com.etoitau.pixeldungeon.scenes.RankingsScene;
import com.etoitau.pixeldungeon.scenes.TitleScene;
import com.etoitau.pixeldungeon.ui.Icons;
import com.etoitau.pixeldungeon.ui.RedButton;
import com.etoitau.pixeldungeon.ui.Window;

public class WndGame extends Window {

    private static final String TXT_SETTINGS = "Settings";
    private static final String TXT_CHALLEGES = "Challenges";
    private static final String TXT_RANKINGS = "Rankings";
    private static final String TXT_START = "Start New Game";
    private static final String TXT_MENU = "Main Menu";
    private static final String TXT_EXIT = "Exit Game";
    private static final String TXT_RETURN = "Return to Game";

    private static final int WIDTH = 120;
    private static final int BTN_HEIGHT = 20;
    private static final int GAP = 2;

    private int pos;

    public WndGame() {

        super();

        addButton(new RedButton(TXT_SETTINGS) {
            @Override
            protected void onClick() {
                hide();
                GameScene.show(new WndSettings(true));
            }
        });

        if (Dungeon.challenges > 0) {
            addButton(new RedButton(TXT_CHALLEGES) {
                @Override
                protected void onClick() {
                    hide();
                    GameScene.show(new WndChallenges(Dungeon.challenges, false));
                }
            });
        }

        if (!Dungeon.hero.isAlive()) {
            RedButton btnStart;
            addButton(btnStart = new RedButton(TXT_START) {
                @Override
                protected void onClick() {
                    Dungeon.hero = null;
                    PixelDungeon.challenges(Dungeon.challenges);
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    InterlevelScene.noStory = true;
                    Game.switchScene(InterlevelScene.class);
                }
            });
            btnStart.icon(Icons.get(Dungeon.hero.heroClass));

            addButton(new RedButton(TXT_RANKINGS) {
                @Override
                protected void onClick() {
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    Game.switchScene(RankingsScene.class);
                }
            });
        }

        addButtons(
                new RedButton(TXT_MENU) {
                    @Override
                    protected void onClick() {
                        try {
                            Dungeon.saveAll();
                        } catch (IOException e) {
                            // Do nothing
                        }
                        Game.switchScene(TitleScene.class);
                    }
                }, new RedButton(TXT_EXIT) {
                    @Override
                    protected void onClick() {
                        Game.instance.finish();
                    }
                }
        );


        // Debug God Mode
        if (BuildConfig.DEBUG) {
            addButton(new RedButton("Skip Level") {
                @Override
                protected void onClick() {
                    InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                    Game.switchScene(InterlevelScene.class);
                }
            });
            addButton(new RedButton("God Mode") {
                @Override
                protected void onClick() {
                    Hero hero = Dungeon.hero;

                    new PotionBelt().collect();
                    new ScrollHolder().collect();
                    new WandHolster().collect();
                    new SeedPouch().collect();

                    for (int i = 0; i < 12; i++) {
                        new ScrollOfUpgrade().identify().collect();
                        new PotionOfMight().execute(hero, Potion.AC_DRINK);
                        new BombArrow().collect();
                    }

                    for (int i = 0; i < 6; i++) {
                        new ScrollOfIdentify().identify().collect();
                        new ScrollOfWipeOut().collect();
                        new ScrollOfEnchantment().identify().collect();
                        new ScrollOfTeleportation().identify().collect();
                        new PotionOfInvisibility().identify().collect();
                        new PotionOfMindVision().identify().collect();
                        new Ankh().collect();
                    }

                    for (int i = 0; i < 25; i++) {
                        new PotionOfExperience().execute(hero, Potion.AC_DRINK);
                        new ScrollOfMagicMapping().identify().collect();
                    }

//                    try {
//                        for (Class scrollClass: Scroll.getUnknown()) {
//                            Scroll scroll = (Scroll) scrollClass.newInstance();
//                            if (!(scroll instanceof ScrollOfFrostLevel))
//                                scroll.identify().collect();
//                        }
//                        for (Class potionClass: Potion.getUnknown()) {
//                            Potion potion = (Potion) potionClass.newInstance();
//                            potion.identify().collect();
//                        }
//                        for (Class ringClass: Ring.getUnknown()) {
//                            Ring ring = (Ring) ringClass.newInstance();
//                            ring.identify().collect();
//                        }
//                        for (Class wandClass: Wand.getUnknown()) {
//                            Wand wand = (Wand) wandClass.newInstance();
//                            wand.identify().collect();
//                        }
//                    } catch (Exception e) {
//                        // do nothing
//                    }

                    Armor armor = new PlateArmor();
                    armor.identify().collect();
                    for (int i = 0; i < 6; i++) {
                        armor.upgrade();
                    }
                    MeleeWeapon weapon = new WarHammer();
                    weapon.identify().collect();
                    for (int i = 0; i < 6; i++) {
                        weapon.upgrade();
                    }

                    for (int i = 0; i < 5; i++) {
                        new PotionOfHealing().identify().collect();
                        new Pasty().collect();
                    }
                    new WandOfFirebolt().identify().collect();
                    new WandOfSlowness().identify().collect();
                    new WandOfTeleportation().identify().collect();
                    Wand wand = new WandOfBlink();
                    wand.identify().collect();
                    for (int i = 0; i < 4; i++) {
                        wand.upgrade();
                    }


                }
            });
        }


        addButton(new RedButton(TXT_RETURN) {
            @Override
            protected void onClick() {
                hide();
            }
        });

        resize(WIDTH, pos);
    }

    private void addButton(RedButton btn) {
        add(btn);
        btn.setRect(0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT);
        pos += BTN_HEIGHT;
    }

    private void addButtons(RedButton btn1, RedButton btn2) {
        add(btn1);
        btn1.setRect(0, pos > 0 ? pos += GAP : 0, (WIDTH - GAP) / 2, BTN_HEIGHT);
        add(btn2);
        btn2.setRect(btn1.right() + GAP, btn1.top(), WIDTH - btn1.right() - GAP, BTN_HEIGHT);
        pos += BTN_HEIGHT;
    }
}
