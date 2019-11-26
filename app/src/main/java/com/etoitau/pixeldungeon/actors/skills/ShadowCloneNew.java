package com.etoitau.pixeldungeon.actors.skills;


import com.watabau.noosa.tweeners.AlphaTweener;
import com.etoitau.pixeldungeon.Dungeon;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.actors.mobs.npcs.SummonedPet;
import com.etoitau.pixeldungeon.effects.CellEmitter;
import com.etoitau.pixeldungeon.effects.particles.ElmoParticle;
import com.etoitau.pixeldungeon.items.wands.WandOfBlink;
import com.etoitau.pixeldungeon.levels.Level;
import com.etoitau.pixeldungeon.scenes.GameScene;
import com.etoitau.pixeldungeon.sprites.MirrorSprite;
import com.etoitau.pixeldungeon.ui.StatusPane;
import com.watabau.utils.Random;

import java.util.ArrayList;

/**
 * Created by Moussa on 20-Jan-17.
 */
public class ShadowCloneNew extends ShadowClone {

    {
        castText = "Shadow clone";
        mana = 6;
    }


    @Override
    public void execute(Hero hero, String action) {
        if (action == Skill.AC_CAST) {
            ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

            for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
                int p = hero.pos + Level.NEIGHBOURS8[i];
                if (p < 0 || p >= Level.passable.length)
                    continue;
                if (Actor.findChar(p) == null && (Level.passable[p] || Level.avoid[p])) {
                    respawnPoints.add(p);
                }
            }

            int nImages = 1;
            while (nImages > 0 && respawnPoints.size() > 0) {
                int index = Random.index(respawnPoints);

                SummonedPet minion = new SummonedPet(MirrorSprite.class);
                minion.name = "Shadow Clone";
                minion.screams = false;
                minion.HT = 7 + 5 * level;
                minion.HP = 7 + 5 * level;
                minion.defenseSkill = (int) (Dungeon.hero.defenseSkill(Dungeon.hero) * ((1f + level) / 4f));
                GameScene.add(minion);
                WandOfBlink.appear(minion, respawnPoints.get(index));
                minion.setLevel(level);
                ((MirrorSprite) minion.sprite).updateArmor(level);
                minion.sprite.alpha(0);
                minion.sprite.parent.add(new AlphaTweener(minion.sprite, 1, 0.15f));
                CellEmitter.get(minion.pos).burst(ElmoParticle.FACTORY, 4);

                nImages--;
            }

            hero.MP -= getManaCost();
            StatusPane.manaDropping += getManaCost();
            castTextYell();
            Dungeon.hero.heroSkills.lastUsed = this;
            hero.spend(TIME_TO_USE);
            hero.busy();
            hero.sprite.operate(hero.pos);
        }
    }
}
