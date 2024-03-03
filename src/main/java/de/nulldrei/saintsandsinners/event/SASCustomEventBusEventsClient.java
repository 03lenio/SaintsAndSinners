package de.nulldrei.saintsandsinners.event;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.armor.model.ReclaimedMaskModel;
import de.nulldrei.saintsandsinners.block.skull.render.SASSkullBlockRenderer;
import de.nulldrei.saintsandsinners.block.skull.render.SASSkullHeadLayer;
import de.nulldrei.saintsandsinners.entity.client.DecapitatedModel;
import de.nulldrei.saintsandsinners.entity.client.SASModelLayers;
import de.nulldrei.saintsandsinners.entity.client.SurvivorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SaintsAndSinners.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SASCustomEventBusEventsClient {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(new ModelLayerLocation(new ResourceLocation(SaintsAndSinners.MODID, "main"), "reclaimed_mask"), ReclaimedMaskModel::createBodyLayer);
        event.registerLayerDefinition(SASModelLayers.SURVIVOR_LAYER, () -> LayerDefinition.create(SurvivorModel.createMesh(CubeDeformation.NONE), 64, 64));
        event.registerLayerDefinition(SASModelLayers.DECAPITATED_LAYER, DecapitatedModel::createBodyLayer);
        event.registerLayerDefinition(SASModelLayers.SURVIVOR_HEAD_LAYER, SkullModel::createHumanoidHeadLayer);

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerEffectSkullHeadLayers(final EntityRenderersEvent.AddLayers event) {
        Map<EntityType<?>, EntityRenderer<?>> renderers = Minecraft.getInstance().getEntityRenderDispatcher().renderers;
        for(Map.Entry<EntityType<?>, EntityRenderer<?>> renderer : renderers.entrySet()) {
            if (renderer.getValue() instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                boolean flag = false;
                for (RenderLayer<?, ?> layer : livingEntityRenderer.layers) {
                    if (layer instanceof CustomHeadLayer customHeadLayer) {
                        flag = true;
                        customHeadLayer.skullModels = SASSkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels());
                    }
                }
                if (flag) {
                    livingEntityRenderer.addLayer((RenderLayer)new SASSkullHeadLayer<>((RenderLayerParent)livingEntityRenderer, Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer()));
                }
            }
        }

        Map<String, EntityRenderer<? extends Player>> skins = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
        for(Map.Entry<String, EntityRenderer<? extends Player>> renderer : skins.entrySet()) {
            if (renderer.getValue() instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                boolean flag = false;
                for (RenderLayer<?, ?> layer : livingEntityRenderer.layers) {
                    if (layer instanceof CustomHeadLayer customHeadLayer) {
                        flag = true;
                        customHeadLayer.skullModels = SASSkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels());
                    }
                }
                if (flag) {
                    livingEntityRenderer.addLayer((RenderLayer)new SASSkullHeadLayer<>((RenderLayerParent)livingEntityRenderer, Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer()));
                }
            }
        }
    }
}
