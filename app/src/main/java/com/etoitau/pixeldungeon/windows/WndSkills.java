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

import com.watabau.noosa.BitmapText;
import com.watabau.noosa.ColorBlock;
import com.watabau.noosa.Image;
import com.watabau.noosa.audio.Sample;
import com.etoitau.pixeldungeon.Assets;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.skills.BranchSkill;
import com.etoitau.pixeldungeon.actors.skills.Skill;
import com.etoitau.pixeldungeon.items.bags.Bag;
import com.etoitau.pixeldungeon.items.bags.Keyring;
import com.etoitau.pixeldungeon.items.bags.ScrollHolder;
import com.etoitau.pixeldungeon.items.bags.SeedPouch;
import com.etoitau.pixeldungeon.items.bags.WandHolster;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.scenes.PixelScene;
import com.etoitau.pixeldungeon.ui.Icons;
import com.etoitau.pixeldungeon.ui.SkillSlot;
import com.etoitau.pixeldungeon.utils.Utils;

public class WndSkills extends WndTabbed {

    protected static final int SLOT_SIZE = 28;
    protected static final int SLOT_MARGIN = 1;

    protected static final int TITLE_HEIGHT = 12;

    private Listener listener;
    private String title;

    private final int NCOLS = 4;
    private final int NROWS = 3;

    protected int count;
    protected int col;
    protected int row;


    public WndSkills(Listener listener, String title) {

        super();

        this.listener = listener;
        this.title = title;

        int slotsWidth = SLOT_SIZE * NCOLS + SLOT_MARGIN * (NCOLS - 1);
        int slotsHeight = SLOT_SIZE * NROWS + SLOT_MARGIN * (NROWS - 1);

        BitmapText txtTitle = PixelScene.createText(title != null ? title : Utils.capitalize("Skills" + (Skill.availableSkill > 0 ? " (" + Skill.availableSkill + " points)" : "")), 9);
        txtTitle.hardlight(TITLE_COLOR);
        txtTitle.measure();
        txtTitle.x = (int) (slotsWidth - txtTitle.width()) / 2;
        txtTitle.y = (int) (TITLE_HEIGHT - txtTitle.height()) / 2;
        add(txtTitle);

        placeSkills();


        resize(slotsWidth, slotsHeight + TITLE_HEIGHT);
    }


    protected void placeSkills() {


        placeSkill(Dungeon.hero.heroSkills.branchPA, 0, 0);
        placeSkill(Dungeon.hero.heroSkills.passiveA1, 0, 1);
        placeSkill(Dungeon.hero.heroSkills.passiveA2, 0, 2);
        placeSkill(Dungeon.hero.heroSkills.passiveA3, 0, 3);

        placeSkill(Dungeon.hero.heroSkills.branchPB, 1, 0);
        placeSkill(Dungeon.hero.heroSkills.passiveB1, 1, 1);
        placeSkill(Dungeon.hero.heroSkills.passiveB2, 1, 2);
        placeSkill(Dungeon.hero.heroSkills.passiveB3, 1, 3);

        placeSkill(Dungeon.hero.heroSkills.branchA, 2, 0);
        placeSkill(Dungeon.hero.heroSkills.active1, 2, 1);
        placeSkill(Dungeon.hero.heroSkills.active2, 2, 2);
        placeSkill(Dungeon.hero.heroSkills.active3, 2, 3);

    }

    protected void placeSkill(final Skill skill, int row, int col) {

        int x = col * (SLOT_SIZE + SLOT_MARGIN);
        int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN);

        add(new SkillButton(skill).setPos(x, y));
    }

    @Override
    public void onMenuPressed() {
        if (listener == null) {
            hide();
        }
    }

    @Override
    public void onBackPressed() {
        if (listener != null) {
            listener.onSelect(null);
        }
        super.onBackPressed();
    }

    @Override
    protected void onClick(Tab tab) {
        hide();
        GameScene.show(new WndSkills(listener, title));
    }

    @Override
    protected int tabHeight() {
        return 20;
    }

    private class SkillButton extends SkillSlot {

        private static final int NORMAL = 0xFF4A4D44;
        private static final int EQUIPPED = 0xFF63665B;


        private Skill skill;
        private ColorBlock bg;

        // note durability here is indicating the levels of upgrade to a skill,
        // name is because it uses durability indicator from bag ui
        private ColorBlock[] durability;

        public SkillButton(Skill skill) {

            super(skill);

            this.skill = skill;


            width = height = SLOT_SIZE;

            durability = new ColorBlock[Skill.MAX_LEVEL];

            if (skill != null && skill.name != null && skill.level > 0 && skill.level <= Skill.MAX_LEVEL) {
                for (int i = 0; i < skill.level; i++) {
                    durability[i] = new ColorBlock(2, 2, 0xFF00EE00);
                    add(durability[i]);
                }
                for (int i = skill.level; i < Skill.MAX_LEVEL; i++) {
                    durability[i] = new ColorBlock(2, 2, 0x4000EE00);
                    add(durability[i]);
                }
            }

            if (skill instanceof BranchSkill)
                bg.visible = false;
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


            if (skill != null && skill.name != null && skill.level > 0 && skill.level <= Skill.MAX_LEVEL) {
                for (int i = 0; i < Skill.MAX_LEVEL; i++) {
                    durability[i].x = x + width - 9 + i * 3;
                    durability[i].y = y + 3;

                }
            }

            super.layout();
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
            if (listener != null) {

                hide();
                listener.onSelect(skill);

            } else {

                WndSkills.this.add(new WndSkill(WndSkills.this, skill));

            }
        }

        @Override
        protected boolean onLongClick() {
            return true;
        }
    }

    public interface Listener {
        void onSelect(Skill skill);
    }
}
