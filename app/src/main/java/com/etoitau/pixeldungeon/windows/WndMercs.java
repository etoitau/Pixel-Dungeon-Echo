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
import com.watabau.input.Touchscreen;
import com.watabau.noosa.BitmapText;
import com.watabau.noosa.BitmapTextMultiline;
import com.watabau.noosa.ColorBlock;
import com.watabau.noosa.Game;
import com.watabau.noosa.Image;
import com.watabau.noosa.TouchArea;
import com.watabau.noosa.audio.Sample;
import com.watabau.noosa.ui.Component;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.PixelDungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.hero.HeroClass;
import com.etoitau.pixeldungeon.actors.hero.Storage;
import com.etoitau.pixeldungeon.actors.mobs.npcs.HiredMerc;
import com.etoitau.pixeldungeon.actors.skills.BranchSkill;
import com.etoitau.pixeldungeon.actors.skills.Skill;
import com.etoitau.pixeldungeon.items.Gold;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.armor.Armor;
import com.etoitau.pixeldungeon.items.armor.ClothArmor;
import com.etoitau.pixeldungeon.items.armor.LeatherArmor;
import com.etoitau.pixeldungeon.items.bags.Bag;
import com.etoitau.pixeldungeon.items.bags.Keyring;
import com.etoitau.pixeldungeon.items.bags.ScrollHolder;
import com.etoitau.pixeldungeon.items.bags.SeedPouch;
import com.etoitau.pixeldungeon.items.bags.WandHolster;
import com.etoitau.pixeldungeon.items.food.ChargrilledMeat;
import com.etoitau.pixeldungeon.items.potions.PotionOfHealing;
import com.etoitau.pixeldungeon.items.wands.WandOfBlink;
import com.etoitau.pixeldungeon.items.weapon.Weapon;
import com.etoitau.pixeldungeon.items.weapon.melee.Dagger;
import com.etoitau.pixeldungeon.items.weapon.melee.Knuckles;
import com.etoitau.pixeldungeon.items.weapon.melee.Mace;
import com.etoitau.pixeldungeon.items.weapon.missiles.Bow;
import com.etoitau.pixeldungeon.items.weapon.missiles.FrostBow;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.scenes.PixelScene;
import com.etoitau.pixeldungeon.sprites.HeroSprite;
import com.etoitau.pixeldungeon.sprites.ItemSprite;
import com.etoitau.pixeldungeon.sprites.ItemSpriteSheet;
import com.etoitau.pixeldungeon.sprites.MercSprite;
import com.etoitau.pixeldungeon.sprites.SkillSprite;
import com.etoitau.pixeldungeon.ui.Icons;
import com.etoitau.pixeldungeon.ui.ItemSlot;
import com.etoitau.pixeldungeon.ui.RedButton;
import com.etoitau.pixeldungeon.ui.SkillSlot;
import com.etoitau.pixeldungeon.ui.Window;
import com.etoitau.pixeldungeon.utils.Utils;

import java.util.ArrayList;

public class WndMercs extends WndTabbed {

	public static enum Mode {
		ALL,
		BRUTE,
		WIZARD,
		THIEF,
		ARCHER,
		ARCHERMAIDEN
	}

    float pos = 5;
    float GAP = 2;

    private static final int WIDTH_P	= 120;
    private static final int WIDTH_L	= 144;

    protected static final int TAB_WIDTH	= 25;

    private static final String TXT_TITLE =  "Hire A Mercenary";

    private static final String TXT_MERCENARIES_DETAIL = "Mercenaries will fight for you in exchange for a fee.\n \n"
            + "There are five mercenary classes each with strengths and weaknesses. \n"
            + "Each class has one skill with the exception of the Archer Maiden who knows two.\n"
            + "Skills level with the mercenary and are capped at level 3. \n"
            + "Mercenaries have the same level as the hero and level with him. \n \n"
            + "You can unequip an item from a merc by tapping on it and holding down. \n \n"
            + "Mercs will consume any healing potion equipped on them if about to die. \n \n"
            + "You cannot hire the mercenary equivalent of your class.";



    private static final String TXT_NO_GOLD = "Insufficient Gold";

    public static int maxHeight = 0;

    public WndMercs(final Mode mode)
    {
        super();

        int width = PixelDungeon.landscape() ? WIDTH_L : WIDTH_P;

        if(mode == Mode.ALL) {
            Component titlebar = new IconTitle(new SkillSprite(96), TXT_TITLE);
            titlebar.setRect(0, 0, width, 0);
            add(titlebar);

            resize(width, (int) titlebar.bottom());


            pos = (int) titlebar.bottom() + GAP * 2;


            BitmapTextMultiline info = PixelScene.createMultiline(6);
            add(info);

            info.text(TXT_MERCENARIES_DETAIL);
            info.maxWidth = width;
            info.measure();
            info.y = pos;

            pos = (int) info.y + (int) info.height() + GAP * 2;

            if(maxHeight < pos)
                maxHeight = (int)pos;

            resize(width,  maxHeight);
        }
        else
        {
            Component titlebar = new IconTitle(new SkillSprite(getImage(mode)), "Hire " + (mode == Mode.ARCHER || mode == Mode.ARCHERMAIDEN ? "An " : "A ") +   getName(mode));
            titlebar.setRect(0, 0, width, 0);
            add(titlebar);

            resize(width, (int) titlebar.bottom());


            pos = (int) titlebar.bottom() + GAP * 2;


            BitmapTextMultiline info = PixelScene.createMultiline(6);
            add(info);

            info.text(getMercDetails(mode));
            info.maxWidth = width;
            info.measure();
            info.y = pos;

            pos = (int) info.y + (int) info.height() + GAP * 2;

            BitmapText stats = PixelScene.createText( Utils.capitalize(getName(mode) + " Stats" ), 9 );
            stats.hardlight( TITLE_COLOR );
            stats.measure();
            add( stats );

            stats.y = pos;

            pos = stats.y + stats.height() + GAP;

            BitmapTextMultiline infoStats = PixelScene.createMultiline(6);
            add(infoStats);

            infoStats.text(getMercStats(mode));
            infoStats.maxWidth = width;
            infoStats.measure();
            infoStats.y = pos;

            pos = infoStats.y + infoStats.height() + 2 * GAP;

            BitmapText equipment = PixelScene.createText( Utils.capitalize("Standard Layout" ), 9 );
            equipment.hardlight( TITLE_COLOR );
            equipment.measure();
            add( equipment );

            equipment.y = pos;

            pos = equipment.y + equipment.height() + GAP;


            if(mode == Mode.BRUTE) {
                final Image imageWeapon = new ItemSprite(new Mace());
                add(imageWeapon);

                imageWeapon.x = 0;
                imageWeapon.y = pos;

                TouchArea hotArea_imageWeapon = new TouchArea(imageWeapon) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageWeapon);
                        parent.add(new previewInformation(tmp, "Mace", "A Brute favorite.."));
                    }
                };
                add(hotArea_imageWeapon);


                final Image imageArmor = new ItemSprite(new LeatherArmor());
                add(imageArmor);

                imageArmor.x = imageWeapon.x + imageWeapon.width() + GAP;
                imageArmor.y = pos;

                TouchArea hotArea_imageArmo = new TouchArea(imageArmor) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageArmor);
                        parent.add(new previewInformation(tmp, "Leather Armor", "Leather Armor provides basic protection"));
                    }
                };
                add(hotArea_imageArmo);

                final Image image = new ItemSprite(new ChargrilledMeat());
                add(image);

                image.x = imageArmor.x + imageArmor.width() + GAP;
                image.y = pos + 2;

                TouchArea hotArea_image = new TouchArea(image) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(image);
                        parent.add(new previewInformation(tmp, "Meat", "These Brutes are always hungry..."));
                    }
                };
                add(hotArea_image);

                final Image imageSkill = new SkillSprite(3);
                add(imageSkill);

                imageSkill.x = image.x + image.width() + GAP;
                imageSkill.y = pos;

                TouchArea hotArea_imageSkill = new TouchArea(imageSkill) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageSkill);
                        parent.add(new previewInformation(tmp, "Toughness", "Brutes are physically strong. They take less damage than what others would have."));
                    }
                };
                add(hotArea_imageSkill);

                pos = image.y + image.height() + GAP * 3;
            }
            else if(mode == Mode.WIZARD)
            {
                final Image imageWeapon = new ItemSprite(new Knuckles());
                add(imageWeapon);

                imageWeapon.x = 0;
                imageWeapon.y = pos + 2;

                TouchArea hotArea_imageWeapon = new TouchArea(imageWeapon) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageWeapon);
                        parent.add(new previewInformation(tmp, "Knuckles", "A weak weapon."));
                    }
                };
                add(hotArea_imageWeapon);


                final Image imageArmor = new ItemSprite(new ClothArmor());
                add(imageArmor);

                imageArmor.x = imageWeapon.x + imageWeapon.width() + GAP;
                imageArmor.y = pos + 2;

                TouchArea hotArea_imageArmo = new TouchArea(imageArmor) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageArmor);
                        parent.add(new previewInformation(tmp, "Cloths", "Basic cloths."));
                    }
                };
                add(hotArea_imageArmo);

                final Image image = new ItemSprite(new PotionOfHealing());
                add(image);

                image.x = imageArmor.x + imageArmor.width() + GAP;
                image.y = pos + 2;

                TouchArea hotArea_image = new TouchArea(image) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(image);
                        parent.add(new previewInformation(tmp, "Healing Potion", "A Healing Potion."));
                    }
                };
                add(hotArea_image);

                final Image imageSkill = new SkillSprite(41);
                add(imageSkill);

                imageSkill.x = image.x + image.width() + GAP;
                imageSkill.y = pos;

                TouchArea hotArea_imageSkill = new TouchArea(imageSkill) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageSkill);
                        parent.add(new previewInformation(tmp, "Summon Rat", "The wizard summons rats to fight for him."));
                    }
                };
                add(hotArea_imageSkill);

                pos = image.y + image.height() + GAP * 3;
            }
            else if(mode == Mode.THIEF)
            {
                final Image imageWeapon = new ItemSprite(new Dagger());
                add(imageWeapon);

                imageWeapon.x = 0;
                imageWeapon.y = pos + 1;

                TouchArea hotArea_imageWeapon = new TouchArea(imageWeapon) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageWeapon);
                        parent.add(new previewInformation(tmp, "Dagger", "A basic dagger."));
                    }
                };
                add(hotArea_imageWeapon);


                final Image imageArmor = new ItemSprite(new ClothArmor());
                add(imageArmor);

                imageArmor.x = imageWeapon.x + imageWeapon.width() + GAP;
                imageArmor.y = pos + 2;

                TouchArea hotArea_imageArmo = new TouchArea(imageArmor) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageArmor);
                        parent.add(new previewInformation(tmp, "Cloths", "Basic cloths."));
                    }
                };
                add(hotArea_imageArmo);

                final Image image = new ItemSprite(new PotionOfHealing());
                add(image);

                image.x = imageArmor.x + imageArmor.width() + GAP;
                image.y = pos + 2;

                TouchArea hotArea_image = new TouchArea(image) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(image);
                        parent.add(new previewInformation(tmp, "Healing Potion", "A Healing Potion."));
                    }
                };
                add(hotArea_image);

                final Image imageSkill = new SkillSprite(57);
                add(imageSkill);

                imageSkill.x = image.x + image.width() + GAP;
                imageSkill.y = pos;

                TouchArea hotArea_imageSkill = new TouchArea(imageSkill) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageSkill);
                        parent.add(new previewInformation(tmp, "Venom", "Thieves have a small chance of poisoning enemies in combat."));
                    }
                };
                add(hotArea_imageSkill);

                pos = image.y + image.height() + GAP * 3;
            }
            else if(mode == Mode.ARCHER)
            {
                final Image imageWeapon = new ItemSprite(new Bow());
                add(imageWeapon);

                imageWeapon.x = 0;
                imageWeapon.y = pos + 1;

                TouchArea hotArea_imageWeapon = new TouchArea(imageWeapon) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageWeapon);
                        parent.add(new previewInformation(tmp, "Bow", "A basic bow."));
                    }
                };
                add(hotArea_imageWeapon);


                final Image imageArmor = new ItemSprite(new ClothArmor());
                add(imageArmor);

                imageArmor.x = imageWeapon.x + imageWeapon.width() + GAP;
                imageArmor.y = pos + 2;

                TouchArea hotArea_imageArmo = new TouchArea(imageArmor) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageArmor);
                        parent.add(new previewInformation(tmp, "Cloths", "Basic cloths."));
                    }
                };
                add(hotArea_imageArmo);

                final Image image = new ItemSprite(new PotionOfHealing());
                add(image);

                image.x = imageArmor.x + imageArmor.width() + GAP;
                image.y = pos + 2;

                TouchArea hotArea_image = new TouchArea(image) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(image);
                        parent.add(new previewInformation(tmp, "Healing Potion", "A Healing Potion."));
                    }
                };
                add(hotArea_image);

                final Image imageSkill = new SkillSprite(82);
                add(imageSkill);

                imageSkill.x = image.x + image.width();
                imageSkill.y = pos + 1;

                TouchArea hotArea_imageSkill = new TouchArea(imageSkill) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageSkill);
                        parent.add(new previewInformation(tmp, "Knee Shot", "High accuracy and knowledge in body anatomy allow the archer to hit weak spots crippling targets."));
                    }
                };
                add(hotArea_imageSkill);

                pos = image.y + image.height() + GAP * 3;
            }
            else if(mode == Mode.ARCHERMAIDEN)
            {
                final Image imageWeapon = new ItemSprite(new FrostBow());
                add(imageWeapon);

                imageWeapon.x = 0;
                imageWeapon.y = pos + 1;

                TouchArea hotArea_imageWeapon = new TouchArea(imageWeapon) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageWeapon);
                        parent.add(new previewInformation(tmp, "FrostBow", "A bow imbued with magic. It can freeze targets on occasion."));
                    }
                };
                add(hotArea_imageWeapon);




                final Image image = new ItemSprite(new PotionOfHealing());
                add(image);

                image.x = imageWeapon.x + imageWeapon.width() + GAP;
                image.y = pos + 2;

                TouchArea hotArea_image = new TouchArea(image) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(image);
                        parent.add(new previewInformation(tmp, "Healing Potion", "A Healing Potion."));
                    }
                };
                add(hotArea_image);

                final Image imageSkill = new SkillSprite(82);
                add(imageSkill);

                imageSkill.x = image.x + image.width();
                imageSkill.y = pos + 1;

                TouchArea hotArea_imageSkill = new TouchArea(imageSkill) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageSkill);
                        parent.add(new previewInformation(tmp, "Knee Shot", "High accuracy and knowledge in body anatomy allow the archer maiden to hit weak spots crippling targets."));
                    }
                };
                add(hotArea_imageSkill);

                final Image imageSkillB = new SkillSprite(106);
                add(imageSkillB);

                imageSkillB.x = imageSkill.x + imageSkill.width() + GAP;
                imageSkillB.y = pos + 1;

                TouchArea hotArea_imageSkillB = new TouchArea(imageSkillB) {
                    @Override
                    protected void onClick(Touchscreen.Touch touch) {
                        Image tmp = new Image();
                        tmp.copy(imageSkillB);
                        parent.add(new previewInformation(tmp, "Keen Eye", "Can shoot through friendly units without harming them."));
                    }
                };
                add(hotArea_imageSkillB);

                pos = image.y + image.height() + GAP * 3;
            }

            if(mode != Mode.ARCHERMAIDEN || HiredMerc.archerMaidenUnlocked == true) {
                RedButton btnHire = new RedButton("Hire " + getName(mode) + " For " + getGoldCost(mode) + " gold") {
                    @Override
                    protected void onClick() {
                        if (Dungeon.gold < getGoldCost(mode)) {
                            text(TXT_NO_GOLD);
                        } else {

                            //Dungeon.hero.checkMerc = true;
                            ArrayList<Integer> respawnPoints = new ArrayList<Integer>();
                            for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
                                int p = Dungeon.hero.pos + Level.NEIGHBOURS8[i];
                                if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
                                    respawnPoints.add( p );
                                }
                            }
                            if(respawnPoints.size() > 0) {
                                Dungeon.gold -= getGoldCost(mode);
                                Dungeon.hero.hiredMerc = new HiredMerc(getMercType(mode));
                                Dungeon.hero.hiredMerc.spawn(Dungeon.hero.lvl);
                                Dungeon.hero.hiredMerc.HP = Dungeon.hero.hiredMerc.mercType.getHealth(Dungeon.hero.lvl);
                                Dungeon.hero.hiredMerc.mercType.setEquipment(Dungeon.hero.hiredMerc);
                                Dungeon.hero.hiredMerc.pos = respawnPoints.get(0);
                                GameScene.add( Dungeon.hero.hiredMerc );
                                ((MercSprite) Dungeon.hero.hiredMerc.sprite).updateArmor();
                                WandOfBlink.appear(Dungeon.hero.hiredMerc, respawnPoints.get(0));

                                Dungeon.hero.spend( 1 / Dungeon.hero.speed() );
                            }
                            hide();
                            //WndStory.showStory("Your mercenary will arrive shortly");
                        }

                    }
                };

                btnHire.setRect((width - 120) / 2 > 0 ? (width - 120) / 2 : 0, pos,
                        120, 20);
                add(btnHire);

                pos = btnHire.bottom() + GAP;
            }
            else
            {
                RedButton btnHire = new RedButton("Unlock") {
                    @Override
                    protected void onClick() {
                        Image tmp = new  SkillSprite(104);
                        parent.add(new previewInformation(tmp, "Archer Maiden", HiredMerc.MAIDEN_UNLOCK_BY));
                    }
                };

                btnHire.setRect((width - 120) / 2 > 0 ? (width - 120) / 2 : 0, pos,
                        120, 20);
                add(btnHire);

                pos = btnHire.bottom() + GAP;
            }

            if(maxHeight < pos)
                maxHeight = (int)pos;

            resize(width,  maxHeight);
        }

        MercenaryTab tabAll = new MercenaryTab(Mode.ALL);
        tabAll.setSize(TAB_WIDTH, tabHeight());
        add(tabAll);
        tabAll.select(mode == Mode.ALL);

        if(Dungeon.hero.heroClass != HeroClass.WARRIOR) {
            MercenaryTab tabBrute = new MercenaryTab(Mode.BRUTE);
            tabBrute.setSize(TAB_WIDTH, tabHeight());
            add(tabBrute);
            tabBrute.select(mode == Mode.BRUTE);
        }

        if(Dungeon.hero.heroClass != HeroClass.MAGE) {
        MercenaryTab tabWizard = new MercenaryTab(Mode.WIZARD);
        tabWizard.setSize(TAB_WIDTH, tabHeight());
        add(tabWizard);
        tabWizard.select(mode == Mode.WIZARD);
        }

        if(Dungeon.hero.heroClass != HeroClass.ROGUE) {
        MercenaryTab tabThief = new MercenaryTab(Mode.THIEF);
        tabThief.setSize(TAB_WIDTH, tabHeight());
        add(tabThief);
        tabThief.select(mode == Mode.THIEF);
        }

        if(Dungeon.hero.heroClass != HeroClass.HUNTRESS) {
        MercenaryTab tabArcher = new MercenaryTab(Mode.ARCHER);
        tabArcher.setSize(TAB_WIDTH, tabHeight());
        add(tabArcher);
        tabArcher.select(mode == Mode.ARCHER);
        }

        MercenaryTab tabMaiden = new MercenaryTab(Mode.ARCHERMAIDEN);
        tabMaiden.setSize(TAB_WIDTH, tabHeight());
        add(tabMaiden);
        tabMaiden.select(mode == Mode.ARCHERMAIDEN);

    }

    private String getMercDetails(Mode mode)
    {
        switch(mode)
        {
            case BRUTE: return "Brutes are strong mercenaries who rely in physical fitness.\n \n";
            case WIZARD: return "Wizards choose the path of magic. They are physically weak so they rely on summoned units.\n \n";
            case THIEF: return "Thieves rely on stealth and poison in combat. Each strike has a small chance of poisoning enemies.\n \n";
            case ARCHER: return "Archers are physically weak so they strike from a distance. Chance of crippling enemies.\n \n";
            case ARCHERMAIDEN: return "Archer Maidens are elite Archers. Only the select few reach this rank.\n \n";
        }

        return "Brutes are strong mercenaries who rely in physical fitness.\n \n";
    }

    private String getMercStats(Mode mode)
    {
        switch(mode)
        {
            case BRUTE: return "- Health: 20 + level x 3\n"
                    + "- Strength: 13 + level x 0.33\n"
                    + "- Speed: Slow\n"
                    + "- Skill: Endurance\n"
                    + "- Special: Can equip any item in carry slot.\n"
                    + "- Cost: 100 gold + 25 gold per level.\n";

            case WIZARD: return "- Health: 10 + level\n"
                    + "- Strength: 10 + level x 0.2\n"
                    + "- Speed: Normal\n"
                    + "- Skill: Summon Rat\n"
                    + "- Special: Summons minions.\n"
                    + "- Cost: 80 gold + 20 gold per level.\n";
            case THIEF:  return "- Health: 15 + level x 2\n"
                    + "- Strength: 13 + level x 0.25\n"
                    + "- Speed: Very Fast\n"
                    + "- Skill: Venom\n"
                    + "- Special: Poisons enemies with his skill.\n"
                    + "- Cost: 70 gold + 15 gold per level.\n";
            case ARCHER:  return "- Health: 15 + level x 2\n"
                    + "- Strength: 11 + level x 0.25\n"
                    + "- Speed: Fast\n"
                    + "- Skill: Knee Shot\n"
                    + "- Special: Attacks from a distance.\n"
                    + "- Cost: 90 gold + 20 gold per level.\n";
            case ARCHERMAIDEN:  return "- Health: 17 + level x 2\n"
                    + "- Strength: 12 + level x 0.25\n"
                    + "- Speed: Fast\n"
                    + "- Skills: Knee Shot and Keen Eye\n"
                    + "- Special: Exclusive access to the Keen Eye skill.\n"
                    + "- Cost: 90 gold + 25 gold per level.\n";
        }

        return "- Health: 20 + level x 3\n"
                + "- Strength: 13 + level x 0.33\n"
                + "- Speed: Slow\n"
                + "- Skill: Endurance\n"
                + "- Special: Can equip any item in carry slot.\n"
                + "- Cost: 100 gold + 25 gold per level.\n";
    }
    private int getImage(Mode mode)
    {
        switch (mode)
        {
            case BRUTE: return 0;
            case WIZARD: return 24;
            case THIEF: return 48;
            case ARCHER: return 72;
            case ARCHERMAIDEN: return 104;
        }

        return 0;
    }

    private String getName(Mode mode)
    {
        switch (mode)
        {
            case BRUTE: return "Brute";
            case WIZARD: return "Wizard";
            case THIEF: return "Thief";
            case ARCHER: return "Archer";
            case ARCHERMAIDEN: return "ArcherMaiden";
        }

        return "Brute";
    }

    private HiredMerc.MERC_TYPES getMercType(Mode mode)
    {
        switch (mode)
        {
            case BRUTE: return HiredMerc.MERC_TYPES.Brute;
            case WIZARD: return HiredMerc.MERC_TYPES.Wizard;
            case THIEF: return HiredMerc.MERC_TYPES.Thief;
            case ARCHER: return HiredMerc.MERC_TYPES.Archer;
            case ARCHERMAIDEN: return HiredMerc.MERC_TYPES.ArcherMaiden;
        }

        return HiredMerc.MERC_TYPES.Brute;
    }

    private int getGoldCost(Mode mode)
    {
        switch (mode)
        {
            case BRUTE: return 100 + Dungeon.hero.lvl * 25;
            case WIZARD: return 80 + Dungeon.hero.lvl * 20;
            case THIEF: return 75 + Dungeon.hero.lvl * 15;
            case ARCHER: return 90 + Dungeon.hero.lvl * 20;
            case ARCHERMAIDEN: return 90 + Dungeon.hero.lvl * 25;
        }

        return 0;
    }

    @Override
    protected void onClick( Tab tab ) {

        parent.add(new WndMercs(((MercenaryTab)tab).mode));
        hide();
    }

    private class MercenaryTab extends Tab {

        private Image icon;

        Mode mode = Mode.BRUTE;

        public MercenaryTab(Mode mode  ) {
            super();

            this.mode = mode;

            switch (mode)
            {
                case ALL:  icon = Icons.get( Icons.ALL_MERCS ); break;
                case ARCHER:  icon = Icons.get( Icons.ARCHER ); break;
                case BRUTE: icon = Icons.get( Icons.BRUTE ); break;
                case WIZARD: icon = Icons.get( Icons.WIZARD ); break;
                case THIEF: icon = Icons.get( Icons.THIEF ); break;
                case ARCHERMAIDEN: icon = Icons.get( Icons.ARCHER_MAIDEN ); break;

            }

            add( icon );
        }

        @Override
        protected void select( boolean value ) {
            super.select( value );
            icon.am = selected ? 1.0f : 0.6f;
        }

        @Override
        protected void layout() {
            super.layout();

            icon.copy( icon );
            icon.x = x + (width - icon.width) / 2;
            icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
            if (!selected && icon.y < y + CUT) {
                RectF frame = icon.frame();
                frame.top += (y - icon.y) / icon.texture.height;
                icon.frame( frame );
                icon.y = y;
            }
        }
    }

    private static class MercenaryTitle extends Component {

        private static final int GAP	= 2;

        private SkillSprite image;
        private BitmapText title;


        public MercenaryTitle(int image, String name) {



            this.image = new SkillSprite(image);


            title = PixelScene.createText( Utils.capitalize(name ), 9 );
            title.hardlight( TITLE_COLOR );
            title.measure();
            add( title );
            add(this.image);

        }

        @Override
        protected void layout() {

            image.x = 0;
            image.y = Math.max( 0, title.height() + GAP - image.height );

            title.x = image.width + GAP;
            title.y = image.height - GAP - title.baseLine();



            height = image.y + image.height();
        }
    }

    private class previewInformation extends Window
    {
        public previewInformation(Image image, String title, String description)
        {

            IconTitle titlebar = new IconTitle();
            titlebar.icon( image);
            titlebar.label( Utils.capitalize( title ), TITLE_COLOR );
            titlebar.setRect( 0, 0, 100, 0 );
            add( titlebar );

            BitmapTextMultiline txtInfo = PixelScene.createMultiline( description, 6 );
            txtInfo.maxWidth = 100;
            txtInfo.measure();
            txtInfo.x = titlebar.left();
            txtInfo.y = titlebar.bottom() + GAP;
            add( txtInfo );

            resize( 100, (int) txtInfo.y + (int) txtInfo.height() + (int)GAP);
        }
    }

}
