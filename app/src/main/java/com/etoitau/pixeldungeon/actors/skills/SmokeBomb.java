package com.etoitau.pixeldungeon.actors.skills;


import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Invisibility;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.particles.ElmoParticle;
import com.etoitau.pixeldungeon.ui.StatusPane;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class SmokeBomb extends ActiveSkill1{


    {
        name = "Smoke Bomb";
        castText = "Now you see me..";
        tier = 1;
        image = 65;
        mana = 6;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = new ArrayList<String>();
        if(level > 0 && hero.MP >= getManaCost())
            actions.add(AC_CAST);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if(action == Skill.AC_CAST)
        {
                Buff.affect(hero, Invisibility.class, Invisibility.DURATION * (0.5f * level));
                CellEmitter.get(hero.pos).burst(ElmoParticle.FACTORY, 4);
                hero.MP -= getManaCost();
                StatusPane.manaDropping += getManaCost();
                castTextYell();
                Dungeon.hero.heroSkills.lastUsed = this;
                hero.spend( TIME_TO_USE );
                hero.busy();
                hero.sprite.operate( hero.pos );
        }
    }

    @Override
    public int getManaCost()
    {
        return (int)Math.ceil(mana * (1 + 0.55 * level));
    }

    @Override
    protected boolean upgrade()
    {
        return true;
    }


    @Override
    public String info()
    {
        return "You become invisible.\n"
                + costUpgradeInfo();
    }

}
