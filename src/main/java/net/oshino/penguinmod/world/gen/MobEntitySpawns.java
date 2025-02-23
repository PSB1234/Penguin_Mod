package net.oshino.penguinmod.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.oshino.penguinmod.entity.ModEntities;


public class MobEntitySpawns {
    public static void addPenguinSpawns() {
        // Add penguin spawns to the overworld
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(BiomeKeys.FROZEN_OCEAN,BiomeKeys.COLD_OCEAN,BiomeKeys.SNOWY_BEACH,BiomeKeys.FROZEN_RIVER),
                SpawnGroup.CREATURE,
                ModEntities.PENGUIN,
                10, 4,8
        );
        SpawnRestriction.register(
                ModEntities.PENGUIN,
                SpawnLocationTypes.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AnimalEntity::isValidNaturalSpawn
        );
    }

}
