package com.vertexcubed.ad_infinitum.client.screen;

import com.vertexcubed.ad_infinitum.AdInfinitum;
import com.vertexcubed.ad_infinitum.common.blockentity.SatelliteLauncherBlockEntity;
import com.vertexcubed.ad_infinitum.common.item.SatelliteItem;
import com.vertexcubed.ad_infinitum.common.menu.SatelliteLauncherMenu;
import com.vertexcubed.ad_infinitum.common.menu.SatelliteSlot;
import com.vertexcubed.ad_infinitum.common.satellite.Satellite;
import com.vertexcubed.ad_infinitum.server.capability.SatelliteItemStorage;
import earth.terrarium.adastra.client.components.PressableImageButton;
import earth.terrarium.adastra.client.components.base.ContainerWidget;
import earth.terrarium.adastra.client.components.machines.OptionsBarWidget;
import earth.terrarium.adastra.client.screens.base.ConfigurationScreen;
import earth.terrarium.adastra.client.screens.base.MachineScreen;
import earth.terrarium.adastra.common.menus.slots.InventorySlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class SatelliteLauncherScreen extends MachineScreen<SatelliteLauncherMenu, SatelliteLauncherBlockEntity> {

    public static final ResourceLocation TEXTURE = modLoc("textures/gui/container/satellite_launcher.png");
    public static final ResourceLocation SLOT = new ResourceLocation("ad_astra", "textures/gui/container/slots/steel.png");

    private EditSatellite editSatellite;
    private final ResourceLocation texture;
    private final Component name;
    public SatelliteLauncherScreen(SatelliteLauncherMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, Component.empty(), TEXTURE, SLOT, 176, 184);
        this.texture = TEXTURE;
        this.name = component;
    }

    @Override
    protected void init() {
        super.init();
        editSatellite = new EditSatellite(this, leftPos + 9, topPos + 80);
        this.addRenderableWidget(editSatellite);
        this.addRenderableWidget(new TextButton(leftPos + 9, topPos + 53, 58, Component.literal("Configure"), b -> {
            editSatellite.setActive(!editSatellite.isActive());
            this.menu.slots.stream().filter(s -> s instanceof InventorySlot).forEach(slot -> {
                ((InventorySlot) slot).setActive(!editSatellite.isActive());
            });
        }));
        List<SatelliteConfigWidget> configSlots = new ArrayList<>();
        this.menu.slots.stream().filter(s -> s instanceof SatelliteSlot).forEach(slot -> {
            configSlots.add(this.addRenderableWidget(new SatelliteConfigWidget(this, slot.index, leftPos + slot.x, topPos + slot.y)));
        });
    }

    @Override
    public boolean canConfigure() {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {


        super.render(graphics, mouseX, mouseY, partialTick);
    }

    public EditSatellite getEditSatellite() {
        return editSatellite;
    }


    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 2762283, false);
        if(!this.editSatellite.isActive()) {
            graphics.drawString(this.font, name, this.inventoryLabelX, this.inventoryLabelY, 2762283, false);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        graphics.blit(this.texture, left, top, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    public static class EditSatellite extends ContainerWidget {

        public static final ResourceLocation TEXTURE = modLoc("textures/gui/sprites/satellite_edit.png");


        private int activeIndex = 0;
        private final SatelliteLauncherScreen screen;
        public EditSatellite(SatelliteLauncherScreen screen, int x, int y) {
            super(x, y, 155, 96);
            this.setActive(false);
            this.screen = screen;
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if(!isActive()) return;
            graphics.blit(TEXTURE, x, y, 0, 0, width, height, width, height);
            super.render(graphics, mouseX, mouseY, partialTicks);

            Slot slot = screen.getMenu().getSlot(activeIndex);
            if(slot != null && !slot.getItem().isEmpty() && slot.getItem().getItem() instanceof SatelliteItem satelliteItem) {
                ItemStack stack = slot.getItem();
                Satellite satellite = satelliteItem.getSatellite(stack);
                if(satellite != null) {
//                AdInfinitum.LOGGER.info("Satellite: " + satellite);
                    Font font = Minecraft.getInstance().font;
                    graphics.enableScissor(x + 85, y, x + width - 2, y + height - 2);

                    graphics.pose().pushPose();
                    graphics.pose().translate(x + 88, y + 6, 0.0);
                    graphics.pose().scale(0.75f, 0.75f, 0.75f);
                    List<Satellite.InfoField<?>> toDraw = satellite.getInformation();
                    for(int i = 0; i < toDraw.size(); i++) {
                        Component firstPart = MutableComponent.create(toDraw.get(i).getName().getContents()).append(": ");
                        Component secondPart = toDraw.get(i).getType();
                        graphics.drawString(font, firstPart, 0, i * 12, 0xff8bf5f5, false);
                        graphics.drawString(font, secondPart, font.width(firstPart), i * 12, 0xff8bf5f5, false);
//                        graphics.drawString(font, toDraw.get(i), 0, (i * 12), 0xff8bf5f5, false);

                    }
                    graphics.pose().popPose();
                    graphics.disableScissor();
                }
            }

        }

        public void setActiveIndex(int activeIndex) {
            this.activeIndex = activeIndex;
        }

        public int getActiveIndex() {
            return activeIndex;
        }
    }

    public static class SatelliteConfigWidget extends AbstractWidget {

        private final SatelliteLauncherScreen screen;
        private final int index;
        public SatelliteConfigWidget(SatelliteLauncherScreen screen, int index, int pX, int pY) {
            super(pX, pY, 16, 16, Component.empty());
            this.screen = screen;
            this.index = index;
        }

        @Override
        public void onClick(double pMouseX, double pMouseY) {
            screen.getEditSatellite().setActiveIndex(index);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
            boolean isCorrectSlot = screen.getEditSatellite().getActiveIndex() == this.index;
            int color = isCorrectSlot ? -16711936 : -10066177;
            graphics.renderOutline(this.getX() - 1, this.getY() - 1, this.width + 2, this.height + 2, color);

        }

        @Override
        public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
            this.visible = screen.getEditSatellite().isActive();
            if (this.visible) {
                renderWidget(graphics, pMouseX, pMouseY, pPartialTick);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {

        }
    }
}
