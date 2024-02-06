package de.nulldrei.saintsandsinners.entity.animations;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;

public class SASAnimationUtils {

    public static void animateBegging(ModelPart p_102087_, ModelPart p_102088_, LivingEntity p_102089_, boolean p_102090_) {
        ModelPart modelpart = p_102090_ ? p_102087_ : p_102088_;
        ModelPart modelpart1 = p_102090_ ? p_102088_ : p_102087_;
        modelpart.yRot = p_102090_ ? -0.8F : 0.8F;
        modelpart.xRot = -0.97079635F;
        modelpart1.xRot = modelpart.xRot;
        modelpart1.yRot = p_102090_ ? 0.8F : -0.8F;
    }

}
