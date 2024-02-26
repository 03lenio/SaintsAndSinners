package de.nulldrei.saintsandsinners.event;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.armor.model.ReclaimedMaskModel;
import de.nulldrei.saintsandsinners.entity.client.DecapitatedModel;
import de.nulldrei.saintsandsinners.entity.client.SASModelLayers;
import de.nulldrei.saintsandsinners.entity.client.SurvivorModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SaintsAndSinners.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SASCustomEventBusEventsClient {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(SaintsAndSinners.MODID, "main"), "reclaimed_mask"), ReclaimedMaskModel::createBodyLayer);
        event.registerLayerDefinition(SASModelLayers.SURVIVOR_LAYER, () -> LayerDefinition.create(SurvivorModel.createMesh(CubeDeformation.NONE), 64, 64));
        event.registerLayerDefinition(SASModelLayers.DECAPITATED_LAYER, () -> DecapitatedModel.createBodyLayer());

    }

}
