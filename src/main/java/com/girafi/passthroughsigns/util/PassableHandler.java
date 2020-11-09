package com.girafi.passthroughsigns.util;

import com.girafi.passthroughsigns.PassthroughSigns;
import com.girafi.passthroughsigns.api.IPassable;
import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
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
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = event.getPlayer();
        Block block = state.getBlock();
        if (block instanceof WallSignBlock && GENERAL.shouldWallSignBePassable.get() || block instanceof WallBannerBlock && GENERAL.shouldBannerBePassable.get() ||
                block instanceof IPassable && ((IPassable) block).canBePassed(world, pos, IPassable.EnumPassableType.WALL_BLOCK) ||
                PassthroughSignsAPI.BLOCK_PASSABLES.contains(block)) {
            Direction facingOpposite = Direction.NORTH.getOpposite();
            if (state.hasProperty(DirectionalBlock.FACING)) {
                facingOpposite = state.get(DirectionalBlock.FACING).getOpposite();
            } else if (state.hasProperty(HorizontalBlock.HORIZONTAL_FACING)) {
                facingOpposite = state.get(HorizontalBlock.HORIZONTAL_FACING).getOpposite();
            }

            if (block instanceof WallSignBlock) {
                if (IS_QUARK_LOADED && player.isCrouching() && GENERAL.shiftClickQuark.get()) {
                    rightClick(world, pos, player, event.getHand(), facingOpposite);
                } else if (!IS_QUARK_LOADED) {
                    rightClick(world, pos, player, event.getHand(), facingOpposite);
                }
            } else if (!player.isCrouching()) {
                rightClick(world, pos, player, event.getHand(), facingOpposite);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        PlayerEntity player = event.getPlayer();
        Entity entity = event.getTarget();

        if (entity instanceof ItemFrameEntity && GENERAL.shouldItemFrameBePassable.get() || entity instanceof PaintingEntity && GENERAL.shouldPaintingsBePassable.get() ||
                entity instanceof IPassable && ((IPassable) entity).canBePassed(world, pos, IPassable.EnumPassableType.HANGING_ENTITY) ||
                PassthroughSignsAPI.ENTITY_PASSABLES.contains(entity.getType())) {
            Direction facingOpposite = entity.getHorizontalFacing().getOpposite();

            if (!player.isCrouching()) {
                if (entity instanceof ItemFrameEntity && GENERAL.turnOffItemRotation.get() && world.getBlockState(pos.offset(facingOpposite)).hasTileEntity()) {
                    event.setCanceled(true);
                }
                rightClick(world, pos, player, event.getHand(), facingOpposite);
            }
        }
    }

    private static void rightClick(World world, BlockPos pos, PlayerEntity player, Hand hand, Direction facingOpposite) {
        if (hand == Hand.MAIN_HAND) {
            BlockPos posOffset = pos.add(facingOpposite.getXOffset(), facingOpposite.getYOffset(), facingOpposite.getZOffset());
            BlockState attachedState = world.getBlockState(posOffset);

            BlockState stateDown = world.getBlockState(pos.down());
            BlockRayTraceResult rayTrace = new BlockRayTraceResult(new Vector3d(posOffset.getX(), posOffset.getY(), posOffset.getZ()), facingOpposite, pos, false);
            if (!world.isAirBlock(pos.down()) && attachedState.getBlock().isAir(attachedState, world, pos)) {
                stateDown.getBlock().onBlockActivated(attachedState, world, pos.down(), player, hand, rayTrace);
            } else if (!attachedState.getBlock().isAir(attachedState, world, pos)) {
                attachedState.getBlock().onBlockActivated(attachedState, world, posOffset, player, hand, rayTrace);
            }
        }
    }
}