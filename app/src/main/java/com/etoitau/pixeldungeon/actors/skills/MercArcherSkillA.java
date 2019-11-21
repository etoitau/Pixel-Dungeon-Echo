package com.etoitau.pixeldungeon.actors.skills;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.sprites.CharSprite;
import com.watabau.utils.Random;

/**
 * Created by Moussa on 25-Jan-17.
 */
public class MercArcherSkillA extends KneeShot {
    {
        tag = "mercA";
    }

    @Override
    protected boolean upgrade()
    {
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
        return "Aims for weak spots crippling targets.\n";
    }
}
