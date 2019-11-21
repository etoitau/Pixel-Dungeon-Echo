package com.etoitau.pixeldungeon.actors.skills;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.particles.ElmoParticle;
import com.etoitau.pixeldungeon.items.weapon.missiles.Arrow;
import com.etoitau.pixeldungeon.ui.StatusPane;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class AimedShot extends ActiveSkill1{


    boolean cast;
    {
        name = "Aimed Shot";
        castText = "I see him";
        image = 93;
        tier = 1;
        mana = 3;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if(action == Skill.AC_ACTIVATE)
        {
            hero.heroSkills.active2.active = false; // Disable Double shot
            hero.heroSkills.active3.active = false; // Disable Bombvoyage
        }
    }

    //@Override
    public float rangedDamageModifier()
    {
        float toReturn = 1f;
        toReturn += cast ? 0.2f * level : 0;
        cast = false;
        return toReturn;
    }

    //@Override
    public boolean aimedShot()
    {
        if(active == false || Dungeon.hero.MP < getManaCost())
        {
            cast = false;
            return false;
        }

        cast = true;

        castTextYell();
        Dungeon.hero.MP -= getManaCost();
        StatusPane.manaDropping += getManaCost();
        return true;
    }

    @Override
    protected boolean upgrade()
    {
        return true;
    }


    @Override
    public String info()
    {
        return "Stronger ranged attack that never misses.\n"
                + costUpgradeInfo();
    }
}
