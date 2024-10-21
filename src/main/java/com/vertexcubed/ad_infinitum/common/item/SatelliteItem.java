package com.vertexcubed.ad_infinitum.common.item;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.server.capability.SatelliteItemStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SatelliteItem extends Item {
    public SatelliteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        stack.getCapability(SatelliteItemStorage.CAP).ifPresent(satelliteStorage -> {
            if(satelliteStorage.getSatellite() == null) {
                satelliteStorage.setSatellite(initializeSatellite(stack, level, player), player, slotIndex);
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        SatelliteItemStorage storage = pStack.getCapability(SatelliteItemStorage.CAP).orElse(null);
        if(storage == null) return;

        if(storage.getSatellite() == null) {
            tooltip.add(Component.translatable("tooltip.ad_infinitum.empty_satellite").withStyle(ChatFormatting.RED));
        }
        else {
            tooltip.add(Component.literal(storage.getSatellite().toString()).withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    @Nullable
    public Satellite getSatellite(ItemStack stack) {
        SatelliteItemStorage storage = stack.getCapability(SatelliteItemStorage.CAP).orElse(null);
        if(storage == null) return null;
        return storage.getSatellite();
    }

    protected abstract Satellite initializeSatellite(ItemStack stack, Level level, Player player);
}
