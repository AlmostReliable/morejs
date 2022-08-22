package com.almostreliable.morejs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

public class MoreJS {

    public static final Logger LOG = LogManager.getLogger(BuildConfig.MOD_NAME + "/enchanting");
    public static final MoreJSPlatform PLATFORM = load();

    static MoreJSPlatform load() {
        Class<MoreJSPlatform> clazz = MoreJSPlatform.class;
        final MoreJSPlatform loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        MoreJS.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
