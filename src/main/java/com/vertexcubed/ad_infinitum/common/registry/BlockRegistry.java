package com.vertexcubed.ad_infinitum.common.registry;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.block.HoloDoorBlock;
import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AdInfinitum.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AdInfinitum.MODID);


    public static final RegistryObject<Block> HOLO_DOOR_BLOCK = BLOCKS.register("holo_door", () -> new HoloDoorBlock(
            BlockBehaviour.Properties.of()
                    .strength(3.0f)
            ))
    ;

    public static final RegistryObject<BlockEntityType<HoloDoorBlockEntity>> HOLO_DOOR_BLOCK_ENTITY =
            BLOCK_ENTITY_TYPES.register("holo_door_block_entity", () -> BlockEntityType.Builder.of(HoloDoorBlockEntity::new, HOLO_DOOR_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
    }

    public static void registerBlockItems(final RegisterEvent event) {
        BLOCKS.getEntries().forEach(block -> {
            event.register(ForgeRegistries.Keys.ITEMS,
                    helper -> helper.register(new ResourceLocation(Objects.requireNonNull(block.getId().toString())), new BlockItem(block.get(), new Item.Properties())));
        });
    }
}
