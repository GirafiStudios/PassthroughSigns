package com.girafi.passthroughsigns;

import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import com.girafi.passthroughsigns.util.ConfigurationHandler;
import fuzs.forgeconfigapiport.forge.api.neoforge.v4.NeoForgeConfigRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class PassthroughSigns {

    public PassthroughSigns() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::handleIMCMessages);
        NeoForgeConfigRegistry.INSTANCE.register(ModConfig.Type.COMMON, ConfigurationHandler.spec);
        CommonClass.init();
    }

    public void handleIMCMessages(InterModProcessEvent event) {
        event.getIMCStream("registerPassable"::equals).forEach(m -> PassthroughSignsAPI.setCanBePassed(m.method()));
    }
}