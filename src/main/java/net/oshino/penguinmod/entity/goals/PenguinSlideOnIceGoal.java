package net.oshino.penguinmod.entity.goals;

import net.minecraft.entity.ai.goal.Goal;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public class PenguinSlideOnIceGoal extends Goal {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PenguinSlideOnIceGoal.class);
    private final PenguinEntity penguin;
    private int slideTicks = 0; // Sliding time
    private int restartCooldown = 0; // Cooldown before next slide

    public PenguinSlideOnIceGoal(PenguinEntity penguin) {
        this.penguin = penguin;
    }

    @Override
    public boolean canStart() {
        return this.penguin.isOnIce() // Only slide on ice
                && restartCooldown <= 0 // Check if cooldown is finished
                && this.penguin.getVelocity().horizontalLengthSquared() > 0.01; // Only slide if moving
    }

    //100 ticks == 5 seconds
    @Override
    public void start() {
        this.slideTicks = 100; // Sliding lasts 5 seconds (100 ticks)
        this.penguin.setSliding(true);
        // Boost initial velocity
        this.penguin.setVelocity(this.penguin.getVelocity().multiply(1.2D, 0.0D, 1.2D));
    }

    @Override
    public boolean shouldContinue() {
        return this.penguin.isOnIce() && this.slideTicks > 0;
    }

    @Override
    public void stop() {
        this.slideTicks = 0;
        this.penguin.setSliding(false);
        this.restartCooldown = 200; // Start cooldown
    }
    @Override
    public boolean canStop() {
        return this.slideTicks <= 0;
    }
    @Override
    public void tick() {
        System.out.println(" isSliding in goal before: " + penguin.isSliding());

        if (this.slideTicks-- > 0) {
            // Reduce friction effect for smooth sliding
            this.penguin.setVelocity(this.penguin.getVelocity().multiply(1.02D, 0.0D, 1.02D));
        } else {
            this.penguin.setSliding(false);
        }

        // Reduce cooldown even when the goal is inactive
        if (this.restartCooldown > 0) {
            this.restartCooldown--;
        }
        System.out.println(" isSliding in goal after: " + penguin.isSliding());

    }
}
