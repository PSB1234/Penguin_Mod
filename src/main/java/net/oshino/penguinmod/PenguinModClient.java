package net.oshino.penguinmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.entity.client.PenguinModel;
import net.oshino.penguinmod.entity.client.PenguinRenderer;

public class PenguinModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(PenguinModel.Penguin,PenguinModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.Penguin, PenguinRenderer::new);
    }
}
