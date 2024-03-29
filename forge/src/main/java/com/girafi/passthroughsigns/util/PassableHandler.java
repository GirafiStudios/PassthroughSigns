package com.girafi.passthroughsigns.util;

import com.girafi.passthroughsigns.Constants;
import com.girafi.passthroughsigns.api.IPassable;
import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static com.girafi.passthroughsigns.util.ConfigurationHandler.GENERAL;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class PassableHandler {

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        Block block = state.getBlock();
        if (block instanceof WallSignBlock && GENERAL.shouldWallSignBePassable.get() || block instanceof WallBannerBlock && GENERAL.shouldBannerBePassable.get() ||
                block instanceof IPassable && ((IPassable) block).canBePassed(level, pos, IPassable.EnumPassableType.WALL_BLOCK) ||
                PassthroughSignsAPI.BLOCK_PASSABLES.contains(block)) {
            Direction facingOpposite = Direction.NORTH.getOpposite();
            if (state.hasProperty(DirectionalBlock.FACING)) {
                facingOpposite = state.getValue(DirectionalBlock.FACING).getOpposite();
            } else if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
                facingOpposite = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
            }

            if (block instanceof WallSignBlock) {
                if (!player.isCrouching()) {
                    PassableHelper.rightClick(level, pos, player, event.getHand(), facingOpposite);
                    event.setCanceled(true);
                }
            } else if (!player.isCrouching()) {
                PassableHelper.rightClick(level, pos, player, event.getHand(), facingOpposite);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        Entity entity = event.getTarget();

        if (entity instanceof ItemFrame && GENERAL.shouldItemFrameBePassable.get() || entity instanceof Painting && GENERAL.shouldPaintingsBePassable.get() ||
                entity instanceof IPassable && ((IPassable) entity).canBePassed(level, pos, IPassable.EnumPassableType.HANGING_ENTITY) ||
                PassthroughSignsAPI.ENTITY_PASSABLES.contains(entity.getType())) {
            Direction facingOpposite = entity.getDirection().getOpposite();

            if (!player.isCrouching()) {
                if (entity instanceof ItemFrame && GENERAL.turnOffItemRotation.get() && level.getBlockState(pos.relative(facingOpposite)).hasBlockEntity()) {
                    event.setCanceled(true);
                }
                PassableHelper.rightClick(level, pos, player, event.getHand(), facingOpposite);
            }
        }
    }
}