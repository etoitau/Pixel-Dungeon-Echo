package com.etoitau.pixeldungeon.actors.skills;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.ui.StatusPane;

public class DoubleStab extends ActiveSkill1 {


    {
        name = "Double Strike";
        castText = "Too slow";
        image = 61;
        tier = 1;
        mana = 5;
    }


    @Override
    public boolean doubleStab() {
        if (!active || Dungeon.hero.MP < getManaCost())
            return false;

        castTextYell();
        Dungeon.hero.MP -= getManaCost();
        StatusPane.manaDropping += getManaCost();
        return true;

    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Attacks twice with equipped melee weapon.\n"
                + costUpgradeInfo();
    }
}
