package de.nulldrei.saintsandsinners.event;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.block.skull.SASSkullItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SaintsAndSinners.MODID, value = Dist.CLIENT)
public class SASForgeEvents {

    @SubscribeEvent
    public static void renderHeadPre(RenderLivingEvent.Pre<?, ?> event) {
        EntityModel<?> model = event.getRenderer().getModel();
        if (model instanceof HumanoidModel<?> humanoidModel) {
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SASSkullItem) {
                humanoidModel.head.visible = false;
                humanoidModel.hat.visible = false;
            }
        }
    }

    @SubscribeEvent
    public static void renderHeadPost(RenderLivingEvent.Post<?, ?> event) {
        EntityModel<?> model = event.getRenderer().getModel();
        if (model instanceof HumanoidModel<?> humanoidModel) {
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SASSkullItem) {
                humanoidModel.head.visible = true;
                humanoidModel.hat.visible = true;
            }
        }
    }
}

