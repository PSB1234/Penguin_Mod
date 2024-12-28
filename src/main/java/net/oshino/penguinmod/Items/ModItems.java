package net.oshino.penguinmod.Items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.ModEntities;

public class ModItems {
    public static final Item PENGUIN_SPAWN_EGG = registerItem("penguin_spawn_egg",
            new SpawnEggItem(ModEntities.Penguin,0x22181C,0xF6E8EA,new Item.Settings()));
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(PenguinMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        PenguinMod.LOGGER.info("Registering Mod Items for " + PenguinMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(PENGUIN_SPAWN_EGG);
        });
    }
}
