package de.nulldrei.saintsandsinners.entity.client;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.projectile.arrow.LureArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LureArrowRenderer extends ArrowRenderer<LureArrow> {
    public static final ResourceLocation LURE_ARROW_LOCATION = new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/projectiles/lure_arrow.png");

    public LureArrowRenderer(EntityRendererProvider.Context p_174399_) {
        super(p_174399_);
    }

    @Override
    public ResourceLocation getTextureLocation(LureArrow p_114482_) {
        return LURE_ARROW_LOCATION;
    }

}