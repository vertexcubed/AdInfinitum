package com.vertexcubed.ad_infinitum.common.blockentity;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.server.data.ChunkProtectedBlocks;
import com.vertexcubed.ad_infinitum.server.data.HoloDoorSavedData;
import earth.terrarium.adastra.common.blockentities.base.EnergyContainerMachineBlockEntity;
import earth.terrarium.adastra.common.blockentities.base.RedstoneControl;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.Configuration;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.ConfigurationEntry;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.ConfigurationType;
import earth.terrarium.adastra.common.blocks.base.MachineBlock;
import earth.terrarium.adastra.common.constants.ConstantComponents;
import earth.terrarium.botarium.common.energy.impl.InsertOnlyEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HoloDoorBlockEntity extends EnergyContainerMachineBlockEntity {

    public static final List<ConfigurationEntry> SIDE_CONFIG = List.of(
            new ConfigurationEntry(ConfigurationType.ENERGY, Configuration.NONE, ConstantComponents.SIDE_CONFIG_ENERGY)
    );
    public static final RedstoneControl REDSTONE = RedstoneControl.ON_WHEN_NOT_POWERED;

    public static final int CONTAINER_SIZE = 1;
    public static final String DATA_X_SIZE_KEY = "x_size";
    public static final String DATA_Y_SIZE_KEY = "y_size";
    public static final String DATA_VISIBLE_KEY = "visible";
    public static final String DATA_UUID_KEY = "uuid";

    private int x_size;
    private int y_size;
    private boolean visible;
    private UUID uuid;
    private BlockPos firstPos;
    private BlockPos secondPos;

    public HoloDoorBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, CONTAINER_SIZE);
        this.x_size = 2;
        this.y_size = 3;
        this.visible = true;
        this.uuid = UUID.randomUUID();
        updateCorners();
    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        super.serverTick(level, time, state, pos);
        this.updateCorners();


        WrappedBlockEnergyContainer energyStorage = this.getEnergyStorage();
        int usedEnergy = 100;
        List<BlockPos> protectedPositions = BlockPos.betweenClosedStream(getFirstPos(), getSecondPos()).map(BlockPos::immutable).toList();
        HoloDoorSavedData data = HoloDoorSavedData.getOrLoad(level);
        if(!this.canFunction() || energyStorage.internalExtract(usedEnergy, true) < usedEnergy) {
            data.remove(this.uuid);
            return;
        }
        energyStorage.internalExtract(usedEnergy, false);
        if(!data.containsKey(uuid)) {
            data.add(this.uuid, this.getBlockPos());
        }
        setChanged();
//        AdInfinitum.LOGGER.info("time: " + time);
        if(level.getGameTime() % 40 == 0) {
//            AdInfinitum.LOGGER.info("Attempting to protect positions");
//            AdInfinitum.LOGGER.info("pos 1: " + getFirstPos() + ", pos 2: " + getSecondPos());
            protectedPositions.forEach(protectedPos -> {
//                AdInfinitum.LOGGER.info("Trying position " + protectedPos + ", " + protectedPos.asLong());
                level.getChunkAt(protectedPos).getCapability(ChunkProtectedBlocks.CAP).ifPresent(cap -> {
                    cap.put(protectedPos, uuid);
//                    AdInfinitum.LOGGER.info("Successfully protected position " + protectedPos);
//                    AdInfinitum.LOGGER.info("Protected positions: " + cap);
                });
            });
        }
    }

    public static void invalidateBlocks(ServerLevel level, LevelChunk chunk) {
        chunk.getCapability(ChunkProtectedBlocks.CAP).ifPresent(cap -> {
            Map<Long, UUID> blockMap = cap.getBlockMap();
            Queue<BlockPos> toRemove = new LinkedList<>();
            blockMap.forEach((packedPos, uuid) -> {
                BlockPos pos = BlockPos.of(packedPos);
                HoloDoorSavedData data = HoloDoorSavedData.getOrLoad(level);
                if(data.containsKey(uuid)) {
                    BlockEntity be = chunk.getBlockEntity(data.get(uuid));
                    if(!(be instanceof HoloDoorBlockEntity)) {
                        data.remove(uuid);
                        toRemove.offer(pos);
                    }
                }
                else {
                    toRemove.offer(pos);
                }
            });
            toRemove.forEach(cap::remove);
        });
    }


//    private void updateProtectedBlocks(ServerLevel level, BlockState state, BlockPos pos) {
//        HoloDoorSavedData data = HoloDoorSavedData.getOrLoad(level);
////        List<Long> positions = data.get(this.uuid);
//        if(!positions.isEmpty() && !this.canFunction()) {
//            positions.clear();
//            data.setDirty();
//            return;
//        }
//
//
//        energyStorage.extractEnergy(usedEnergy, false);
//        positions.clear();
//        positions.addAll(protectedPositions);
//        data.setDirty();
//
//    }


    @Override
    public void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos) {
        super.clientTick(level, time, state, pos);
    }

    //Calculations moved to tick event instead of on call to prevent having to calculate this every frame
    private void updateCorners() {
        Direction counter = this.getBlockState().getValue(MachineBlock.FACING).getCounterClockWise();
        firstPos = this.getBlockPos().above().relative(counter, this.x_size - 1).above(this.y_size - 1);
        Direction clock = this.getBlockState().getValue(MachineBlock.FACING).getClockWise();
        secondPos = this.getBlockPos().above().relative(clock, this.x_size - 1);
    }

    public BlockPos getFirstPos() {
        return firstPos;
    }
    public BlockPos getSecondPos() {
        return secondPos;
    }

    /**
     * Returns true if this block is protected by the holo door.
     */
    public static boolean testBlock(Level level, BlockPos pos, BlockState state) {
        if(!(level instanceof ServerLevel serverLevel)) return false;
        ChunkProtectedBlocks blocks = level.getChunkAt(pos).getCapability(ChunkProtectedBlocks.CAP).orElse(null);
        if(blocks == null) return false;
        return blocks.contains(pos);

    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if(this.level instanceof ServerLevel serverLevel) {
            HoloDoorSavedData data = HoloDoorSavedData.getOrLoad(serverLevel);
            data.remove(this.uuid);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(DATA_X_SIZE_KEY, x_size);
        tag.putInt(DATA_Y_SIZE_KEY, y_size);
        tag.putBoolean(DATA_VISIBLE_KEY, visible);
        tag.putUUID(DATA_UUID_KEY, uuid);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        x_size = tag.getInt(DATA_X_SIZE_KEY);
        y_size = tag.getInt(DATA_Y_SIZE_KEY);
        visible = tag.getBoolean(DATA_VISIBLE_KEY);
        uuid = tag.getUUID(DATA_UUID_KEY);
    }

    @Override
    public RedstoneControl getRedstoneControl() {
        return REDSTONE;
    }

    @Override
    public List<ConfigurationEntry> getDefaultConfig() {
        return SIDE_CONFIG;
    }

    @Override
    public WrappedBlockEnergyContainer getEnergyStorage() {
        if (this.energyContainer == null)
        {
            this.energyContainer = new WrappedBlockEnergyContainer(this, new WrappedBlockEnergyContainer(this, new InsertOnlyEnergyContainer(20000, 20000)));
        }

        return this.energyContainer;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{};
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }
}
