package net.oshino.penguinmod.entity.goals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.oshino.penguinmod.block.ModBlocks;

import net.oshino.penguinmod.entity.custom.PenguinEntity;
/**
 * A custom AI goal for penguins to lay eggs in a suitable location.
 * The penguin searches for an open space, moves to the target position, and lays an egg if conditions are met.
 */
public class PenguinLayEggGoal extends MoveToTargetPosGoal {
    private final PenguinEntity penguin;
    /**
     * Constructs a new PenguinLayEggGoal instance.
     *
     * @param mob   The penguin entity that will use this goal.
     * @param speed The movement speed at which the penguin will search for a nesting spot.
     */
    public PenguinLayEggGoal(PathAwareEntity mob, double speed) {
        super(mob, speed, 16);
        this.penguin = (PenguinEntity) mob;
    }
    /**
     * Determines whether the penguin can start searching for a place to lay an egg.
     * The goal starts only if the penguin is carrying an egg.
     *
     * @return {@code true} if the penguin has an egg and can start the goal, otherwise {@code false}.
     */
    @Override
    public boolean canStart() {
        return this.penguin.hasEgg() && super.canStart();
    }
    /**
     * Determines whether the goal should continue running.
     * The goal continues only if the penguin still has an egg and has not yet laid it.
     *
     * @return {@code true} if the penguin still has an egg, otherwise {@code false}.
     */
    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && this.penguin.hasEgg();
    }
    /**
     * Called every tick while the goal is active.
     * The penguin will attempt to place an egg at the target location if conditions are met.
     */
    @Override
    public void tick() {
        super.tick();
        BlockPos blockPos = this.penguin.getBlockPos();
        if (!this.penguin.isTouchingWater() && this.hasReached() && this.penguin.hasEgg()) {
            World world = this.penguin.getWorld();
            BlockPos targetPos = this.targetPos.up();

            // Check if the target position is occupied by a PenguinEntity
            if (!world.getEntitiesByClass(PenguinEntity.class, new Box(targetPos), e -> true).isEmpty()) {
                // Try to place the egg in an adjacent block
                BlockPos[] possiblePositions = {
                        targetPos.north(), targetPos.south(), targetPos.east(), targetPos.west()
                };

                for (BlockPos pos : possiblePositions) {
                    if (world.getBlockState(pos).isAir() &&
                            world.getEntitiesByClass(PenguinEntity.class, new Box(pos), e -> true).isEmpty()) {
                        targetPos = pos;
                        break;
                    }
                }
            }

            // Final check before placing the egg
            if (world.getBlockState(targetPos).isAir()) {
                this.penguin.setHasEgg(false);
                world.playSound(null, blockPos, SoundEvents.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                BlockState blockState = ModBlocks.PENGUIN_EGG.getDefaultState();
                world.setBlockState(targetPos, blockState, Block.NOTIFY_ALL);
                world.emitGameEvent(GameEvent.BLOCK_PLACE, targetPos, GameEvent.Emitter.of(this.penguin, blockState));
                this.penguin.setLoveTicks(600);
            }
        }
    }

    /**
     * Determines whether a given position is a valid nesting spot for the penguin to lay an egg.
     * The block above the target position must be air.
     *
     * @param world The world in which the penguin is searching for a nesting spot.
     * @param pos   The position being evaluated.
     * @return {@code true} if the position is suitable for egg-laying, otherwise {@code false}.
     */
    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        return world.isAir(pos.up());
    }
}
