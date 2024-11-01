package com.vertexcubed.ad_infinitum.common.blockentity;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.item.SatelliteItem;
import com.vertexcubed.ad_infinitum.common.menu.SatelliteLauncherMenu;
import com.vertexcubed.ad_infinitum.common.multiblock.Multiblock;
import com.vertexcubed.ad_infinitum.common.multiblock.MultiblockEventListener;
import com.vertexcubed.ad_infinitum.common.multiblock.data.GenericMachineData;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.common.satellite.SatelliteManager;
import com.vertexcubed.ad_infinitum.common.util.InternalOnlyEnergyContainer;
import earth.terrarium.adastra.common.blockentities.base.EnergyContainerMachineBlockEntity;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.Configuration;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.ConfigurationEntry;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.ConfigurationType;
import earth.terrarium.adastra.common.constants.ConstantComponents;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
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

    public static final List<ConfigurationEntry> SIDE_CONFIG = List.of(
            new ConfigurationEntry(ConfigurationType.ENERGY, Configuration.NONE, ConstantComponents.SIDE_CONFIG_ENERGY)
    );

    public static final ResourceLocation MULTIBLOCK_ID = modLoc("satellite_launcher");
    public static final int MAX_TRANSFER = 32767;
    public static final int LAUNCH_ENERGY = 1024;

    private Multiblock multiblock;

    private boolean isFormed = false;
    private final List<BlockPos> energyInputs = new ArrayList<>();

    protected int launchTime;
    protected int launchTimeTotal;
    protected boolean launching;
    public SatelliteLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, 5);

        launchTime = 0;
        launchTimeTotal = 100;
        launching = false;
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

    public boolean isFormed() {
        return isFormed;
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

        if(canFunction() && launching) {
            launchTick(level, this.getEnergyStorage());
        }
    }

    public void launchTick(Level level, WrappedBlockEnergyContainer energyStorage) {
        if(energyStorage.internalExtract(LAUNCH_ENERGY, true) < LAUNCH_ENERGY) {
            failLaunch(level);
            return;
        }
        energyStorage.internalExtract(LAUNCH_ENERGY, false);
        launchTime++;

        if(launchTime >= launchTimeTotal) {
            this.launchSatellites(level);
        }
    }

    public void startLaunching() {
        this.launching = true;
    }

    public void launchSatellites(Level level) {
        launching = false;
        launchTime = 0;

        AdInfinitum.LOGGER.info("Launching satellites, client: " + level.isClientSide);
        if(level instanceof ServerLevel serverLevel) {
            for(int i = 0; i < items().size(); i++) {
                if(!(items().get(i).getItem() instanceof SatelliteItem satelliteItem)) continue;
                Satellite satellite = satelliteItem.getSatellite(items().get(i));
                if(satellite == null) continue;
                SatelliteManager.addSatellite(serverLevel, satellite);
                this.setItem(i, ItemStack.EMPTY);
            }
        }
        this.setChanged();
    }

    public void failLaunch(Level level) {
        launching = false;
        launchTime = 0;
    }



    @Override
    public List<ConfigurationEntry> getDefaultConfig() {
        return SIDE_CONFIG;
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

    public boolean attemptToForm(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(multiblock == null) {
            AdInfinitum.LOGGER.warn("Multiblock for BE" + this + " is null, cannot form!");
            return false;
        }

        if(!isFormed) {
            checkForMultiblock(level, pos);
        }

        return isFormed;
    }

    private void checkForMultiblock(Level level, BlockPos pos) {
        Multiblock.TestResult result = multiblock.test(level, pos);

        if(!result.found()) return;
        isFormed = true;
        setChanged();


        if(multiblock.getData().isPresent() && multiblock.getData().get() instanceof GenericMachineData data) {
            List<BlockPos> relative = data.getDataPositions(GenericMachineData.DataType.ENERGY_IN, result.rotation());
            energyInputs.clear();
            energyInputs.addAll(relative);
        }

        this.sendFormEvent(result, level, pos);
    }

    private void sendFormEvent(Multiblock.TestResult result, Level level, BlockPos pos) {
        result.eventListeners().forEach((listenerPos, listener) -> listener.onMultiblockForm(level, this.multiblock, level.getBlockState(listenerPos), pos));
    }

    private void sendUnformEvent(Level level, BlockPos pos) {

    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("isFormed", isFormed);
        tag.putInt("launchTime", launchTime);
        tag.putInt("launchTimeTotal", launchTimeTotal);
        tag.putBoolean("launching", launching);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        isFormed = tag.getBoolean("isFormed");
        launchTime = tag.getInt("launchTime");
        launchTimeTotal = tag.getInt("launchTimeTotal");
        launching = tag.getBoolean("launching");
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockEvent.NeighborNotifyEvent event) {
        if(!this.isFormed || multiblock == null) return;
        BlockPos pos = event.getPos().subtract(this.worldPosition.subtract(multiblock.getCenter()));
        if (!multiblock.inBounds(pos)) return;


        Multiblock.TestResult result = multiblock.test(level, this.worldPosition);
        if(!result.found()) {
            BlockState oldState = event.getState();
            isFormed = false;
            setChanged();
            Level level = ((Level) event.getLevel());
            level.sendBlockUpdated(this.worldPosition, oldState, level.getBlockState(this.worldPosition), 3);
            this.sendUnformEvent(level, this.worldPosition);
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
