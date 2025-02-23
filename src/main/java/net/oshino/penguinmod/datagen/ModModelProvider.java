package net.oshino.penguinmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.block.ModBlocks;
import net.oshino.penguinmod.items.ModItems;

import java.util.Optional;
/**
 * The ModModelProvider class is responsible for generating models for blocks and items in the mod.
 * It extends FabricModelProvider to define block state models and item models.
 */
public class ModModelProvider extends FabricModelProvider {
    /**
     * Constructor for ModModelProvider.
     *
     * @param output The FabricDataOutput instance used for data generation.
     */
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }
    /**
     * Generates block state models.
     * This method registers a simple cube model for the PENGUIN_EGG block.
     *
     * @param blockStateModelGenerator The generator used to create block state models.
     */
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PENGUIN_EGG);
    }

    /**
     * Generates item models.
     * This method registers a spawn egg model for the PENGUIN_SPAWN_EGG item.
     *
     * @param itemModelGenerator The generator used to create item models.
     */
    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.PENGUIN_SPAWN_EGG,
                new Model(Optional.of(Identifier.of("item/template_spawn_egg")), Optional.empty()));
    }
}
