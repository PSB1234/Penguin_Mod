package net.oshino.penguinmod.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.ModEntities;

import java.util.List;

public class ModItems {
    public static final Item PENGUIN_SPAWN_EGG = registerItem("penguin_spawn_egg",
            new SpawnEggItem(ModEntities.PENGUIN, 0x9dc783, 0xbfaf5f, new Item.Settings()){
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    tooltip.add(Text.of("tooltip.penguinmod.penguin_spawn_egg.tooltip"));
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(PenguinMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        PenguinMod.LOGGER.info("Registering Mod Items for " + PenguinMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(new ItemStack(PENGUIN_SPAWN_EGG));
        });
    }

}
