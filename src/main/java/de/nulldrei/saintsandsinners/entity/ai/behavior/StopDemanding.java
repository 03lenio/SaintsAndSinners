package de.nulldrei.saintsandsinners.entity.ai.behavior;

import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class StopDemanding {
        public static BehaviorControl<AbstractSurvivor> create() {
        return BehaviorBuilder.create((p_259197_) -> {
            return p_259197_.group(p_259197_.absent(MemoryModuleType.NEAREST_VISIBLE_PLAYER)).apply(p_259197_, (p_259512_) -> {
                return (p_289481_, p_289482_, p_289483_) -> {
                    if (p_289482_.getOffhandItem().isEmpty()) {
                        if(p_289482_ instanceof RobberSurvivor robberSurvivor) {
                            robberSurvivor.setActivelyDemanding(false);
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