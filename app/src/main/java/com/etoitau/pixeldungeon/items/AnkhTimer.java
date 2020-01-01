package com.etoitau.pixeldungeon.items;

import com.etoitau.pixeldungeon.TimeMachine;
import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.buffs.Buff;

public class AnkhTimer extends Buff {
    @Override
    public boolean act() {
        // save snapshot
        TimeMachine.takeSnapshot();
        // postpone till next
        spend(TimeMachine.TOCK);
        return true;
    }
}
