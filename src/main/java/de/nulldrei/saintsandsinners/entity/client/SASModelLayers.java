package de.nulldrei.saintsandsinners.entity.client;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class SASModelLayers {

    public static final ModelLayerLocation SURVIVOR_LAYER = new ModelLayerLocation(
            new ResourceLocation(SaintsAndSinners.MODID, "survivor_layer"), "main");
    public static final ModelLayerLocation DECAPITATED_LAYER = new ModelLayerLocation(
            new ResourceLocation(SaintsAndSinners.MODID, "decapitated_layer"), "main");

}
