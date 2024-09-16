package com.vertexcubed.ad_infinitum.common.satellite.frequency;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.StringRepresentable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class Frequency {


    public static final Codec<Frequency> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id").forGetter(Frequency::getId),
            Codec.STRING.fieldOf("name").forGetter(Frequency::getName),
            FrequencyDataType.CODEC.fieldOf("type").forGetter(Frequency::getDataType),
            UUIDUtil.CODEC.fieldOf("owner").forGetter(Frequency::getOwner),
            Publicity.CODEC.fieldOf("publicity").forGetter(Frequency::getPublicity)
    ).apply(instance, Frequency::new));

    private final UUID id;
    private String name;
    private FrequencyDataType dataType;
    private final UUID owner;
    private Publicity publicity;

    public Frequency(UUID id, String name, FrequencyDataType dataType, UUID owner, Publicity publicity) {
        this.id = id;
        this.name = name;
        this.dataType = dataType;
        this.owner = owner;
        this.publicity = publicity;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FrequencyDataType getDataType() {
        return dataType;
    }

    public void setDataType(FrequencyDataType dataType) {
        this.dataType = dataType;
    }

    public UUID getOwner() {
        return owner;
    }

    public Publicity getPublicity() {
        return publicity;
    }

    public void setPublicity(Publicity publicity) {
        this.publicity = publicity;
    }

    @Override
    public String toString() {
        return this.getName() + " (" + this.getId() + ")";
    }

    public enum Publicity implements StringRepresentable {
        PRIVATE("private"),
        TEAM("team"),
        PUBLIC("public");

        public static final Codec<Publicity> CODEC = StringRepresentable.fromEnum(Publicity::values);

        private final String name;

        Publicity(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Frequency) && ((Frequency) obj).id.equals(id);
    }
}
