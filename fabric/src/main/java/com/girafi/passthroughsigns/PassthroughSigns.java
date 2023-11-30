package com.girafi.passthroughsigns;

import net.fabricmc.api.ModInitializer;

public class PassthroughSigns implements ModInitializer {
    
    @Override
    public void onInitialize() {

        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
    }
}