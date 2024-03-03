package de.nulldrei.saintsandsinners.block.skull;

import de.nulldrei.saintsandsinners.block.SASBlocks;
import de.nulldrei.saintsandsinners.block.blockentity.SASBlockEntities;
import de.nulldrei.saintsandsinners.block.skull.entity.SASSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SASSkullBlock extends SkullBlock {

    public SASSkullBlock(Type type, Properties properties) {
        super(type, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SASSkullBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
                                                                  BlockEntityType<T> tBlockEntityType) {
        if (level.isClientSide()) {
                return createTickerHelper(tBlockEntityType, SASBlockEntities.SAINTSANDSINNERS_SKULL.get(),
                        SASSkullBlockEntity::animation);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public enum Types implements SkullBlock.Type {
        ABRAHAM,
        BEN,
        GEORGIA,
        JESSE,
        JOE,
        MISSY,
        OSAMA,
        PATRICK,
        RANDY,
        RICK,
        TOM,
        WALTER
    }
}