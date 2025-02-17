package net.oshino.penguinmod.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;

public class ModSounds {
    public static final SoundEvent PENGUIN_AMBIENT = registerSoundEvent("penguin_ambient");
    public static final SoundEvent PENGUIN_HURT = registerSoundEvent("penguin_hurt");
    public static final SoundEvent PENGUIN_DEATH = registerSoundEvent("penguin_death");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(PenguinMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        PenguinMod.LOGGER.info("Registering Mod Sounds for " + PenguinMod.MOD_ID);
    }
}
