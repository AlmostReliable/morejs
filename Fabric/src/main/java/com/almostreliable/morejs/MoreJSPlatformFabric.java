package com.almostreliable.morejs;

import net.fabricmc.loader.api.FabricLoader;

public class MoreJSPlatformFabric implements MoreJSPlatform {

    @Override
    public Platform getPlatform() {
        return Platform.Fabric;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
