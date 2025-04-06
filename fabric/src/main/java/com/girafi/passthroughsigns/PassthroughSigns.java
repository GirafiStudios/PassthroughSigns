package com.girafi.passthroughsigns;

import com.girafi.passthroughsigns.util.ConfigurationHandler;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class PassthroughSigns implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.COMMON, ConfigurationHandler.spec);
        CommonClass.init();
    }
}