package com.almostreliable.missingname;

import net.fabricmc.loader.api.FabricLoader;

public class MissingNamePlatformFabric implements MissingNamePlatform {

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
