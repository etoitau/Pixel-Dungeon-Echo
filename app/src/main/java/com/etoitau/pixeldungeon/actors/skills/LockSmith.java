package com.etoitau.pixeldungeon.actors.skills;

import com.watabau.utils.Random;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class LockSmith extends PassiveSkillA3 {


    {
        name = "Lock Smith";

        tier = 3;
        image = 51;
    }

    @Override
    public boolean disableTrap() {
        if (level < 1) { return false; }
        if (Random.Int(100) < 33 * level) {
            castText = "That was close!";
            castTextYell();
            return true;
        } else {
            castText = "Need to train in " + name + " more...";
            castTextYell();
            return false;
        }
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public String info() {
        return "33% per level chance to disable traps.\n"
                + costUpgradeInfo();
    }
}
