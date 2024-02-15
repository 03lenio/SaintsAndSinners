package de.nulldrei.saintsandsinners.armor.material;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum MaterialReclaimed implements ArmorMaterial {

        RECLAIMED("reclaimed", 0, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.HELMET, 0);
        }), 25, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
            return Ingredient.of(Items.SKELETON_SKULL);
        });

        private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.HELMET, 5);
        });
        private final String name;
        private final int maxDamageFactor;
        private final EnumMap<ArmorItem.Type, Integer> damageReductionAmountArray;
        private final int enchantability;
        private final SoundEvent soundEvent;
        private final float toughness;
        private final float knockbackResistance;
        private final Supplier<Ingredient> repairMaterial;

        MaterialReclaimed(String name, int maxDamageFactor, EnumMap<ArmorItem.Type, Integer> damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
            this.name = name;
            this.maxDamageFactor = maxDamageFactor;
            this.damageReductionAmountArray = damageReductionAmountArray;
            this.enchantability = enchantability;
            this.soundEvent = soundEvent;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
            this.repairMaterial = repairMaterial;
        }

        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.maxDamageFactor;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type) {
            return this.damageReductionAmountArray.get(type);
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantability;
        }

        @Override
        public SoundEvent getEquipSound() {
            return this.soundEvent;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairMaterial.get();
        }

        @OnlyIn(Dist.CLIENT)
        public String getName() {
            return this.name;
        }

        public float getToughness() {
            return this.toughness;
        }

        /**
         * Gets the percentage of knockback resistance provided by armor of the material.
         */
        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }
    }