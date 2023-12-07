package com.github.jarva.arsartifice.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawString(this.font, this.message, this.getX(), this.getY(), this.color, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
