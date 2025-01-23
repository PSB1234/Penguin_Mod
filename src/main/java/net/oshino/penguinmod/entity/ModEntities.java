package net.oshino.penguinmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

public class ModEntities {
    public static final EntityType<PenguinEntity> PENGUIN  = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(PenguinMod.MOD_ID,"penguin"),
            EntityType.Builder.create(PenguinEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.45f,0.7f).build()
            );
    public static void registerModEntities(){
        PenguinMod.LOGGER.info("Registering Mod Entities for "+ PenguinMod.MOD_ID);
    }
}
