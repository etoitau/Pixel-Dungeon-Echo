package com.etoitau.pixeldungeon.actors.skills;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.watabau.utils.Random;

/**
 * Created by Moussa on 25-Jan-17.
 */
public class MercThiefSkillA extends Venom {
    {
        tag = "mercA";
    }

    @Override
    protected boolean upgrade()
    {
        return false;
    }

    @Override
    public boolean venomousAttack()
    {
        if(Random.Int(100) < 5 * level + 15)
        {
            return true;
        }
        return false;
    }

    @Override
    public void castTextYell()
    {
        if(castText != "")
            Dungeon.hero.hiredMerc.sprite.showStatus(CharSprite.NEUTRAL, castText);
    }


    @Override
    public String info()
    {
        return "Chance to poison target.\n";
    }
}
