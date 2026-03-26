package com.girafi.passthroughsigns;

import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import com.girafi.passthroughsigns.util.ConfigurationHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Constants.MOD_ID)
public class PassthroughSigns {

    public PassthroughSigns(ModContainer modContainer, IEventBus modBus) {
        modBus.addListener(this::handleIMCMessages);
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigurationHandler.spec);
        CommonClass.init();

        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }

    public void handleIMCMessages(InterModProcessEvent event) {
        event.getIMCStream("registerPassable"::equals).forEach(m -> PassthroughSignsAPI.setCanBePassed(m.method()));
    }
}