/*
 * Pixel Dungeon Echo
 * Copyright (C) 2019 Kyle Chatman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.etoitau.pixeldungeon;

import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.etoitau.pixeldungeon.actors.hero.Belongings;
import com.etoitau.pixeldungeon.actors.hero.Hero;
import com.etoitau.pixeldungeon.items.Ankh;
import com.etoitau.pixeldungeon.items.AnkhCracked;
import com.etoitau.pixeldungeon.items.AnkhTimer;
import com.etoitau.pixeldungeon.levels.Level;
import com.watabau.utils.Bundlable;
import com.watabau.utils.Bundle;

import java.util.LinkedList;
import java.util.Queue;

public class TimeMachine {
    public static final float TOCK = 10f;
    private static final int MAX_SAVES = 2;
    private static AnkhTimer timer;

    public static boolean isOn = false;


    private static final String KEY_IS_ON = "isOn";
    private static final String KEY_TIME_MACHINE = "timeMachine";
    private static final String KEY_N_SNAPSHOTS = "numberOfSnapshots";
    private static final String KEY_SNAPSHOT_PREFIX = "snapshot";
    private static final String KEY_GAME = "game";
    private static final String KEY_LEVEL = "level";

    private static LinkedList<Bundle> snapshots = new LinkedList<>();

    public static void setTimer() {
        if (!isOn) {
            // add timer if not already added
            AnkhTimer foundTimer = Dungeon.hero.buff(AnkhTimer.class);
            if (foundTimer == null) {
                timer = new AnkhTimer();
                timer.attachTo(Dungeon.hero);
            } else {
                timer = foundTimer;
            }

            isOn = true;
        }
    }

    public static void stopTimer() {
        timer.detach();
        isOn = false;
    }

    public static boolean isFull() {
        return snapshots.size() >= MAX_SAVES;
    }

    public static void takeSnapshot() {
        while (isFull()) {
            snapshots.poll();
        }
        Bundle snapshot = new Bundle();
        snapshot.put(KEY_GAME, Dungeon.saveGameToBundle(false));
        snapshot.put(KEY_LEVEL, Dungeon.level);
        snapshots.offer(snapshot);
    }

    public static void goBack() {

        // get oldest snapshot, should be 1-2 x TOCK
        Bundle bundle = snapshots.getFirst();
        if (bundle == null) { return; }

        // load game from snapshot
        Actor.clear();
        Bundle gameBundle = bundle.getBundle(KEY_GAME);
        Dungeon.loadGameFromBundle(gameBundle, true, false);
        Dungeon.level = (Level) bundle.get(KEY_LEVEL);

        // find ankh to use
        Ankh ankhToUse = Dungeon.hero.belongings.getItem(AnkhCracked.class);
        if (ankhToUse == null) {
            ankhToUse = Dungeon.hero.belongings.getItem(Ankh.class);
        }

        // use the ankh
        if (ankhToUse != null) {
            ankhToUse.usedTimeMachine();
        }

        // check if have any ankh left, if not, shut down
        Ankh foundAnkh = Dungeon.hero.belongings.getItem(Ankh.class);
        if (foundAnkh == null) {
            foundAnkh = Dungeon.hero.belongings.getItem(AnkhCracked.class);
        }
        if (foundAnkh == null) {
            stopTimer();
        } else {
            setTimer();
        }

        // restart snapshot collection
        snapshots.clear();
    }

    public static void restore(Bundle bundle) {
        reset();

        if (!bundle.contains(KEY_TIME_MACHINE)) {
            isOn = false;
            return;
        }

        Bundle timeMachine = bundle.getBundle(KEY_TIME_MACHINE);

        isOn = timeMachine.getBoolean(KEY_IS_ON);

        if (!isOn) {
            return;
        }

        int nSnapshots = timeMachine.getInt(KEY_N_SNAPSHOTS);
        for (int i = 0; i < nSnapshots; i++) {
            snapshots.offer(timeMachine.getBundle(KEY_SNAPSHOT_PREFIX + i));
        }
    }

    public static void save(Bundle bundle) {
        Bundle timeMachine = new Bundle();
        timeMachine.put(KEY_IS_ON, isOn);

        if (!isOn) { return; }

        int i = 0;
        for (Bundle snapshot: snapshots) {
            timeMachine.put(KEY_SNAPSHOT_PREFIX + i, snapshot);
            i++;
        }
        timeMachine.put(KEY_N_SNAPSHOTS, i);
        bundle.put(KEY_TIME_MACHINE, timeMachine);
    }

    public static void reset() {
        snapshots.clear();
        isOn = false;
    }

}
