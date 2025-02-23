package net.oshino.penguinmod.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.block.custom.PenguinEggBlock;
/**
 * The ModBlocks class is responsible for registering custom blocks in the PenguinMod.
 */
public class ModBlocks {
    /**
     * Registers a custom block representing a penguin egg.
     */
    public static final Block PENGUIN_EGG = registerBlock("penguin_egg",
            new PenguinEggBlock(AbstractBlock.Settings.create().ticksRandomly()));
    /**
     * Registers a block in the game's block registry.
     *
     * @param name  The name of the block.
     * @param block The block instance.
     * @return The registered block.
     */
    private static Block registerBlock(String name, Block block) {
        registerBlockItems(name, block);// Registers the block item representation.
        return Registry.register(Registries.BLOCK, Identifier.of(PenguinMod.MOD_ID, name), block);
    }
    /**
     * Registers the block as an item so it can be placed in the game world.
     *
     * @param name  The name of the block.
     * @param block The block instance.
     */
    private static void registerBlockItems(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(PenguinMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }
    /**
     * Logs a message indicating that mod blocks are being registered.
     * This method should be called during mod initialization.
     */
    public static void registerModBlocks() {
        PenguinMod.LOGGER.info("Registering Mod blocks for: " + PenguinMod.MOD_ID);}
}
