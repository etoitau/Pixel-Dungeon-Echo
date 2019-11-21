package com.etoitau.pixeldungeon.actors.hero;

import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.buffs.Hunger;
import com.etoitau.pixeldungeon.actors.buffs.ManaRegeneration;
import com.etoitau.pixeldungeon.actors.buffs.Regeneration;
import com.etoitau.pixeldungeon.items.Item;
import com.etoitau.pixeldungeon.items.armor.Armor;
import com.etoitau.pixeldungeon.items.armor.PlateArmor;
import com.etoitau.pixeldungeon.items.wands.WandOfMagicCasting;
import com.etoitau.pixeldungeon.items.weapon.Weapon;
import com.etoitau.pixeldungeon.items.weapon.melee.Longsword;
import com.etoitau.pixeldungeon.levels.MovieLevel;
import com.etoitau.pixeldungeon.scenes.MissionScene;
import com.watabau.utils.Bundle;

/**
 * Created by Moussa on 04-Feb-17.
 */
public class Legend extends Hero {

    {
        heroClass = HeroClass.HATSUNE;
    }


    @Override
    public boolean act() {
            super.act();

            if(MissionScene.scenePause == true)
            {
                spendAndNext( 1f );
            }

        return false;
    }

    @Override
    public boolean isStarving() {
        return false;
    }

    @Override
    public void live() {
        Buff.affect(this, ManaRegeneration.class);
        Buff.affect(this, ManaRegeneration.class);
        Buff.affect(this, ManaRegeneration.class);

        Buff.affect( this, Regeneration.class );
        Buff.affect( this, Regeneration.class );

        lvl = 100;
        HP = HT = 100;
        STR = 25;
        MP = MMP = 100;
        attackSkill = 40;
        defenseSkill = 25;
        Item tmp = new Longsword().identify();
        belongings.weapon = (Weapon)tmp;
        tmp = new PlateArmor().identify();
        belongings.armor = (Armor)tmp;
    }

    @Override
    public void die(Object reason)
    {
        super.die(reason);
        MissionScene.scenePause = true;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {



    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

    }
}
