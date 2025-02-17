package net.oshino.penguinmod.entity.goals;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

public class PenguinMateGoal extends AnimalMateGoal {
    private final PenguinEntity penguin;

    PenguinMateGoal(PenguinEntity penguin, double speed) {
            super(penguin, speed);
            this.penguin = penguin;
        }

        @Override
        public boolean canStart() {
            return super.canStart() ;
        }

        @Override
        protected void breed() {
            ServerPlayerEntity serverPlayerEntity = this.animal.getLovingPlayer();
            if (serverPlayerEntity == null && this.mate.getLovingPlayer() != null) serverPlayerEntity = this.mate.getLovingPlayer();

            if (serverPlayerEntity != null) {
                serverPlayerEntity.incrementStat(Stats.ANIMALS_BRED);
                Criteria.BRED_ANIMALS.trigger(serverPlayerEntity, this.animal, this.mate, null);
            }

            this.animal.setBreedingAge(6000);
            this.mate.setBreedingAge(6000);
            this.animal.resetLoveTicks();
            this.mate.resetLoveTicks();
            Random random = this.animal.getRandom();
            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
            }
            PenguinEntity babyPenguin = ModEntities.PENGUIN.create(this.world);
            if (babyPenguin != null) {
                babyPenguin.refreshPositionAndAngles(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0F, 0.0F);
                babyPenguin.setBaby(true);
                this.world.spawnEntity(babyPenguin);
            }
        }
    }

