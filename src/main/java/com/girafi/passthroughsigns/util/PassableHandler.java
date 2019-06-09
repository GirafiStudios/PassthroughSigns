package com.girafi.passthroughsigns.util;

import com.girafi.passthroughsigns.PassthroughSigns;
import com.girafi.passthroughsigns.api.IPassable;
import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
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
        PlayerEntity player = event.getEntityPlayer();
        Block block = state.getBlock();

        if (block instanceof WallSignBlock && GENERAL.shouldWallSignBePassable.get() || block instanceof WallBannerBlock && GENERAL.shouldBannerBePassable.get() ||
                block instanceof IPassable && ((IPassable) block).canBePassed(world, pos, IPassable.EnumPassableType.WALL_BLOCK) ||
                PassthroughSignsAPI.BLOCK_PASSABLES.contains(block)) {
            Direction facingOpposite = Direction.NORTH.getOpposite();
            if (state.has(DirectionalBlock.FACING)) {
                facingOpposite = state.get(DirectionalBlock.FACING).getOpposite();
            } else if (state.has(HorizontalBlock.HORIZONTAL_FACING)) {
                facingOpposite = state.get(HorizontalBlock.HORIZONTAL_FACING).getOpposite();
            }

            ItemStack heldStack = player.getHeldItemMainhand();
            if (heldStack.getItem() instanceof BlockItem) {
                event.setUseItem(Event.Result.DENY);
            }

            if (block instanceof WallSignBlock) {
                if (IS_QUARK_LOADED && player.isSneaking() && GENERAL.shiftClickQuark.get()) {
                    rightClick(world, pos, player, event.getHand(), facingOpposite);
                } else if (!IS_QUARK_LOADED) {
                    rightClick(world, pos, player, event.getHand(), facingOpposite);
                }
            } else if (!player.isSneaking()) {
                rightClick(world, pos, player, event.getHand(), facingOpposite);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        PlayerEntity player = event.getEntityPlayer();
        Entity entity = event.getTarget();

        if (entity instanceof ItemFrameEntity && GENERAL.shouldItemFrameBePassable.get() || entity instanceof PaintingEntity && GENERAL.shouldPaintingsBePassable.get() ||
                entity instanceof IPassable && ((IPassable) entity).canBePassed(world, pos, IPassable.EnumPassableType.HANGING_ENTITY) ||
                PassthroughSignsAPI.ENTITY_PASSABLES.contains(entity.getType())) {
            Direction facingOpposite = entity.getHorizontalFacing().getOpposite();

            if (!player.isSneaking()) {
                if (entity instanceof ItemFrameEntity && GENERAL.turnOffItemRotation.get()) {
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
            if (!world.isAirBlock(pos.down()) && attachedState.getBlock().isAir(attachedState, world, pos)) {
                stateDown.getBlock().onBlockActivated(attachedState, world, pos.down(), player, hand, null);
            } else if (!attachedState.getBlock().isAir(attachedState, world, pos)) {
                attachedState.getBlock().onBlockActivated(attachedState, world, posOffset, player, hand, null);
            }
        }
    }
}