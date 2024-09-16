package com.vertexcubed.ad_infinitum.common.blockentity;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.menu.HoloDoorMenu;
import com.vertexcubed.ad_infinitum.common.menu.SatelliteLauncherMenu;
import com.vertexcubed.ad_infinitum.common.multiblock.Multiblock;
import com.vertexcubed.ad_infinitum.common.multiblock.data.GenericMachineData;
import com.vertexcubed.ad_infinitum.common.util.InternalOnlyEnergyContainer;
import earth.terrarium.adastra.common.blockentities.base.EnergyContainerMachineBlockEntity;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.ConfigurationEntry;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class SatelliteLauncherBlockEntity extends EnergyContainerMachineBlockEntity {

    public static final ResourceLocation MULTIBLOCK_ID = modLoc("satellite_launcher");
    public static final int MAX_TRANSFER = 20000;


    private Multiblock multiblock;

    private boolean isFormed = false;
    private final List<BlockPos> energyInputs = new ArrayList<>();

    public SatelliteLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, 5);

    }

    @Override
    public void firstTick(Level level, BlockPos pos, BlockState state) {
        super.firstTick(level, pos, state);
        Optional<Registry<Multiblock>> multiblockRegistry = level.registryAccess().registry(Multiblock.REGISTRY);
        if(multiblockRegistry.isEmpty()) {
            AdInfinitum.LOGGER.error("Multiblock registry is not present!");
            return;
        }
        multiblock = multiblockRegistry.get().get(MULTIBLOCK_ID);
        MinecraftForge.EVENT_BUS.register(this);

        if(isFormed) {
            checkForMultiblock(level, pos);
        }
    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        super.serverTick(level, time, state, pos);
        if(!isFormed) return;
        energyInputs.forEach(relativePos -> {
            BlockPos energyInput = relativePos.offset(pos);
            BlockEntity blockEntity = level.getBlockEntity(energyInput);
            if(blockEntity != null) {
                //todo: get rid of Direction.UP
                blockEntity.getCapability(ForgeCapabilities.ENERGY, Direction.UP).ifPresent(energy -> {

                    long extracted = energy.extractEnergy(MAX_TRANSFER, true);
                    if(extracted == 0) return;
                    int inserted = (int) getEnergyStorage().internalInsert(extracted, true);
                    if(inserted == 0) return;
                    getEnergyStorage().internalInsert(energy.extractEnergy(inserted, false), false);
                });
            }

        });


    }

    @Override
    public List<ConfigurationEntry> getDefaultConfig() {
        return List.of();
    }

    @Override
    public WrappedBlockEnergyContainer getEnergyStorage() {
        if (this.energyContainer == null)
        {
            this.energyContainer = new WrappedBlockEnergyContainer(this, new WrappedBlockEnergyContainer(this, new InternalOnlyEnergyContainer(200000, MAX_TRANSFER)));
        }

        return this.energyContainer;
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return new int[0];
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SatelliteLauncherMenu(pContainerId, pPlayerInventory, this);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(multiblock == null) {
            AdInfinitum.LOGGER.warn("Multiblock is null!");
            return InteractionResult.FAIL;
        }

        if(!isFormed) {
            checkForMultiblock(level, pos);
        }
        else {
            AdInfinitum.LOGGER.info("Multiblock already formed!");
        }


        return InteractionResult.SUCCESS;
    }

    private void checkForMultiblock(Level level, BlockPos pos) {
        Multiblock.TestResult result = multiblock.test(level, pos);
        AdInfinitum.LOGGER.info("Multiblock found: " + result.found());

        if(!result.found()) return;
        isFormed = true;
        setChanged();


        if(multiblock.getData().isPresent() && multiblock.getData().get() instanceof GenericMachineData data) {
            AdInfinitum.LOGGER.info("Data found!");
            List<BlockPos> relative = data.getDataPositions(GenericMachineData.DataType.ENERGY_IN, result.rotation());
            energyInputs.clear();
            energyInputs.addAll(relative);
            AdInfinitum.LOGGER.info("Positions: " + relative.toString());
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("isFormed", isFormed);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        isFormed = tag.getBoolean("isFormed");
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockEvent.NeighborNotifyEvent event) {
        if(!this.isFormed || multiblock == null) return;
        BlockPos pos = event.getPos().subtract(this.worldPosition.subtract(multiblock.getCenter()));
        if(multiblock.inBounds(pos)) {
            Multiblock.TestResult result = multiblock.test(level, this.worldPosition);
            if(!result.found()) {
                BlockState oldState = event.getState();
                isFormed = false;
                setChanged();
                Level level = ((Level) event.getLevel());
                level.sendBlockUpdated(this.worldPosition, oldState, level.getBlockState(this.worldPosition), 3);
            }
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
