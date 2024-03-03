package de.nulldrei.saintsandsinners.block.blockentity;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.block.SASBlocks;
import de.nulldrei.saintsandsinners.block.skull.entity.SASSkullBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SASBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SaintsAndSinners.MODID);



    @SuppressWarnings("all")
    public static final RegistryObject<BlockEntityType<SASSkullBlockEntity>> SAINTSANDSINNERS_SKULL =
            BLOCK_ENTITIES.register("saintsandsinners_skull", () -> BlockEntityType.Builder.of(SASSkullBlockEntity::new,
                    SASBlocks.ABRAHAM_HEAD.get(), SASBlocks.ABRAHAM_WALL_HEAD.get(),
                    SASBlocks.BEN_HEAD.get(), SASBlocks.BEN_WALL_HEAD.get(),
                    SASBlocks.GEORGIA_HEAD.get(), SASBlocks.GEORGIA_WALL_HEAD.get(),
                    SASBlocks.JESSE_HEAD.get(), SASBlocks.JESSE_WALL_HEAD.get(),
                    SASBlocks.JOE_HEAD.get(), SASBlocks.JOE_WALL_HEAD.get(),
                    SASBlocks.MISSY_HEAD.get(), SASBlocks.MISSY_WALL_HEAD.get(),
                    SASBlocks.OSAMA_HEAD.get(), SASBlocks.OSAMA_WALL_HEAD.get(),
                    SASBlocks.PATRICK_HEAD.get(), SASBlocks.PATRICK_WALL_HEAD.get(),
                    SASBlocks.RANDY_HEAD.get(), SASBlocks.RANDY_WALL_HEAD.get(),
                    SASBlocks.RICK_HEAD.get(), SASBlocks.RICK_WALL_HEAD.get(),
                    SASBlocks.TOM_HEAD.get(), SASBlocks.TOM_WALL_HEAD.get(),
                    SASBlocks.WALTER_HEAD.get(), SASBlocks.WALTER_WALL_HEAD.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
