package de.nulldrei.saintsandsinners.entity.ai.behavior;

import de.nulldrei.saintsandsinners.entity.ai.survivor.BeggarSurvivorAI;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class StopHoldingItemIfNoLongerAdmiring {
        public static BehaviorControl<AbstractSurvivor> create() {
        return BehaviorBuilder.create((p_259197_) -> {
            return p_259197_.group(p_259197_.absent(MemoryModuleType.ADMIRING_ITEM)).apply(p_259197_, (p_259512_) -> {
                return (p_289481_, p_289482_, p_289483_) -> {

                    if (!p_289482_.getOffhandItem().isEmpty() && !p_289482_.getOffhandItem().canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK)) {
                        if(p_289482_ instanceof  BeggarSurvivor) {
                            BeggarSurvivorAI.stopHoldingOffHandItem((BeggarSurvivor) p_289482_, true);
                        }
                        return true;
                    } else {
                        return false;
                    }
                };
            });
        });
    }
}