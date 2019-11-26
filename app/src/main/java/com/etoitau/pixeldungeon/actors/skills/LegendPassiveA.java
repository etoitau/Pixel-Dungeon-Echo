package com.etoitau.pixeldungeon.actors.skills;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.hero.Hero;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class LegendPassiveA extends BranchSkill {


    {
        name = "Hatsune";
        image = 112;
        level = 0;
    }

    @Override
    public String info() {
        return "Hatsune as with her ancestors overflow with spiritual energy.";
    }
}
