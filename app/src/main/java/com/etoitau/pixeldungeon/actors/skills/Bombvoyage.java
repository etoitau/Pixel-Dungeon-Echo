package com.etoitau.pixeldungeon.actors.skills;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.ui.StatusPane;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class Bombvoyage extends ActiveSkill3{


    {
        name = "Bombvoyage";
        castText = "Bombvoyage";
        image = 91;
        tier = 3;
        mana = 15;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if(action == Skill.AC_ACTIVATE)
        {
            hero.heroSkills.active2.active = false; // Disable Double Arrow
        }
    }

    @Override
    public boolean arrowToBomb()
    {
        if(active == false || Dungeon.hero.MP < getManaCost())
            return false;
        else
        {
            castTextYell();
            Dungeon.hero.MP -= getManaCost();
            StatusPane.manaDropping += getManaCost();
            return true;
        }
    }

    @Override
    public int getManaCost()
    {
        return mana - level * 2;
    }

    @Override
    protected boolean upgrade()
    {
        return true;
    }


    @Override
    public String info()
    {
        return "Attaches a bomb to a standard arrow.\n"
                + costUpgradeInfo();
    }
}
