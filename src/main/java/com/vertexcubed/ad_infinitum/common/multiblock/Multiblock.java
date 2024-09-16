package com.vertexcubed.ad_infinitum.common.multiblock;


import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vertexcubed.ad_infinitum.common.multiblock.data.MultiblockData;
import com.vertexcubed.ad_infinitum.common.multiblock.matcher.AirMatcher;
import com.vertexcubed.ad_infinitum.common.multiblock.matcher.AnyMatcher;
import com.vertexcubed.ad_infinitum.common.multiblock.matcher.StateMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

/**
 * Basic multiblock system for validating and creating json multiblocks.
 * Based on Modonomicon/Patchouli but tweaked to fit my needs.
 * <p>
 * Todo: yoink sparse/dense multiblocks from Modonomicon?
 * <p>
 * Todo 2: "metadata" fields. This would be machine specific and would probably need to be registered.
 */
public class Multiblock {

    public static final ResourceKey<Registry<Multiblock>> REGISTRY = ResourceKey.createRegistryKey(modLoc("multiblocks"));

    public static final Codec<Multiblock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.listOf().listOf().fieldOf("pattern").forGetter(multiblock -> multiblock._pattern),
            Codec.unboundedMap(Codec.STRING, StateMatcher.CODEC).fieldOf("mappings").forGetter(multiblock -> multiblock._mappings),
            MultiblockData.CODEC.optionalFieldOf("data").forGetter(Multiblock::getData)
    ).apply(instance, Multiblock::new));


    //for internal use and serialization
    private final List<List<String>> _pattern;
    private final Map<String, StateMatcher> _mappings;


    private StateMatcher[][][] matchers;
    private Vec3i center;
    private final Vec3i size;
    private final Optional<MultiblockData> data;


    public Multiblock(List<List<String>> pattern, Map<String, StateMatcher> mappings, Optional<MultiblockData> data) {
        this._pattern = pattern;
        _mappings = new ImmutableMap.Builder<String, StateMatcher>()
                //hardcoded keys. Can be overriden (hence buildKeepingLast instead of buildOrThrow)
                .put(" ", new AirMatcher())
                .put("_", new AnyMatcher())
                .putAll(mappings)
                .buildKeepingLast();
        this.data = data;
        this.size = getPatternSize(pattern);
        buildMultiBlock();
        this.data.ifPresent(d -> d.init(this));
    }

    /**
     * Determines the size of the multiblock and errors if not a box.
     */
    private static Vec3i getPatternSize(List<List<String>> pattern) {
        int x = -1;
        int z = -1;
        for(List<String> layer : pattern) {
            if(x == -1) {
                x = layer.size();
            }
            if(layer.size() != x) throw new IllegalArgumentException("Inconsistent list size, expected: " + x + ", got:" + layer.size());
            for(String str : layer) {
                if(z == -1) {
                    z = str.length();
                }
                if(str.length() != z) throw new IllegalArgumentException("Inconsistent string size, expected: " + z + ", got: " + str.length());
            }
        }
        return new Vec3i(x, pattern.size(), z);
    }


    private void buildMultiBlock() {

        center = null;
        matchers = new StateMatcher[size.getX()][size.getY()][size.getZ()];
        for(int y  = 0; y < size.getY(); y++) {
            for(int z = 0; z < size.getZ(); z++) {
                for(int x = 0; x < size.getX(); x++) {
                    String c = String.valueOf(_pattern.get(y).get(z).charAt(x));
                    if(!_mappings.containsKey(c)) {
                        throw new IllegalArgumentException("Key " + c + " not in mappings!");
                    }
                    if(c.equals("0")) {
                        if(center != null) throw new IllegalArgumentException("Cannot have more than one core!");
                        this.center = new Vec3i(x, y, z);
                    }

                    this.matchers[x][y][z] = _mappings.get(c);
                    if(this.data.isPresent()) {
                        this.data.get().setupData(x, y, z, c);
                    }
                }
            }
        }
    }

    /**
     * Returns the center in multiblock space.
     */
    public Vec3i getCenter() {
        return center;
    }

    public Optional<MultiblockData> getData() {
        return data;
    }

    //x y z starting from 0,0,0 in multiblock space
    public TestResult test(Level level, BlockPos center) {
        for(Rotation rotation : Rotation.values()) {
            if(test(level, center, rotation)) return new TestResult(true, rotation);
        }
        return new TestResult(false, Rotation.NONE);
    }

    public boolean test(Level level, BlockPos centerWorld, Rotation rotation) {
        BlockPos offset = centerWorld.subtract(this.center);
        for(int y = 0; y < size.getY(); y++) {
            for(int z = 0; z < size.getZ(); z++) {
                for(int x = 0; x < size.getX(); x++) {
                    BlockPos original = new BlockPos(x, y, z);
                    BlockPos rotated = original.subtract(this.center).rotate(rotation).offset(this.center);
                    if(!test(level, offset, rotated, original)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean test(Level level, BlockPos offset, BlockPos rotatedPos, BlockPos original) {

        if(!inBounds(original)) return false;

        BlockPos checkPos = rotatedPos.offset(offset);
        BlockState blockState = level.getBlockState(checkPos);
        return matchers[original.getX()][original.getY()][original.getZ()].getStatePredicate().test(level, checkPos, blockState);
    }

    public boolean inBounds(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return !(x < 0 || y < 0 || z < 0 || x > size.getX() || y > size.getY() || z > size.getZ());
    }


    @Override
    public String toString() {
        return "Multiblock { Pattern: " + _pattern +", Mappings: " + _mappings + " }";
    }



    public record TestResult(Boolean found, Rotation rotation) {}
}