package com.girafi.passthroughsigns;

import com.girafi.passthroughsigns.util.ConfigurationHandler;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.neoforged.fml.config.ModConfig;

public class PassthroughSigns implements ModInitializer {

    @Override
    public void onInitialize() {
        NeoForgeConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.COMMON, ConfigurationHandler.spec);
        CommonClass.init();
    }
}