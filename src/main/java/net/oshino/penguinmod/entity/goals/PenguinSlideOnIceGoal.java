package net.oshino.penguinmod.entity.goals;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
import org.joml.Vector3f;

/**
 * Goal for penguin entities to slide on ice.
 */
public class PenguinSlideOnIceGoal extends Goal {
    private final PenguinEntity penguin;
    private int slideTicks = 0; // Sliding time

    /**
     * Constructor for PenguinSlideOnIceGoal.
     *
     * @param penguin The penguin entity that will slide on ice.
     */
    public PenguinSlideOnIceGoal(PenguinEntity penguin) {
        this.penguin = penguin;
    }

    /**
     * Determines if the goal can start.
     *
     * @return true if the penguin is on ice, the sliding cooldown is finished, and the penguin is moving.
     */
    @Override
    public boolean canStart() {
        return this.penguin.isOnIce() // Only slide on ice
                && ((this.penguin.getSlidingCooldown() <= 0 // Check if cooldown is finished
                && this.penguin.getVelocity().horizontalLengthSquared() > 0.02)// Only slide if moving
                ||(this.penguin.gotHitByPlayer())); // Only slide if hit by player
    }

    /**
     * Starts the sliding goal.
     * Sets the sliding time and initial sliding direction.
     */
    @Override
    public void start() {
        this.slideTicks = 10; // Sliding lasts 5 seconds (100 ticks)
        this.penguin.setSliding(true);

        // Get the direction the penguin is being punched from
        Vector3f punchDirection = this.penguin.getHitByPlayerDirection();
        // If the penguin was not punched, slide in the direction it is facing
        // Stores initial sliding direction
        Vec3d slideDirection;
        if (punchDirection == null || punchDirection.length() <= 0) {
            // Capture the direction penguin is facing
            double yawRad = Math.toRadians(this.penguin.getYaw()); // Convert degrees to radians
            Vec3d lookDirection = new Vec3d(-Math.sin(yawRad), 0, Math.cos(yawRad)).normalize(); // Set a fixed sliding velocity in that direction
            slideDirection = lookDirection.multiply(0.8D); // Adjust for balance
        } else {
            // If the penguin was punched, slide in the direction it was punched from
            slideDirection = new Vec3d(punchDirection.x, punchDirection.y, punchDirection.z).normalize().multiply(0.8D);
        }
        this.penguin.setVelocity(slideDirection);
    }

    /**
     * Determines if the goal should continue.
     *
     * @return true if the penguin is on ice and the sliding time has not elapsed.
     */
    @Override
    public boolean shouldContinue() {
        return this.penguin.isOnIce() && this.slideTicks > 0;
    }

    /**
     * Stops the sliding goal.
     * Resets the sliding time and sets the sliding cooldown.
     */
    @Override
    public void stop() {
        this.slideTicks = 0;
        this.penguin.setSliding(false);
        this.penguin.setSlidingCooldown(200);
        this.penguin.setHitByPlayer(false);
    }

    /**
     * Determines if the goal can stop.
     *
     * @return true if the sliding time has elapsed, the penguin is not on ice, or the penguin is not moving.
     */
    @Override
    public boolean canStop() {
        return this.slideTicks <= 0 || !this.penguin.isOnIce() || this.penguin.getVelocity().lengthSquared() <= 0;
    }

    /**
     * Updates the sliding goal each tick.
     * Reduces the sliding time and slows down the penguin.
     */
    @Override
    public void tick() {
        if (this.slideTicks-- > 0) {
            // Slow down the penguin
            this.penguin.setVelocity(this.penguin.getVelocity().multiply(0.95D));
        } else {
            this.penguin.setSliding(false);
        }
    }
}