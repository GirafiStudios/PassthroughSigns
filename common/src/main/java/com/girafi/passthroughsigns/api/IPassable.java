package com.girafi.passthroughsigns.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IPassable {
    /**
     * Return true if this block/entity should be passable.
     *
     * @param level the level
     * @param pos   the position of the block/entity
     * @param type  the EnumPassableType for the block/entity
     * @return whether the block/entity can be passed or not
     */
    boolean canBePassed(Level level, BlockPos pos, EnumPassableType type);

    enum EnumPassableType {
        /**
         * For blocks similar to wall signs & banners.
         */
        WALL_BLOCK,

        /**
         * For entities similar to item frames, paintings etc.
         */
        HANGING_ENTITY
    }
}