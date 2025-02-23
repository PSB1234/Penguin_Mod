package net.oshino.penguinmod.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.block.ModBlocks;
import net.oshino.penguinmod.entity.ModEntities;

import java.util.List;
/**
 * Handles the registration of custom items in the Penguin Mod.
 */
public class ModItems {
    /**
     * The spawn egg for the penguin entity.
     * - Primary color: White (0xffffff)
     * - Secondary color: Black (0x000000)
     */
    public static final Item PENGUIN_SPAWN_EGG = registerItem("penguin_spawn_egg",
            new SpawnEggItem(ModEntities.PENGUIN, 0xffffff, 0x000000, new Item.Settings()));
    /**
     * The block item representing a penguin egg.
     * This item allows players to place the penguin egg as a block.
     */
    public static final Item PENGUIN_EGG = registerItem("penguin_egg",
            new BlockItem(ModBlocks.PENGUIN_EGG, new Item.Settings()));
    /**
     * Registers an item with the specified name and item instance.
     *
     * @param name The registry name of the item.
     * @param item The item instance to register.
     * @return The registered item.
     */
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(PenguinMod.MOD_ID, name), item);
    }
    /**
     * Registers all mod items and adds them to appropriate creative mode item groups.
     * - The penguin spawn egg is added to the `SPAWN_EGGS` item group.
     * - The penguin egg block item is added to the `NATURAL` item group.
     */
    public static void registerModItems() {
        PenguinMod.LOGGER.info("Registering Mod Items for " + PenguinMod.MOD_ID);
        // Add Penguin Spawn Egg to the Spawn Eggs tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(new ItemStack(PENGUIN_SPAWN_EGG));
        });
        // Add Penguin Egg to the Natural Blocks tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(new ItemStack(PENGUIN_EGG));
        });
    }

}
