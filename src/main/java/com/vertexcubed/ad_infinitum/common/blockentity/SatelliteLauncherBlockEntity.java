package com.vertexcubed.ad_infinitum.common.blockentity;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.multiblock.Multiblock;
import com.vertexcubed.ad_infinitum.common.registry.BlockRegistry;
import com.vertexcubed.ad_infinitum.common.util.ModEnergyStorage;
import earth.terrarium.adastra.common.blockentities.base.EnergyContainerMachineBlockEntity;
import earth.terrarium.adastra.common.blockentities.base.sideconfig.ConfigurationEntry;
import earth.terrarium.botarium.common.energy.impl.InsertOnlyEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class SatelliteLauncherBlockEntity extends EnergyContainerMachineBlockEntity {

    public static final ResourceLocation MULTIBLOCK_ID = modLoc("satellite_launcher");



    private Multiblock multiblock;

    public SatelliteLauncherBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, 5);

    }

    @Override
    public void firstTick(Level level, BlockPos pos, BlockState state) {
        super.firstTick(level, pos, state);
        Optional<Registry<Multiblock>> multiblockRegistry = level.registryAccess().registry(Multiblock.REGISTRY);
        if(multiblockRegistry.isEmpty()) {
            AdInfinitum.LOGGER.info("Multiblock registry is not present!");
            return;
        }
        multiblock = multiblockRegistry.get().get(MULTIBLOCK_ID);

        AdInfinitum.LOGGER.info(multiblock == null ? "multiblock is null!" : multiblock.toString());
    }

    @Override
    public List<ConfigurationEntry> getDefaultConfig() {
        return List.of();
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
    public int[] getSlotsForFace(Direction pSide) {
        return new int[0];
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }
}
