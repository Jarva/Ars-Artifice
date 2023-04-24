package com.github.jarva.arsartifice.client.gui;

import com.github.jarva.arsartifice.client.ArtificeCreationMenu;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.SpellValidationError;
import com.hollingsworth.arsnouveau.client.gui.book.GuiSpellBook;
import com.hollingsworth.arsnouveau.client.gui.buttons.GuiImageButton;
import com.hollingsworth.arsnouveau.client.gui.utils.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CraftingButton extends GuiImageButton {
    public List<SpellValidationError> validationErrors = new LinkedList<>();
    int slotNum;
    public ResourceLocation spellTag;
    public AbstractSpellPart abstractSpellPart;

    public CraftingButton(ArtificeCreationMenu parent, int x, int y, int slotNum, Button.OnPress onPress) {
        super(x, y, 0, 0, 22, 20, 22, 20, "textures/gui/spell_glyph_slot.png", onPress);
        this.slotNum = slotNum;
        this.spellTag = ArsNouveauAPI.EMPTY_KEY;
        this.resourceIcon = "";
        this.parent = parent;
    }

    public void clear() {
        this.spellTag = ArsNouveauAPI.EMPTY_KEY;
        this.resourceIcon = "";
        this.validationErrors.clear();
        this.abstractSpellPart = null;
    }

    @Override
    public void render(PoseStack ms, int parX, int parY, float partialTicks) {
        if (visible) {
            if (validationErrors.isEmpty()) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                RenderSystem.setShaderColor(1.0F, 0.7F, 0.7F, 1.0F);
            }
            if (this.abstractSpellPart != null) {
                RenderUtils.drawSpellPart(this.abstractSpellPart, ms, x + 3, y + 2, 16, !validationErrors.isEmpty());
            }
            if (parent.isMouseInRelativeRange(parX, parY, x, y, width, height)) {

                if (parent.api.getSpellpartMap().containsKey(this.spellTag)) {
                    List<Component> tooltip = new LinkedList<>();
                    tooltip.add(Component.translatable(parent.api.getSpellpartMap().get(this.spellTag).getLocalizationKey()));
                    for (SpellValidationError ve : validationErrors) {
                        tooltip.add(ve.makeTextComponentExisting().withStyle(ChatFormatting.RED));
                    }
                    parent.tooltip = tooltip;
                }
            }
        }
        super.render(ms, parX, parY, partialTicks);
    }
}
