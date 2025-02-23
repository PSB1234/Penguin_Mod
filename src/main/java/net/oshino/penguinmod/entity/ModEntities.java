package net.oshino.penguinmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
/**
 * Handles the registration of custom entities for the Penguin Mod.
 */
public class ModEntities {
    /**
     * The registered entity type for the Penguin.
     * This defines the penguin as a creature, its dimensions, and its spawning behavior.
     */
    public static final EntityType<PenguinEntity> PENGUIN  = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(PenguinMod.MOD_ID,"penguin"),
            EntityType.Builder.create(PenguinEntity::new, SpawnGroup.CREATURE)
                    .dimensions(.45f,0.7f).build()
            );

    /**
     * Logs the registration of mod entities.
     * This method should be called during mod initialization to ensure entities are properly registered.
     */
    public static void registerModEntities(){
        PenguinMod.LOGGER.info("Registering Mod Entities for "+ PenguinMod.MOD_ID);
    }

}
