package com.girafi.passthroughsigns;

import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import com.girafi.passthroughsigns.util.ConfigurationHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;

@Mod(Constants.MOD_ID)
public class PassthroughSigns {

    public PassthroughSigns(ModContainer modContainer, IEventBus modBus) {
        modBus.addListener(this::handleIMCMessages);
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigurationHandler.spec);
        CommonClass.init();
    }

    public void handleIMCMessages(InterModProcessEvent event) {
        event.getIMCStream("registerPassable"::equals).forEach(m -> PassthroughSignsAPI.setCanBePassed(m.method()));
    }
}