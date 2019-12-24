package com.etoitau.pixeldungeon.items;

import com.etoitau.pixeldungeon.TimeMachine;
import com.etoitau.pixeldungeon.actors.Actor;

public class AnkhTimer extends Actor {
    @Override
    protected boolean act() {
        // save snapshot
        TimeMachine.takeSnapshot();
        // postpone till next
        spend(TimeMachine.TOCK);
        return true;
    }
}
