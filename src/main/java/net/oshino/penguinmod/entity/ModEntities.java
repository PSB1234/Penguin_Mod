package net.oshino.penguinmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.custom.PenguinEntities;

public class ModEntities {
    public static final EntityType<PenguinEntities> Penguin  = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(PenguinMod.MOD_ID,"penguin"),
            EntityType.Builder.create(PenguinEntities::new, SpawnGroup.CREATURE)
                    .dimensions(1f,1f).build()
            );
    public static void registerModEntities(){
        PenguinMod.LOGGER.info("Registering Mod Entities for"+ PenguinMod.MOD_ID);
    }
}
