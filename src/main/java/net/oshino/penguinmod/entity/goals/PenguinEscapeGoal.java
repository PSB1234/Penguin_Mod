package net.oshino.penguinmod.entity.goals;

import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.util.math.BlockPos;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
/**
 * A custom AI goal for penguins to escape danger by moving towards the nearest water source.
 * Extends the {@link EscapeDangerGoal} class to provide penguins with a more natural escape behavior.
 */
public class PenguinEscapeGoal extends EscapeDangerGoal {
    /**
     * Constructs a new PenguinEscapeGoal instance.
     *
     * @param penguin The penguin entity that will use this goal.
     * @param speed   The movement speed at which the penguin will escape.
     */
     public PenguinEscapeGoal(PenguinEntity penguin, double speed) {
        super(penguin, speed);
    }
    /**
     * Determines whether the penguin should start escaping.
     * The penguin will attempt to find the nearest water source within a 20-block radius.
     *
     * @return {@code true} if the penguin is in danger and a water source or another escape target is found, otherwise {@code false}.
     */
    @Override
    public boolean canStart() {
        // Check if the penguin is currently in danger
        if (!this.isInDanger()) {
            return false;
        } else {
            // Try to locate the nearest water source within 20 blocks
            BlockPos blockPos = this.locateClosestWater(this.mob.getWorld(), this.mob, 20);
            if (blockPos != null) {
                // Set the target escape coordinates to the water location
                this.targetX = blockPos.getX();
                this.targetY = blockPos.getY();
                this.targetZ = blockPos.getZ();
                return true;
            } else {
                // If no water is found, use the default escape logic
                return this.findTarget();
            }
        }
    }
}
