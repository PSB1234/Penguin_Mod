package net.oshino.penguinmod.mixin;

import net.minecraft.entity.Entity;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/**
 * A Mixin that modifies the eye height calculation for PenguinEntity.
 * This ensures that the eye height dynamically adjusts when the penguin is sliding or swimming.
 */
@Mixin(Entity.class)
public class EyeHeightMixin {
    /**
     * Modifies the `getStandingEyeHeight` method to return a custom eye height for PenguinEntity.
     *
     * - Default eye height is 70% of the penguin's total height.
     * - If the penguin is sliding or swimming at a noticeable speed, the eye height is lowered to 30%.
     * - This adjustment ensures that the penguin's viewpoint aligns with its actual state.
     *
     * @param cir Callback that modifies the return value of `getStandingEyeHeight`.
     */
    @Inject(method = "getStandingEyeHeight", at = @At("RETURN"), cancellable = true)
    private void getDefaultEyeHeight(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof PenguinEntity penguin) { // Correct instanceof check
            double eyeOffset = 0.7; // Default eye level percentage of height
            if ((penguin.isSliding() && penguin.getVelocity().lengthSquared() > 0.02)|| penguin.isTouchingWater()) {
                eyeOffset = 0.3; // Lower eye level when sliding and swimming
            }
            // Adjust eye height based on current hitbox height
            cir.setReturnValue((float) (penguin.getHeight() * eyeOffset));
        }
    }
}
