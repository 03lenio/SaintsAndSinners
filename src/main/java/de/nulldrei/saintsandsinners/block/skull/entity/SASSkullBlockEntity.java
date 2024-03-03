package de.nulldrei.saintsandsinners.block.skull.entity;

import de.nulldrei.saintsandsinners.block.blockentity.SASBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.print.DocFlavor;

public class SASSkullBlockEntity extends SkullBlockEntity {
    private int animationTickCount;
    private boolean isAnimating;

    public SASSkullBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return SASBlockEntities.SAINTSANDSINNERS_SKULL.get();
    }

    public static void animation(Level level, BlockPos blockPos, BlockState blockState, SASSkullBlockEntity effectSkullBlockEntity) {

            effectSkullBlockEntity.isAnimating = false;


    }

    public float getAnimation(float p_262053_) {
        return this.isAnimating ? (float) this.animationTickCount + p_262053_ : (float) this.animationTickCount;
    }

}