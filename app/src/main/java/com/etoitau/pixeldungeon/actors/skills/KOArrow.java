package com.etoitau.pixeldungeon.actors.skills;


import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.ui.StatusPane;
import com.watabau.utils.Random;

/**
 * Created by Moussa on 20-Jan-17.
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

        if (Random.Int(100) < 10 * level) {
            castTextYell();
            return true;
        }
        return false;
    }


    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "A chance to knock out a target with a arrow attacks.\n"
                + costUpgradeInfo();
    }

}
