package net.oshino.penguinmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
/**
 * A Mixin that modifies the player's attack behavior when hitting a PenguinEntity.
 * - Detects when a player attacks a penguin.
 * - Calculates the direction of the attack relative to the penguin.
 * - Sets a flag in the PenguinEntity indicating that it was hit by a player.
 */
@Mixin(PlayerEntity.class)
public class PlayerAttackMixin {
    /**
     * Modifies the `attack` method of PlayerEntity to detect when a player attacks a penguin.
     * - Retrieves the target entity at the start of the attack method.
     * - If the target is a PenguinEntity, calculates the attack direction from the player to the penguin.
     * - Stores this direction inside the PenguinEntity for potential use in knockback effects or animations.
     *
     * @param target The entity being attacked.
     * @return The target entity, unchanged.
     */
    @ModifyVariable(method = "attack", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Entity onPlayerAttack(Entity target) {  // Ensure the variable matches the method's parameter
        if (target instanceof PenguinEntity) {  // Replace with your Penguin class
            PlayerEntity player = (PlayerEntity) (Object) this;

            Vec3d playerPos = player.getPos();
            Vec3d penguinPos = target.getPos();

            Vec3d direction = penguinPos.subtract(playerPos).normalize();
            ((PenguinEntity) target).setHitByPlayerDirection((direction).toVector3f());
            ((PenguinEntity) target).setHitByPlayer(true);
        }
        return target;
    }
}

