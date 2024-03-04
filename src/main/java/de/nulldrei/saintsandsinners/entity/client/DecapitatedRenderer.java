package de.nulldrei.saintsandsinners.entity.client;

import com.google.common.collect.Maps;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.hostile.variant.ReclaimedVariant;
import de.nulldrei.saintsandsinners.entity.hostile.variant.TowerVariant;
import de.nulldrei.saintsandsinners.entity.dead.Decapitated;
import de.nulldrei.saintsandsinners.entity.peaceful.variant.Variant;
import net.minecraft.Util;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import java.util.Map;

public class DecapitatedRenderer extends MobRenderer<Decapitated, DecapitatedModel<Decapitated>> {

    private static final Map<Variant, ResourceLocation> TEXTURE_BY_VARIANT_BEGGAR = Util.make(Maps.newEnumMap(Variant.class), (p_114874_) -> {
        p_114874_.put(Variant.TOM, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/tom.png"));
        p_114874_.put(Variant.PATRICK, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/patrick.png"));
        p_114874_.put(Variant.OSAMA, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/osama.png"));
    });

    private static final Map<de.nulldrei.saintsandsinners.entity.neutral.variant.Variant, ResourceLocation> TEXTURE_BY_VARIANT_ROBBER = Util.make(Maps.newEnumMap(de.nulldrei.saintsandsinners.entity.neutral.variant.Variant.class), (p_114874_) -> {
        p_114874_.put(de.nulldrei.saintsandsinners.entity.neutral.variant.Variant.BEN, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/ben.png"));
        p_114874_.put(de.nulldrei.saintsandsinners.entity.neutral.variant.Variant.RANDY, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/randy.png"));
        p_114874_.put(de.nulldrei.saintsandsinners.entity.neutral.variant.Variant.RICK, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/rick.png"));
    });

    private static final Map<TowerVariant, ResourceLocation> TEXTURE_BY_VARIANT_TOWER_GUARD = Util.make(Maps.newEnumMap(TowerVariant.class), (p_114874_) -> {
        p_114874_.put(TowerVariant.MISSY, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/missy.png"));
        p_114874_.put(TowerVariant.ABRAHAM, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/abraham.png"));
        p_114874_.put(TowerVariant.JOE, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/joe.png"));
    });

    private static final Map<ReclaimedVariant, ResourceLocation> TEXTURE_BY_VARIANT_RECLAIMED_CULTIST = Util.make(Maps.newEnumMap(ReclaimedVariant.class), (p_114874_) -> {
        p_114874_.put(ReclaimedVariant.GEORGIA  , new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/georgia.png"));
        p_114874_.put(ReclaimedVariant.WALTER, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/walter.png"));
        p_114874_.put(ReclaimedVariant.JESSE, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/jesse.png"));
    });


    static DecapitatedModel<Mob> decapitatedModel;

    public DecapitatedRenderer(EntityRendererProvider.Context p_174344_, ModelLayerLocation p_174345_, ModelLayerLocation p_174346_, ModelLayerLocation p_174347_, boolean p_174348_) {
        super(p_174344_, new DecapitatedModel<>(p_174344_.bakeLayer(SASModelLayers.DECAPITATED_LAYER)), 0.7F);
    }

    private static DecapitatedModel<Mob> createModel(EntityModelSet p_174350_, ModelLayerLocation p_174351_, boolean p_174352_) {
        decapitatedModel = new DecapitatedModel<>(p_174350_.bakeLayer(p_174351_));
        return decapitatedModel;
    }

    @Override
    public ResourceLocation getTextureLocation(Decapitated p_114482_) {
        if(p_114482_.getVariant().startsWith("survivor")) {
            return new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/" + p_114482_.getVariant().toLowerCase().split(":")[1] + ".png");
        } else {
            return new ResourceLocation("textures/entity/zombie/zombie.png");
        }
    }
}
