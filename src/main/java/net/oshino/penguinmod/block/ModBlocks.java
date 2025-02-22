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

public class ModBlocks {
    public static final Block PENGUIN_EGG = registerBlock("penguin_egg",
            new PenguinEggBlock(AbstractBlock.Settings.create()
                    .strength(0.5F)
                    .ticksRandomly()
                    .requiresTool()
                    ));

    private static Block registerBlock(String name, Block block) {
        registerBlockItems(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(PenguinMod.MOD_ID, name), block);
    }
    private static void registerBlockItems(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(PenguinMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }
    public static void registerModBlocks() {
        PenguinMod.LOGGER.info("Registering Mod blocks for: " + PenguinMod.MOD_ID);}
}
