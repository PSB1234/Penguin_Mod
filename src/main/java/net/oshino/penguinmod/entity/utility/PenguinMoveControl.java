package net.oshino.penguinmod.entity.utility;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.MathHelper;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

public class PenguinMoveControl extends MoveControl {
    private final PenguinEntity penguin;

    public PenguinMoveControl(PenguinEntity penguin,float speed) {
        super(penguin);
        this.penguin = penguin;
        this.speed = speed;
    }

    private void updateVelocity() {
        if (this.penguin.isTouchingWater()) {
            // Penguins swim efficiently with increased speed and vertical buoyancy
            this.penguin.setVelocity(this.penguin.getVelocity().add(0.0, 0.1, 0.0));
            this.penguin.setMovementSpeed(Math.max(this.penguin.getMovementSpeed(), 0.15F));
        }else {
            // Penguins waddle on land with reduced speed
            this.penguin.setMovementSpeed((float) this.speed);
        }
    }

    @Override
    public void tick() {
        this.updateVelocity();
        if (this.state == MoveControl.State.MOVE_TO && !this.penguin.getNavigation().isIdle()) {
            double d = this.targetX - this.penguin.getX();
            double e = this.targetY - this.penguin.getY();
            double f = this.targetZ - this.penguin.getZ();
            double g = Math.sqrt(d * d + e * e + f * f);
            if (g < 1.0E-5F) {
                this.entity.setMovementSpeed(0.0F);
            } else {
                e /= g;
                float h = (float)(MathHelper.atan2(f, d) * 180.0F / (float) Math.PI) - 90.0F;
                this.penguin.setYaw(this.wrapDegrees(this.penguin.getYaw(), h, 90.0F));
                this.penguin.bodyYaw = this.penguin.getYaw();
                float i = (float) (this.speed * this.penguin.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                this.penguin.setMovementSpeed(MathHelper.lerp(0.125F, this.penguin.getMovementSpeed(), i));

                // Adjust velocity for swimming or waddling
                if (this.penguin.isTouchingWater()) {
                    this.penguin.setVelocity(this.penguin.getVelocity().add(0.0, (double) this.penguin.getMovementSpeed() * e * 0.2, 0.0));
                } else {
                    this.penguin.setVelocity(this.penguin.getVelocity().multiply(1.0, 0.0, 1.0)); // Ground movement
                }
            }
        } else {
            this.penguin.setMovementSpeed(0.0F);
        }
    }
}
