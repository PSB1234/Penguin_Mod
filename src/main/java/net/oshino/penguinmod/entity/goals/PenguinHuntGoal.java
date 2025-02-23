package net.oshino.penguinmod.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

import java.util.List;
/**
 * A custom AI goal for penguins to hunt nearby prey when they are hungry.
 * The penguin will search for prey, move toward it, attack when close, and increase its hunger level upon a successful attack.
 */

public class PenguinHuntGoal extends Goal {
    private final PenguinEntity penguin;
    private final double speed;
    private LivingEntity target;
    /**
     * Constructs a new PenguinHuntGoal instance.
     *
     * @param penguin The penguin entity that will use this goal.
     * @param speed   The movement speed at which the penguin will chase prey.
     */
    public PenguinHuntGoal(PenguinEntity penguin, double speed) {
        this.penguin = penguin;
        this.speed = speed;
    }
    /**
     * Determines whether the hunting goal should start.
     * The penguin will begin hunting if its hunger level is below 50 and prey is found nearby.
     *
     * @return {@code true} if the penguin is hungry and prey is available, otherwise {@code false}.
     */
    @Override
    public boolean canStart() {
        // Start if the penguin is hungry and there is prey nearby
        return this.penguin.getHungerLevel() < 50 && this.findPrey();
    }
    /**
     * Determines whether the hunting goal should continue.
     * The goal continues as long as the penguin's hunger level is below 80 and the target prey is still alive.
     *
     * @return {@code true} if the penguin is still hungry and prey is still available, otherwise {@code false}.
     */
    @Override
    public boolean shouldContinue() {
        // Continue as long as the penguin is still hungry and the target exists
        return this.penguin.getHungerLevel() < 80 && this.target != null && this.target.isAlive();
    }
    /**
     * Starts the hunting goal by making the penguin move towards the target prey.
     */
    @Override
    public void start() {
        // Begin moving toward the target
        this.penguin.getNavigation().startMovingTo(this.target, this.speed);
    }
    /**
     * Stops the hunting goal by clearing the target and halting navigation.
     */
    @Override
    public void stop() {
        // Clear the target when the goal ends
        this.target = null;
        this.penguin.getNavigation().stop();
    }
    /**
     * Executes each game tick while the hunting goal is active.
     * The penguin will continue chasing the prey and attack when within range.
     */
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
    /**
     * Searches for nearby prey within a 10-block radius.
     * The search is based on the penguin's target predicate.
     *
     * @return {@code true} if a prey entity is found, otherwise {@code false}.
     */
    private boolean findPrey() {
        // Find nearby prey that matches the FOLLOW_TAMED_PREDICATE
        List<LivingEntity> nearbyEntities = this.penguin.getWorld().getEntitiesByClass(
                LivingEntity.class,
                this.penguin.getBoundingBox().expand(10.0),
                PenguinEntity.getTargetPredicate()
        );
        if (!nearbyEntities.isEmpty()) {
            // Select the first available target
            this.target = nearbyEntities.getFirst();
            return true;
        }
        return false;
    }
}
