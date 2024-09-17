package com.vertexcubed.ad_infinitum.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import earth.terrarium.adastra.client.components.PressableImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class TextButton extends PressableImageButton {
    public static final ResourceLocation TEXTURE = modLoc("textures/gui/sprites/text_button.png");

    private Font font;
    private Component component;
    public TextButton(int x, int y, int width, Component component, OnPress onPress) {
        super(x, y, width, 18, 0, 0, 18, TEXTURE, 96, 54, onPress);
        this.component = component;
        this.font = Minecraft.getInstance().font;
    }

    @Override
    public void renderTexture(@NotNull GuiGraphics graphics, @NotNull ResourceLocation texture, int x, int y, int uOffset, int vOffset, int textureDifference, int width, int height, int textureWidth, int textureHeight) {
        int v = vOffset + textureDifference;
        if (!this.isActive()) {
            v = vOffset + textureDifference * 2;
        } else if (this.isFocused()) {
            v = vOffset;
        } else if (this.isHovered()) {
            v = vOffset - textureDifference;
        }

        RenderSystem.enableDepthTest();
        graphics.blit(TEXTURE, x, y, uOffset, v, Mth.floor((float) this.width / 2), height, textureWidth, textureHeight);
        graphics.blit(TEXTURE, x + Mth.floor((float) this.width / 2), y, uOffset + this.textureWidth - Mth.ceil((float) this.width / 2), v, Mth.ceil((float) this.width / 2), height, textureWidth, textureHeight);
        renderLabel(graphics, x, y);
    }

    public void renderLabel(GuiGraphics graphics, int x, int y) {
        graphics.drawString(font, component, x + 5, y + Mth.ceil((this.height - font.lineHeight) / 2.0), 2762283, false);
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
