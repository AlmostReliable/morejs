package com.almostreliable.morejs;

import com.almostreliable.morejs.features.villager.MerchantOfferCodecPatch;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BuildConfig.MOD_ID)
public class MoreJS {

    public static final Logger LOG = LogManager.getLogger(BuildConfig.MOD_NAME);
    public static final String DISABLED_TAG = "morejs$disabled";

    public MoreJS(IEventBus bus) {
        ForgeEventLoaders.load(bus);
        MerchantOfferCodecPatch.patch();
    }
}
