package de.nulldrei.saintsandsinners.entity.ai.behavior;

import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class StopBeggingWhenFleeing {
        public static BehaviorControl<AbstractSurvivor> create() {
        return BehaviorBuilder.create((p_259197_) -> {
            return p_259197_.group(p_259197_.present(MemoryModuleType.NEAREST_VISIBLE_PLAYER), p_259197_.present(MemoryModuleType.AVOID_TARGET)).apply(p_259197_, (player, avoid)  -> {
                return (p_289481_, p_289482_, p_289483_) -> {
                    if (p_289482_.getOffhandItem().isEmpty()) {
                        if(p_289482_ instanceof  BeggarSurvivor) {
                            BeggarSurvivor beggarSurvivor = (BeggarSurvivor) p_289482_;
                            beggarSurvivor.getEntityData().set(BeggarSurvivor.DATA_ACTIVELY_BEGGING, false);
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