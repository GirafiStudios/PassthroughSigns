package com.girafi.passthroughsigns;

import com.girafi.passthroughsigns.util.ConfigurationHandler;
import fuzs.forgeconfigapiport.api.config.v3.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class PassthroughSigns implements ModInitializer {

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.COMMON, ConfigurationHandler.spec);
        CommonClass.init();
    }
}