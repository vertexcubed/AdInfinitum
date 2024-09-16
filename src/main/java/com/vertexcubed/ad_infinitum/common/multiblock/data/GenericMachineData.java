package com.vertexcubed.ad_infinitum.common.multiblock.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vertexcubed.ad_infinitum.common.registry.MultiblockDataRegistry;
import com.vertexcubed.ad_infinitum.common.util.CodecUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Rotation;

import java.util.*;

public class GenericMachineData extends MultiblockData {

    public static final Codec<GenericMachineData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.optionalField("itemIn", CodecUtils.listOrElementCodec(Codec.STRING)).forGetter(data -> data._itemIn),
                    Codec.optionalField("itemOut", CodecUtils.listOrElementCodec(Codec.STRING)).forGetter(data -> data._itemOut),
                    Codec.optionalField("fluidIn", CodecUtils.listOrElementCodec(Codec.STRING)).forGetter(data -> data._fluidIn),
                    Codec.optionalField("fluidOut", CodecUtils.listOrElementCodec(Codec.STRING)).forGetter(data -> data._fluidOut),
                    Codec.optionalField("energyIn", CodecUtils.listOrElementCodec(Codec.STRING)).forGetter(data -> data._energyIn),
                    Codec.optionalField("energyOut", CodecUtils.listOrElementCodec(Codec.STRING)).forGetter(data -> data._energyOut)
            ).apply(instance, GenericMachineData::new));

    private final Optional<List<String>> _itemIn;
    private final Optional<List<String>> _itemOut;
    private final Optional<List<String>> _fluidIn;
    private final Optional<List<String>> _fluidOut;
    private final Optional<List<String>> _energyIn;
    private final Optional<List<String>> _energyOut;

    private final Map<DataType, List<Vec3i>> dataPositions = new HashMap<>();

    public GenericMachineData(Optional<List<String>> itemIn, Optional<List<String>> itemOut, Optional<List<String>> fluidIn, Optional<List<String>> fluidOut, Optional<List<String>> energyIn, Optional<List<String>> energyOut) {
        this._itemIn = itemIn;
        this._itemOut = itemOut;
        this._fluidIn = fluidIn;
        this._fluidOut = fluidOut;
        this._energyIn = energyIn;
        this._energyOut = energyOut;
    }

    /**
     * Important: x, y, z are in MULTIBLOCK SPACE, ie not relative to the center but rather relative to the first key in the multiblock.
     */
    @Override
    public void setupData(int x, int y, int z, String key) {
        checkValue(DataType.ITEM_IN, _itemIn, x, y, z, key);
        checkValue(DataType.ITEM_OUT, _itemOut, x, y, z, key);
        checkValue(DataType.FLUID_IN, _fluidIn, x, y, z, key);
        checkValue(DataType.FLUID_OUT, _fluidOut, x, y, z, key);
        checkValue(DataType.ENERGY_IN, _energyIn, x, y, z, key);
        checkValue(DataType.ENERGY_OUT, _energyOut, x, y, z, key);
    }

    private void checkValue(DataType type, Optional<List<String>> optional, int x, int y, int z, String key) {
        if(optional.isEmpty()) return;
        if(optional.get().contains(key)) {
            addToMap(type, new Vec3i(x, y, z));
        }
    }
    private void addToMap(DataType type, Vec3i vec) {
        dataPositions.computeIfAbsent(type, t -> new ArrayList<>()).add(vec);
    }

    public List<BlockPos> getDataPositions(DataType type) {
        return getDataPositions(type, Rotation.NONE);
    }

    /**
     * Gets a list of vec3i positions relative to the center of the multiblock.
     */
    public List<BlockPos> getDataPositions(DataType type, Rotation rotation) {
        return dataPositions.getOrDefault(type, List.of()).stream().map(vec -> new BlockPos(vec).subtract(multiblock.getCenter()).rotate(rotation)).toList();
    }

    @Override
    public Type<?> getType() {
        return MultiblockDataRegistry.GENERIC_MACHINE_DATA.get();
    }

    public static class DataType implements StringRepresentable {
        public static final DataType ITEM_IN = new DataType("itemIn");
        public static final DataType ITEM_OUT = new DataType("itemOut");
        public static final DataType FLUID_IN = new DataType("fluidIn");
        public static final DataType FLUID_OUT = new DataType("fluidOut");
        public static final DataType ENERGY_IN = new DataType("energyIn");
        public static final DataType ENERGY_OUT = new DataType("energyOut");

        private final String name;
        public DataType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
