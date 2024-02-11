package de.nulldrei.saintsandsinners.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.function.Predicate;

public class ChaseClosestPlayer {

   public static <E extends LivingEntity> BehaviorControl<E> create(Predicate<E> p_259490_) {
      return BehaviorBuilder.create((p_258748_) -> {
         return p_258748_.group(p_258748_.absent(MemoryModuleType.WALK_TARGET), p_258748_.present(MemoryModuleType.NEAREST_VISIBLE_PLAYER)).apply(p_258748_, (p_258743_, p_258744_) -> {
            return (p_258736_, p_258737_, p_258738_) -> {
                if(!p_259490_.test(p_258737_)) {
                    return false;
                }
                p_258743_.set(new WalkTarget(p_258748_.get(p_258744_), 1f,3));
                return true;
            };
         });
      });
   }
}