package com.almostreliable.missingname;

import com.almostreliable.missingname.core.ReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(BuildConfig.MOD_ID)
public class MissingNameModForge {

    public MissingNameModForge() {
        MinecraftForge.EVENT_BUS.addListener(this::reloadListener);
    }

    private void reloadListener(AddReloadListenerEvent event) {
        event.addListener(new ReloadListener());
    }
}
