package net.oshino.penguinmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.oshino.penguinmod.block.ModBlocks;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
import net.oshino.penguinmod.sound.ModSounds;
import net.oshino.penguinmod.world.gen.MobEntitySpawns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.oshino.penguinmod.items.ModItems;
public class PenguinMod implements ModInitializer {
	public static final String MOD_ID = "penguinmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModSounds.registerSounds();
		MobEntitySpawns.addPenguinSpawns();
		ModEntities.registerModEntities();
		FabricDefaultAttributeRegistry.register(ModEntities.PENGUIN, PenguinEntity.createAttributes());

	}

}