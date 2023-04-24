package com.github.jarva.arsartifice.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class PlainTextLabel extends AbstractWidget {
    private final Font font;
    private final Component message;
    private final int color;
    public PlainTextLabel(int x, int y, int w, int h, Component component, Font font, int color) {
        super(x, y, w, h, component);
        this.font = font;
        this.message = component;
        this.color = color;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.font.draw(poseStack, this.message, this.x, this.y, this.color);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
