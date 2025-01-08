package net.oshino.penguinmod.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> PENGUIN_FOOD = createTag("penguin_food");
        private static TagKey<Item> createTag(String id) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(PenguinMod.MOD_ID, id));
        }
    }
}
