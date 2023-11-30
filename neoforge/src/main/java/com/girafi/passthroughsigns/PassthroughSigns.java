package com.girafi.passthroughsigns;

import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import com.girafi.passthroughsigns.util.ConfigurationHandler;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class PassthroughSigns {

    public PassthroughSigns() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::handleIMCMessages);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigurationHandler.spec);
        CommonClass.init();
    }

    public void handleIMCMessages(InterModProcessEvent event) {
        event.getIMCStream("registerPassable"::equals).forEach(m -> PassthroughSignsAPI.setCanBePassed(m.getMethod()));
    }
}