package com.etoitau.pixeldungeon.actors.skills;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.hero.Hero;

import java.util.ArrayList;

/**
 * Created by Moussa on 22-Jan-17.
 */
public class ActiveSkill extends Skill {

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<String>();
        if (!active && level > 0)
            actions.add(AC_ACTIVATE);
        else if (level > 0)
            actions.add(AC_DEACTIVATE);

        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        Dungeon.hero.heroSkills.lastUsed = this;
        if (action.equals(Skill.AC_ACTIVATE)) {
            active = true;
        } else if (action.equals(Skill.AC_DEACTIVATE)) {
            active = false;
        }
    }
}
