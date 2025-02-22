package net.oshino.penguinmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class) // Target LivingEntity instead
public class EyeHeightMixin {
    @Inject(method = "getStandingEyeHeight", at = @At("RETURN"), cancellable = true)
    private void getDefaultEyeHeight(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof PenguinEntity penguin) { // Correct instanceof check
            double eyeOffset = 0.7; // Default eye level percentage of height
            if ((penguin.isSliding()|| penguin.isTouchingWater()) && penguin.getVelocity().lengthSquared() > 0.02) {
                eyeOffset = 0.3; // Lower eye level when sliding and swimming
            }
            // Adjust eye height based on current hitbox height
            cir.setReturnValue((float) (penguin.getHeight() * eyeOffset));
        }
    }
}
