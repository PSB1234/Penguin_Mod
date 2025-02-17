package net.oshino.penguinmod.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

import java.util.List;


public class PenguinHuntGoal extends Goal {
    private final PenguinEntity penguin;
    private final double speed;
    private LivingEntity target;

    public PenguinHuntGoal(PenguinEntity penguin, double speed) {
        this.penguin = penguin;
        this.speed = speed;
    }

    @Override
    public boolean canStart() {
        // Start if the penguin is hungry and there is prey nearby
        return this.penguin.getHungerLevel() < 50 && this.findPrey();
    }

    @Override
    public boolean shouldContinue() {
        // Continue as long as the penguin is still hungry and the target exists
        return this.penguin.getHungerLevel() < 80 && this.target != null && this.target.isAlive();
    }

    @Override
    public void start() {
        // Begin moving toward the target
        this.penguin.getNavigation().startMovingTo(this.target, this.speed);
    }

    @Override
    public void stop() {
        // Clear the target when the goal ends
        this.target = null;
        this.penguin.getNavigation().stop();
    }

    // Complete tryAttack method in PenguinHuntGoal
    @Override
    public void tick() {
        if (this.target != null) {
            double distance = this.penguin.squaredDistanceTo(this.target);
            if (distance < 2.0) {
                // Attack the target
                this.penguin.tryAttack(this.target);
                // Feed penguin and increase hunger
                this.penguin.increaseHunger(20);
                this.stop(); // End hunt after attacking
            } else {
                // Keep moving towards the target
                this.penguin.getNavigation().startMovingTo(this.target, this.speed);
            }
        }
    }

    private boolean findPrey() {
        // Find nearby prey that matches the FOLLOW_TAMED_PREDICATE
        List<LivingEntity> nearbyEntities = this.penguin.getWorld().getEntitiesByClass(
                LivingEntity.class,
                this.penguin.getBoundingBox().expand(10.0),
                PenguinEntity.getTargetPredicate()
        );
        if (!nearbyEntities.isEmpty()) {
            this.target = nearbyEntities.getFirst();
            return true;
        }
        return false;
    }
}
