package net.oshino.penguinmod.entity.goals;

import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.util.math.BlockPos;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

public class PenguinEscapeGoal extends EscapeDangerGoal {
     public PenguinEscapeGoal(PenguinEntity penguin, double speed) {
        super(penguin, speed);
    }
    @Override
    public boolean canStart() {
        if (!this.isInDanger()) {
            return false;
        } else {
            BlockPos blockPos = this.locateClosestWater(this.mob.getWorld(), this.mob, 20);
            if (blockPos != null) {
                this.targetX = blockPos.getX();
                this.targetY = blockPos.getY();
                this.targetZ = blockPos.getZ();
                return true;
            } else {
                return this.findTarget();
            }
        }
    }
}
