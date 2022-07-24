package com.girafi.passthroughsigns.util;

import com.girafi.passthroughsigns.PassthroughSigns;
import com.girafi.passthroughsigns.api.IPassable;
import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static com.girafi.passthroughsigns.util.ConfigurationHandler.GENERAL;

@EventBusSubscriber(modid = PassthroughSigns.MOD_ID)
public class PassableHandler {
    public static final boolean IS_QUARK_LOADED = ModList.get().isLoaded("quark");

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
                if (IS_QUARK_LOADED && player.isCrouching() && GENERAL.shiftClickQuark.get()) {
                    rightClick(level, pos, player, event.getHand(), facingOpposite);
                } else if (!IS_QUARK_LOADED) {
                    rightClick(level, pos, player, event.getHand(), facingOpposite);
                }
            } else if (!player.isCrouching()) {
                rightClick(level, pos, player, event.getHand(), facingOpposite);
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
                rightClick(level, pos, player, event.getHand(), facingOpposite);
            }
        }
    }

    private static void rightClick(Level level, BlockPos pos, Player player, InteractionHand hand, Direction facingOpposite) {
        if (hand == InteractionHand.MAIN_HAND) {
            BlockPos posOffset = pos.offset(facingOpposite.getStepX(), facingOpposite.getStepY(), facingOpposite.getStepZ());
            BlockState attachedState = level.getBlockState(posOffset);

            BlockState stateDown = level.getBlockState(pos.below());
            BlockHitResult rayTrace = new BlockHitResult(new Vec3(posOffset.getX(), posOffset.getY(), posOffset.getZ()), facingOpposite, pos, false);
            if (!level.isEmptyBlock(pos.below()) && attachedState.isAir()) {
                stateDown.getBlock().use(stateDown, level, pos.below(), player, hand, rayTrace);
            } else if (!attachedState.isAir()) {
                attachedState.getBlock().use(attachedState, level, posOffset, player, hand, rayTrace);
            }
        }
    }
}