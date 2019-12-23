package com.etoitau.pixeldungeon;

import com.etoitau.pixeldungeon.actors.Actor;
import com.etoitau.pixeldungeon.actors.buffs.Buff;
import com.watabau.utils.Bundlable;
import com.watabau.utils.Bundle;

import java.util.LinkedList;
import java.util.Queue;

public class TimeMachine implements Bundlable {
    private static final float TOCK = 10f;
    private static final int MAX_SAVES = 2;



    private static final String KEY_TIME_MACHINE = "timeMachine";
    private static final String KEY_N_SNAPSHOTS = "numberOfSnapshots";
    private static final String KEY_SNAPSHOT_PREFIX = "snapshot";
    private static final String KEY_GAME = "game";
    private static final String KEY_LEVEL = "level";

    private static LinkedList<Bundle> snapshots = new LinkedList<>();

    public static boolean isFull() {
        return snapshots.size() >= MAX_SAVES;
    }

    private static void takeSnapshot() {
        while (isFull()) {
            snapshots.poll();
        }
        Bundle snapshot = new Bundle();
        snapshot.put(KEY_GAME, Dungeon.saveGameToBundle());
        snapshot.put(KEY_LEVEL, Dungeon.level);
        snapshots.offer(snapshot);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        snapshots.clear();
        Bundle timeMachine = bundle.getBundle(KEY_TIME_MACHINE);
        int nSnapshots = timeMachine.getInt(KEY_N_SNAPSHOTS);
        for (int i = 0; i < nSnapshots; i++) {
            snapshots.offer(timeMachine.getBundle(KEY_SNAPSHOT_PREFIX + i));
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        Bundle timeMachine = new Bundle();
        int i = 0;
        for (Bundle snapshot: snapshots) {
            timeMachine.put(KEY_SNAPSHOT_PREFIX + i, snapshot);
            i++;
        }
        timeMachine.put(KEY_N_SNAPSHOTS, i);
        bundle.put(KEY_TIME_MACHINE, timeMachine);
    }


    public class saveTimer extends Actor {

        @Override
        protected boolean act() {
            // save snapshot
            takeSnapshot();
            // postpone till next
            spend(TOCK);
            return true;
        }
    }

}
