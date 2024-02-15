package de.nulldrei.saintsandsinners.entity.hostile.variant;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum ReclaimedVariant implements StringRepresentable {
    GEORGIA(0, "georgia"),
    WALTER(1, "walter"),
    JESSE(2, "jesse");

    public static final Codec<ReclaimedVariant> CODEC = StringRepresentable.fromEnum(ReclaimedVariant::values);
    private static final IntFunction<ReclaimedVariant> BY_ID = ByIdMap.continuous(ReclaimedVariant::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String name;

    private ReclaimedVariant(int p_262580_, String p_262591_) {
        this.id = p_262580_;
        this.name = p_262591_;
    }

    public int getId() {
        return this.id;
    }

    public static ReclaimedVariant byId(int p_30987_) {
        return BY_ID.apply(p_30987_);
    }

    public String getSerializedName() {
        return this.name;
    }
}