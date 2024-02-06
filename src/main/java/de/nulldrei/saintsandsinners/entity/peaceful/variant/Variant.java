package de.nulldrei.saintsandsinners.entity.peaceful.variant;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

public enum Variant implements StringRepresentable {
    TOM(0, "tom"),
    PATRICK(1, "patrick"),
    OSAMA(2, "osama");

    public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
    private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    private final int id;
    private final String name;

    private Variant(int p_262580_, String p_262591_) {
        this.id = p_262580_;
        this.name = p_262591_;
    }

    public int getId() {
        return this.id;
    }

    public static Variant byId(int p_30987_) {
        return BY_ID.apply(p_30987_);
    }

    public String getSerializedName() {
        return this.name;
    }
}