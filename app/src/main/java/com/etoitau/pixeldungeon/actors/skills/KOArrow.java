package com.etoitau.pixeldungeon.actors.skills;


import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.ui.StatusPane;
import com.watabau.utils.Random;

/**
 * Created by Moussa on 20-Jan-17.
 *
 * Bonus skill for rogue
 */
public class KOArrow extends PassiveSkillB2 {


    {
        name = "KO Arrow";
        castText = "Go to sleep";
        tier = 2;
        image = 60;
    }

    @Override
    public boolean goToSleep() {
        if (level < 1) { return false; }
        return (Random.Int(100) < 10 * level);
    }


    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "Chance to put a target to sleep with an arrow attack.\n"
                + costUpgradeInfo();
    }

}
