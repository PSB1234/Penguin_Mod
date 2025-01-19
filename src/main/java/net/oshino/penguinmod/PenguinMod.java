package net.oshino.penguinmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
import net.oshino.penguinmod.world.gen.MobEntitySpawns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenguinMod implements ModInitializer {
	public static final String MOD_ID = "penguin-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {

		MobEntitySpawns.addPenguinSpawns();

		ModEntities.registerModEntities();

		FabricDefaultAttributeRegistry.register(ModEntities.PENGUIN, PenguinEntity.createAttributes());

	}
}