package com.girafi.passthroughsigns.util;

import com.girafi.passthroughsigns.api.IPassable;
import com.girafi.passthroughsigns.api.PassthroughSignsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.girafi.passthroughsigns.util.ConfigurationHandler.GENERAL;

public class PassableHandler {

    public static void onPlayerInteract(Level level, BlockPos pos, BlockState state, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
        Block block = state.getBlock();
        ItemStack heldStack = player.getMainHandItem();
        if ((block instanceof WallSignBlock && GENERAL.shouldWallSignBePassable.get() || block instanceof WallBannerBlock && GENERAL.shouldBannerBePassable.get() ||
                block instanceof IPassable && ((IPassable) block).canBePassed(level, pos, IPassable.EnumPassableType.WALL_BLOCK) ||
                PassthroughSignsAPI.BLOCK_PASSABLES.contains(block)) && !(heldStack.getItem() instanceof DyeItem)) {
            Direction facingOpposite = Direction.NORTH.getOpposite();
            if (state.hasProperty(DirectionalBlock.FACING)) {
                facingOpposite = state.getValue(DirectionalBlock.FACING).getOpposite();
            } else if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
                facingOpposite = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
            }

            if (block instanceof WallSignBlock) {
                if (!player.isCrouching()) {
                    PassableHelper.rightClick(level, pos, player, hand, facingOpposite);
                    callbackInfo.setReturnValue(InteractionResult.FAIL);
                }
            } else if (!player.isCrouching()) {
                PassableHelper.rightClick(level, pos, player, hand, facingOpposite);
                callbackInfo.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    public static void onEntityInteract(Level level, BlockPos pos, Player player, Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
        if (entity instanceof ItemFrame && GENERAL.shouldItemFrameBePassable.get() || entity instanceof Painting && GENERAL.shouldPaintingsBePassable.get() ||
                entity instanceof IPassable && ((IPassable) entity).canBePassed(level, pos, IPassable.EnumPassableType.HANGING_ENTITY) ||
                PassthroughSignsAPI.ENTITY_PASSABLES.contains(entity.getType())) {
            Direction facingOpposite = entity.getDirection().getOpposite();

            if (!player.isCrouching()) {
                if (entity instanceof ItemFrame && GENERAL.turnOffItemRotation.get() && level.getBlockState(pos.relative(facingOpposite)).hasBlockEntity()) {
                    callbackInfo.setReturnValue(InteractionResult.FAIL);
                }
                PassableHelper.rightClick(level, pos, player, hand, facingOpposite);
                callbackInfo.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }
}