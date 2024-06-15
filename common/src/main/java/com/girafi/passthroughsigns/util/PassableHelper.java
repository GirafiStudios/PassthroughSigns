package com.girafi.passthroughsigns.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class PassableHelper {
    public static void rightClick(Level level, BlockPos pos, Player player, InteractionHand hand, Direction facingOpposite) {
        if (hand == InteractionHand.MAIN_HAND) {
            BlockPos posOffset = pos.offset(facingOpposite.getStepX(), facingOpposite.getStepY(), facingOpposite.getStepZ());
            BlockState attachedState = level.getBlockState(posOffset);

            BlockState stateDown = level.getBlockState(pos.below());
            BlockHitResult rayTrace = new BlockHitResult(new Vec3(posOffset.getX(), posOffset.getY(), posOffset.getZ()), facingOpposite, posOffset, false);
            if (!level.isEmptyBlock(pos.below()) && attachedState.isAir()) {
                stateDown.useWithoutItem(level, player, rayTrace);
            } else if (!attachedState.isAir()) {
                if (!level.isClientSide) {
                    attachedState.useWithoutItem(level, player, rayTrace);
                }
            }
        }
    }
}