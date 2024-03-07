package de.nulldrei.saintsandsinners.effect;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.effect.harmful.BloodPoisoningEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class SASMobEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SaintsAndSinners.MODID);
    public static final RegistryObject<MobEffect> BLOOD_POISONING = EFFECTS.register("blood_poisoning",
            () -> new BloodPoisoningEffect(MobEffectCategory.HARMFUL, 3454687).addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE9E-7CE8-4030-940E-514C1F160890", (double)-0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
