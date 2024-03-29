package de.nulldrei.saintsandsinners;

import com.mojang.logging.LogUtils;
import de.nulldrei.saintsandsinners.behaviors.SASDispenseBehavior;
import de.nulldrei.saintsandsinners.block.SASBlocks;
import de.nulldrei.saintsandsinners.block.blockentity.SASBlockEntities;
import de.nulldrei.saintsandsinners.block.skull.render.SASSkullBlockRenderer;
import de.nulldrei.saintsandsinners.config.SASConfig;
import de.nulldrei.saintsandsinners.effect.SASMobEffects;
import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.entity.activitiy.SASActivities;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.ai.memory.sensors.SASSensorTypes;
import de.nulldrei.saintsandsinners.entity.client.*;
import de.nulldrei.saintsandsinners.event.SASEventHandler;
import de.nulldrei.saintsandsinners.event.SASForgeEvents;
import de.nulldrei.saintsandsinners.item.SASItems;
import de.nulldrei.saintsandsinners.sound.SASSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SaintsAndSinners.MODID)
public class SaintsAndSinners {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "saintsandsinners";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "saintsandsinners" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "saintsandsinners" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> SAS_TAB = CREATIVE_MODE_TABS.register("saintsandsinners_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.saintsandsinners_tab"))
            .icon(() -> SASItems.ESTEEMED_MORTAL.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
            output.accept(SASItems.BOTTLE.get());
            output.accept(SASItems.BROKEN_BOTTLE.get());
            output.accept(SASItems.SHIV.get());
            output.accept(SASItems.SCREWDRIVER.get());
            output.accept(SASBlocks.RECYCLING_BIN.get());
            output.accept(SASItems.DIAMOND_SHARD.get());
            output.accept(SASItems.NETHERITE_FRAGMENT.get());
            output.accept(SASItems.COPPER_NUGGET.get());
            output.accept(SASItems.SPAWN_BEGGAR.get());
            output.accept(SASItems.SPAWN_ROBBER.get());
            output.accept(SASItems.SPAWN_TOWER_GUARD.get());
            output.accept(SASItems.SPAWN_RECLAIMED_CULTIST.get());
            output.accept(SASItems.GREEN_BOX_OF_STUFF.get());
            output.accept(SASItems.GREY_BOX_OF_STUFF.get());
            output.accept(SASItems.ORANGE_BOX_OF_STUFF.get());
            output.accept(SASItems.RECLAIMED_MASK.get());
            output.accept(SASItems.TOWER_HELMET.get());
            output.accept(SASItems.TOWER_VEST.get());
            output.accept(SASItems.ROTTEN_BRAIN_MATTER.get());
            output.accept(SASItems.ROTTEN_INTESTINES.get());
            output.accept(SASItems.ROTTEN_LEGS.get());
            output.accept(SASItems.ROTTEN_TOES.get());
            output.accept(SASItems.EXPLOSIVE_ARROW.get());
            output.accept(SASItems.LURE_ARROW.get());
            output.accept(SASItems.NAIL_BOMB.get());
            output.accept(SASItems.STICKY_PROXIMITY_BOMB.get());
            output.accept(SASItems.TIMED_NOISE_MAKER_BOMB.get());
            output.accept(SASItems.ABRAHAM_HEAD.get());
            output.accept(SASItems.BEN_HEAD.get());
            output.accept(SASItems.GEORGIA_HEAD.get());
            output.accept(SASItems.JESSE_HEAD.get());
            output.accept(SASItems.JOE_HEAD.get());
            output.accept(SASItems.MISSY_HEAD.get());
            output.accept(SASItems.OSAMA_HEAD.get());
            output.accept(SASItems.PATRICK_HEAD.get());
            output.accept(SASItems.RANDY_HEAD.get());
            output.accept(SASItems.RICK_HEAD.get());
            output.accept(SASItems.TOM_HEAD.get());
            output.accept(SASItems.WALTER_HEAD.get());
            output.accept(SASItems.CLEAVER.get());
            output.accept(SASItems.ESTEEMED_MORTAL.get());
            output.accept(SASItems.FOURTH_N_PAIN.get());
            output.accept(SASItems.THE_BAT.get());
            output.accept(SASItems.NAIL_BAT.get());
            output.accept(SASItems.HATCHET.get());
            output.accept(SASItems.SAMEDIS_HAND.get());
            output.accept(SASItems.NATIONAL_GUARD_KNIFE.get());
            output.accept(SASItems.NINJA_STAR.get());
            output.accept(SASItems.ABSOLUTION.get());
            output.accept(SASItems.NIGHTSHIFT.get());
            output.accept(SASItems.NAIL.get());
            output.accept(SASItems.SHARP_BLADE.get());
            output.accept(SASItems.TIN_CAN.get());
            output.accept(SASItems.MUSIC_PLAYER.get());
            output.accept(SASItems.REINFORCED_HANDLE.get());
            }).build());

    public SaintsAndSinners() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SASBlocks.register(modEventBus);
        SASItems.register(modEventBus);
        SASBlockEntities.register(modEventBus);
        SASEntities.register(modEventBus);
        SASMemoryModules.register(modEventBus);
        SASSensorTypes.register(modEventBus);
        SASActivities.register(modEventBus);
        SASSounds.register(modEventBus);
        SASMobEffects.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(new SASForgeEvents());
        MinecraftForge.EVENT_BUS.register(new SASEventHandler());
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SASConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        event.enqueueWork(SASDispenseBehavior::registerDispenseBehaviors);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(SASEntities.BEGGAR_SURVIVOR.get(), (p_174066_) -> {
                return new SurvivorRenderer(p_174066_, SASModelLayers.SURVIVOR_LAYER, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false);
            });
            EntityRenderers.register(SASEntities.ROBBER_SURVIVOR.get(), (p_174066_) -> {
                return new SurvivorRenderer(p_174066_, SASModelLayers.SURVIVOR_LAYER, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false);
            });
            EntityRenderers.register(SASEntities.TOWER_FACTION_SURVIVOR.get(), (p_174066_) -> {
                return new SurvivorRenderer(p_174066_, SASModelLayers.SURVIVOR_LAYER, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false);
            });
            EntityRenderers.register(SASEntities.RECLAIMED_FACTION_SURVIVOR.get(), (p_174066_) -> {
                return new SurvivorRenderer(p_174066_, SASModelLayers.SURVIVOR_LAYER, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false);
            });
            EntityRenderers.register(SASEntities.DECAPITATED.get(), (p_174066_) -> {
                return new DecapitatedRenderer(p_174066_, SASModelLayers.DECAPITATED_LAYER, ModelLayers.PIGLIN_INNER_ARMOR, ModelLayers.PIGLIN_OUTER_ARMOR, false);
            });
            EntityRenderers.register(SASEntities.EXPLOSIVE_ARROW.get(), ExplosiveArrowRenderer::new);
            EntityRenderers.register(SASEntities.LURE_ARROW.get(), LureArrowRenderer::new);
            EntityRenderers.register(SASEntities.NAIL_BOMB.get(), ThrownItemRenderer::new);
            EntityRenderers.register(SASEntities.STICKY_PROXIMITY_BOMB.get(), PersistentThrownItemRenderer::new);
            EntityRenderers.register(SASEntities.THROWN_TIMED_NOISE_MAKER_BOMB.get(), PersistentThrownItemRenderer::new);
            EntityRenderers.register(SASEntities.TIMED_NOISE_MAKER_BOMB.get(), TimedNoiseMakerBombRenderer::new);
            BlockEntityRenderers.register(SASBlockEntities.SAINTSANDSINNERS_SKULL.get(), SASSkullBlockRenderer::new);
            EntityRenderers.register(SASEntities.NINJA_STAR.get(), ThrownItemRenderer::new);
        }
    }
}
