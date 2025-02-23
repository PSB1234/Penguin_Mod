package net.oshino.penguinmod.entity.goals;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

/**
 * A custom AI goal that handles the breeding behavior of penguins.
 * When two penguins mate, one of them lays an egg, and experience orbs are generated.
 */
public class PenguinMateGoal extends AnimalMateGoal {
    private final PenguinEntity penguin;

    /**
     * Constructs a new PenguinMateGoal instance.
     *
     * @param penguin The penguin entity that will use this goal.
     * @param speed   The movement speed at which the penguin will approach its mate.
     */
    public PenguinMateGoal(PenguinEntity penguin, double speed) {
            super(penguin, speed);
            this.penguin = penguin;
        }
    /**
     * Determines whether the penguin can start the mating process.
     * This method calls the base implementation in {@link AnimalMateGoal}.
     *
     * @return {@code true} if the penguin can start mating, otherwise {@code false}.
     */
        @Override
        public boolean canStart() {
            return super.canStart() ;
        }
    /**
     * Handles the breeding process when two penguins successfully mate.
     * - Assigns an egg to the breeding penguin.
     * - Resets breeding timers for both penguins.
     * - Awards experience orbs if the game rule {@code DO_MOB_LOOT} is enabled.
     */
        @Override
        protected void breed() {
            ServerPlayerEntity serverPlayerEntity = this.animal.getLovingPlayer();
            if (serverPlayerEntity == null && this.mate.getLovingPlayer() != null) serverPlayerEntity = this.mate.getLovingPlayer();

            if (serverPlayerEntity != null) {
                serverPlayerEntity.incrementStat(Stats.ANIMALS_BRED);
                Criteria.BRED_ANIMALS.trigger(serverPlayerEntity, this.animal, this.mate, null);
            }
            this.penguin.setHasEgg(true);
            this.animal.setBreedingAge(6000);
            this.mate.setBreedingAge(6000);
            this.animal.resetLoveTicks();
            this.mate.resetLoveTicks();
            Random random = this.animal.getRandom();
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
            }

        }

    private boolean isSuitableBreedingBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block == Blocks.DIRT || block == Blocks.COBBLESTONE || block == Blocks.STONE;
    }

}

