package com.vertexcubed.ad_infinitum.client.screen;

import com.vertexcubed.ad_infinitum.common.blockentity.HoloDoorBlockEntity;
import com.vertexcubed.ad_infinitum.common.menu.HoloDoorMenu;
import com.vertexcubed.ad_infinitum.server.network.PacketHandler;
import com.vertexcubed.ad_infinitum.server.network.ServerboundUpdateHoloDoorSizePacket;
import earth.terrarium.adastra.client.components.PressableImageButton;
import earth.terrarium.adastra.client.screens.base.MachineScreen;
import earth.terrarium.adastra.client.utils.GuiUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class HoloDoorScreen extends MachineScreen<HoloDoorMenu, HoloDoorBlockEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("ad_astra", "textures/gui/container/etrionic_blast_furnace.png");
    public static final ResourceLocation SLOT = new ResourceLocation("ad_astra", "textures/gui/container/slots/steel.png");

    public HoloDoorScreen(HoloDoorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component, TEXTURE, SLOT, 177, 201);
    }

    @Override
    protected void init() {
        super.init();
        addSizeButton(ServerboundUpdateHoloDoorSizePacket.OpCode.DECREASE_X, leftPos + 10, topPos + 20);
        addSizeButton(ServerboundUpdateHoloDoorSizePacket.OpCode.INCREASE_X, leftPos + 40, topPos + 20);
        addSizeButton(ServerboundUpdateHoloDoorSizePacket.OpCode.DECREASE_Y, leftPos + 10, topPos + 50);
        addSizeButton(ServerboundUpdateHoloDoorSizePacket.OpCode.INCREASE_Y, leftPos + 40, topPos + 50);

    }

    private void addSizeButton(ServerboundUpdateHoloDoorSizePacket.OpCode opCode, int x, int y) {
        ResourceLocation texture = switch (opCode) {
            case INCREASE_X -> GuiUtils.SETTINGS_BUTTON;
            case DECREASE_X -> GuiUtils.SETTINGS_BUTTON;
            case INCREASE_Y -> GuiUtils.SETTINGS_BUTTON;
            case DECREASE_Y -> GuiUtils.SETTINGS_BUTTON;
        };
        addRenderableWidget(new HoloDoorSizeButton(opCode, x, y, 0, 0, 18, texture, 18, 54));
    }


    public class HoloDoorSizeButton extends PressableImageButton {

        private final ServerboundUpdateHoloDoorSizePacket.OpCode opcode;
        public HoloDoorSizeButton(ServerboundUpdateHoloDoorSizePacket.OpCode opcode, int x, int y, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, int textureWidth, int textureHeight) {
            super(x, y, 18, 18, xTexStart, yTexStart, yDiffTex, resourceLocation, textureWidth, textureHeight, b -> {
                entity.changeSize(opcode);
                PacketHandler.CHANNEL.sendToServer(new ServerboundUpdateHoloDoorSizePacket(entity.getBlockPos(), ServerboundUpdateHoloDoorSizePacket.OpCode.INCREASE_X));
            });
            this.opcode = opcode;
        }

        public ServerboundUpdateHoloDoorSizePacket.OpCode getOpcode() {
            return opcode;
        }
    }
}
