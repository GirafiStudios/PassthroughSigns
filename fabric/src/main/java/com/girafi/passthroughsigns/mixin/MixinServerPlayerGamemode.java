package com.girafi.passthroughsigns.mixin;

import com.girafi.passthroughsigns.Constants;
import com.girafi.passthroughsigns.util.PassableHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGamemode {
    
    @Inject(at = @At("HEAD"), method = "useItemOn(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;")
    private InteractionResult useItemOn(ServerPlayer serverPlayer, Level level, ItemStack stack, InteractionHand hand, BlockHitResult blockHitResult, CallbackInfo info) {
        BlockPos pos = blockHitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        PassableHandler.onPlayerInteract(level, pos, state, serverPlayer, hand, info);

        Constants.LOG.info("This line is printed by an example mod mixin from Fabric!");
        Constants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());

        return InteractionResult.PASS;
    }
}