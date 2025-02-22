package net.oshino.penguinmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(PlayerEntity.class)
public class PlayerAttackMixin {
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

