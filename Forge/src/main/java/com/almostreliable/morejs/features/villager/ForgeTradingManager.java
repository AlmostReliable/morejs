package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.MoreJS;

public class ForgeTradingManager extends TradingManager {

    public static final ForgeTradingManager INSTANCE = new ForgeTradingManager();

    private boolean readyToReload = false;

    public void reset() {
        tradesBackup = null;
        wandererTradesBackup = null;
        readyToReload = false;
    }

    public void start() {
        reset();
        readyToReload = true;
        reload();
    }

    @Override
    public void reload() {
        if (readyToReload) {
            super.reload();
            return;
        }

        MoreJS.LOG.debug("Villager trades are not ready to reload yet. Waiting for the server to start.");
    }
}
