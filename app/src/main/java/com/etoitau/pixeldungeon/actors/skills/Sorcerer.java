package com.etoitau.pixeldungeon.actors.skills;


public class Sorcerer extends PassiveSkillB2 {


    {
        name = "Sorcerer";
        image = 34;
        tier = 2;
    }

    @Override
    protected boolean upgrade() {
        return true;
    }


    @Override
    public float wandDamageBonus() {
        return 1f + 0.1f * level;
    }

    @Override
    public String info() {
        return "Wands do more damage.\n"
                + costUpgradeInfo();
    }
}
