package com.etoitau.pixeldungeon.ui;

import com.etoitau.pixeldungeon.actors.Actor;

public class HealthIndicatorTimer extends Actor {

    public boolean hasTime = false;

    @Override
    protected boolean act() {
        hasTime = false;
        diactivate();
        return true;
    }

    public void setTimer(float time) {
        postpone(time);
        hasTime = true;
    }

}
