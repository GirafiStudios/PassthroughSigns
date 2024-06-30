package com.girafi.passthroughsigns.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigurationHandler {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);

    public static class General {
        public ForgeConfigSpec.BooleanValue shouldWallSignBePassable;
        public ForgeConfigSpec.BooleanValue shouldBannerBePassable;
        public ForgeConfigSpec.BooleanValue shouldItemFrameBePassable;
        public ForgeConfigSpec.BooleanValue shouldPaintingsBePassable;
        public ForgeConfigSpec.BooleanValue turnOffItemRotation;

        General(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            shouldWallSignBePassable = builder
                    .comment("Whether to ignore wall signs when attached to an interactable block or not.")
                    .translation("passthroughsigns.configgui.shouldWallSignBePassable")
                    .define("Ignore wall signs", true);
            shouldBannerBePassable = builder
                    .comment("Whether to ignore banners when attached to an interactable block or not.")
                    .translation("passthroughsigns.configgui.shouldBannerBePassable")
                    .define("Ignore banners", true);
            shouldItemFrameBePassable = builder
                    .comment("Whether to ignore item frames when attached to an interactable block or not.")
                    .translation("passthroughsigns.configgui.shouldItemFrameBePassable")
                    .define("Ignore item frames", false);
            shouldPaintingsBePassable = builder
                    .comment("Whether to ignore paintings when attached to an interactable block or not.")
                    .translation("passthroughsigns.configgui.shouldPaintingsBePassable")
                    .define("Ignore paintings", true);
            turnOffItemRotation = builder
                    .comment("Disable default behaviour of item frames rotation display, when not sneaking (Recommended when ignoring item frames is enabled)")
                    .translation("passthroughsigns.configgui.turnOffItemRotation")
                    .define("Turn off item rotation", false);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec spec = BUILDER.build();
}