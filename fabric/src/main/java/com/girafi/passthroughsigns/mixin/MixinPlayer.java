package com.girafi.passthroughsigns.mixin;

import com.girafi.passthroughsigns.util.PassableHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer {

    @Inject(at = @At("HEAD"), method = "interactOn(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;")
    private void interactOn(Entity entity, InteractionHand hand, CallbackInfo info) {
        Level level = entity.level();
        BlockPos pos = entity.blockPosition();
        Player player = (Player) (Object) this; //TODO, does this work?

        PassableHandler.onEntityInteract(level, pos, player, entity, hand, info);
    }
}