package de.nulldrei.saintsandsinners.entity.hostile.variant;

import com.mojang.serialization.Codec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum TowerVariant implements StringRepresentable {
    MORGAN(0, "morgan"),
    ABRAHAM(1, "abraham"),
    JOE(2, "joe");

    public static final Codec<TowerVariant> CODEC = StringRepresentable.fromEnum(TowerVariant::values);
    private static final IntFunction<TowerVariant> BY_ID = ByIdMap.continuous(TowerVariant::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String name;

    private TowerVariant(int p_262580_, String p_262591_) {
        this.id = p_262580_;
        this.name = p_262591_;
    }

    public int getId() {
        return this.id;
    }

    public static TowerVariant byId(int p_30987_) {
        return BY_ID.apply(p_30987_);
    }

    public String getSerializedName() {
        return this.name;
    }
}