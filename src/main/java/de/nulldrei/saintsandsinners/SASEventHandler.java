package de.nulldrei.saintsandsinners;


import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "saintsandsinners", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SASEventHandler {



    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide || event.phase == TickEvent.Phase.END)
                return;
        if (event.player.level().getGameTime() % 20 == 0) {
            SASUtil.tickPlayer(event.player);
        }
    }


}
