package com.girafi.passthroughsigns.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class PassthroughSignsAPI {
    public static IPassable passable;
    public static final Set<Block> BLOCK_PASSABLES = new HashSet<>();
    public static final Set<EntityType<?>> ENTITY_PASSABLES = new HashSet<>();

    /**
     * Can be used to make a block/entity based on its registry name from string passable
     * Example of use of intermod communication : InterModComms.sendTo("passthroughsigns", "registerPassable", "block/entity registry name");
     *
     * @param string the string of a blocks registry name or a entities class string
     */
    public static void setCanBePassed(String string) {
        ResourceLocation resourceLocation = new ResourceLocation(string);
        if (ForgeRegistries.ENTITIES.containsKey(resourceLocation)) {
            ENTITY_PASSABLES.add(ForgeRegistries.ENTITIES.getValue(resourceLocation));
        } else if (ForgeRegistries.BLOCKS.containsKey(resourceLocation)) {
            BLOCK_PASSABLES.add(ForgeRegistries.BLOCKS.getValue(resourceLocation));
        }
    }

    /**
     * Convenience method for making a block passable
     *
     * @param block the block
     **/
    public static void setCanBePassed(Block block) {
        BLOCK_PASSABLES.add(block);
    }

    /**
     * Convenience method for making a entity passable
     *
     * @param entityType the entity type
     **/
    public static void setCanBePassed(EntityType entityType) {
        ENTITY_PASSABLES.add(entityType);
    }
}