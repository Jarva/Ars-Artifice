package com.github.jarva.arsartifice.client;

import com.github.jarva.arsartifice.ArsArtifice;
import com.github.jarva.arsartifice.client.gui.CraftingButton;
import com.github.jarva.arsartifice.client.gui.GlyphButton;
import com.github.jarva.arsartifice.client.gui.PlainTextLabel;
import com.github.jarva.arsartifice.glyphs.AbstractArtificeMethod;
import com.github.jarva.arsartifice.validators.CombinedArtificeValidator;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.client.gui.book.BaseBook;
import com.hollingsworth.arsnouveau.client.gui.buttons.CreateSpellButton;
import com.hollingsworth.arsnouveau.client.gui.buttons.GuiImageButton;
import com.hollingsworth.arsnouveau.common.capability.CapabilityRegistry;
import com.hollingsworth.arsnouveau.common.capability.IPlayerCap;
import com.hollingsworth.arsnouveau.common.network.Networking;
import com.hollingsworth.arsnouveau.common.network.PacketUpdateCaster;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ArtificeCreationMenu extends BaseBook {
    public final ISpellValidator spellValidator;
    private InteractionHand hand;
    private ItemStack held;
    public List<AbstractSpellPart> unlockedSpells;
    public int page = 0;
    public int glyphsPerPage = 58;
    public List<CraftingButton> craftingCells = new ArrayList<>();
    public List<GlyphButton> glyphButtons = new ArrayList<>();
    public PageButton nextButton;
    public PageButton prevButton;

    public int PER_ROW = 6;
    public int PER_PAGE = 6;

    public final static Map<Integer, Component> typeIndexLabels = new HashMap<>();

    static {
        typeIndexLabels.put(1, Component.translatable("ars_nouveau.spell_book_gui.form"));
        typeIndexLabels.put(5, Component.translatable("ars_nouveau.spell_book_gui.augment"));
    }

    public ArtificeCreationMenu(InteractionHand hand) {
        this.hand = hand;
        this.api = ArsNouveauAPI.getInstance();
        IPlayerCap cap = CapabilityRegistry.getPlayerDataCap(Minecraft.getInstance().player).orElse(null);
        unlockedSpells = cap == null ? new ArrayList<>() : new ArrayList<>(cap.getKnownGlyphs().stream().filter(AbstractSpellPart::isEnabled).toList());
        unlockedSpells.addAll(api.getDefaultStartingSpells());
        unlockedSpells = unlockedSpells.stream()
                .filter(s -> s instanceof AbstractArtificeMethod || s instanceof AbstractAugment)
                .collect(Collectors.toList());
        unlockedSpells = unlockedSpells.stream()
                .filter(s -> {
                    if (s instanceof AbstractAugment) {
                        return unlockedSpells.stream().anyMatch(part -> part.compatibleAugments.contains(s));
                    }
                    return true;
                })
                .collect(Collectors.toList());

        held = Minecraft.getInstance().player.getItemInHand(hand);
        this.spellValidator = new CombinedArtificeValidator();
    }

    public void init() {
        super.init();

        for (int i = 0; i < 10; ++i) {
            int offset = i >= 5 ? 14 : 0;
            CraftingButton cell = new CraftingButton(this, this.bookLeft + 19 + 24 * i + offset, this.bookTop + 194 - 47, i, this::onCraftingSlotClick);
            addRenderableWidget(cell);
            craftingCells.add(cell);
        }

        updateCraftingSlots(0);
        layoutAllGlyphs(page);

        addRenderableWidget(new CreateSpellButton(this, this.bookRight - 71, this.bookBottom - 13, this::onCreateClick));
        addRenderableWidget(new GuiImageButton(this.bookRight - 126, this.bookBottom - 13, 0, 0, 41, 12, 41, 12, "textures/gui/clear_icon.png", this::clear));

        nextButton = addRenderableWidget(new PageButton(this.bookRight - 20, this.bookBottom - 10, true, (btn) -> changePage(1), true));
        prevButton = addRenderableWidget(new PageButton(this.bookLeft - 5, this.bookBottom - 10, true, (btn) -> changePage(-1), true));

        updatePageButtons();
        validate();
    }

    public int getNumPages() {
        return (int) Math.ceil(this.unlockedSpells.size() / (double) glyphsPerPage);
    }

    public void updatePageButtons() {
        if (page == 0) {
            this.prevButton.active = false;
            this.prevButton.visible = false;
        }
        if (page == getNumPages() - 1 || getNumPages() <= 2) {
            this.nextButton.active = false;
            this.nextButton.visible = false;
        }
    }

    public void changePage(int val) {
        int newPage = page + val;
        if (newPage > getNumPages()) return;
        if (newPage < 0) return;

        page = newPage;
        updatePageButtons();
        layoutAllGlyphs(page);
        validate();
    }

    public void clear(Button button) {
        for (CraftingButton slot : craftingCells) {
            slot.clear();
        }

        validate();
    }

    public void validate() {
        List<AbstractSpellPart> recipe = craftingCells.stream().filter(b -> b.spellTag != ArsNouveauAPI.EMPTY_KEY).map(b -> this.api.getSpellpartMap().get(b.spellTag)).toList();
        List<SpellValidationError> errors = spellValidator.validate(recipe);

        for (SpellValidationError error : errors) {
            int pos = error.getPosition();
            if (pos >= 0 && pos < craftingCells.size()) {
                craftingCells.get(pos).validationErrors.add(error);
            }
        }

        for (GlyphButton glyphButton : glyphButtons) {
            validateGlyphButton(recipe, glyphButton);
        }
    }

    private void validateGlyphButton(List<AbstractSpellPart> recipe, GlyphButton glyphButton) {
        glyphButton.validationErrors.clear();
        List<AbstractSpellPart> changed = new ArrayList<>(recipe);
        changed.add(api.getSpellPart(glyphButton.abstractSpellPart.getRegistryName()));
        List<SpellValidationError> errors = spellValidator.validate(changed).stream().filter(err -> err.getPosition() >= changed.size() - 1).toList();
        glyphButton.validationErrors.addAll(errors);
    }

    public void onCreateClick(Button button) {
        this.validate();
        if (this.validationErrors.isEmpty()) {
            Spell spell = new Spell();
            craftingCells.stream().map(cell -> api.getSpellPart(cell.spellTag)).filter(Objects::nonNull).forEach(spell::add);
            Networking.INSTANCE.sendToServer(new PacketUpdateCaster(spell, 0, "Event", hand == InteractionHand.MAIN_HAND));
        }
    }

    public void updateCraftingSlots(int sel) {
        List<AbstractSpellPart> recipe = CasterUtil.getCaster(this.held).getSpell(sel).recipe;

        for(int i = 0; i < craftingCells.size(); ++i) {
            CraftingButton slot = craftingCells.get(i);
            slot.spellTag = ArsNouveauAPI.EMPTY_KEY;
            slot.abstractSpellPart = null;
            if (recipe != null && i < recipe.size()) {
                slot.spellTag = recipe.get(i).getRegistryName();
                slot.abstractSpellPart = recipe.get(i);
            }
        }
    }

    public void clearButtons(List<GlyphButton> glyphButtons) {
        for (GlyphButton glyphButton : glyphButtons) {
            renderables.remove(glyphButton);
            children().remove(glyphButton);
        }

        glyphButtons.clear();
    }

    public void layoutAllGlyphs(int page) {
        clearButtons(glyphButtons);

        List<AbstractSpellPart> filtered = new ArrayList<>(unlockedSpells);
        filtered.sort(COMPARE_TYPE_THEN_NAME);
        List<AbstractSpellPart> sorted = filtered.subList(glyphsPerPage * page, Math.min(filtered.size(), glyphsPerPage * (page + 1)));
        Map<Integer, List<AbstractSpellPart>> partitioned = sorted.stream().collect(Collectors.groupingBy(AbstractSpellPart::getTypeIndex));

        int yStart = bookTop + 20;
        int xStart = bookLeft + 20;
        int rows = 0;
        for (Map.Entry<Integer, List<AbstractSpellPart>> group : partitioned.entrySet()) {
            int index = group.getKey();
            List<AbstractSpellPart> glyphs = group.getValue();
            int labelX = xStart;
            int labelY = (rows * 18) + yStart + ((20-minecraft.font.lineHeight) / 2);
            PlainTextLabel label = new PlainTextLabel(labelX, labelY, 0, 0, typeIndexLabels.getOrDefault(index, Component.literal("Label")), minecraft.font, -8355712);
            addRenderableOnly(label);

            for (int i = 0; i < glyphs.size(); i++) {
                AbstractSpellPart part = glyphs.get(i);

                int col = i % PER_ROW;
                if (col == 0) rows++;
                boolean nextPage = rows >= PER_PAGE;
                int row = rows % PER_PAGE;
                int x = xStart + (nextPage ? 134 : 0) + (20 * col);
                int y = (row * 18) + yStart;

                ArsArtifice.LOGGER.info("Drawing Glyph {} at {} and {}, with values row {} col {} idx {}", part.getName(), x, y, rows, col, i);
                GlyphButton cell = new GlyphButton(this, x, y, false, part);
                addRenderableWidget(cell);
                glyphButtons.add(cell);
            }
            rows++;
        }
    }

    public void drawBackgroundElements(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.drawBackgroundElements(stack, mouseX, mouseY, partialTicks);
        drawFromTexture(new ResourceLocation("ars_nouveau", "textures/gui/clear_paper.png"), 161, 179, 0, 0, 47, 15, 47, 15, stack);
        drawFromTexture(new ResourceLocation("ars_nouveau", "textures/gui/create_paper.png"), 216, 179, 0, 0, 56, 15, 56, 15, stack);
        if (validationErrors.isEmpty()) {
            minecraft.font.draw(stack, Component.translatable("ars_nouveau.spell_book_gui.create"), 233.0F, 183.0F, -8355712);
        } else {
            Component textComponent = Component.translatable("ars_nouveau.spell_book_gui.create").withStyle((s) -> s.withStrikethrough(true).withColor(TextColor.parseColor("#FFB2B2")));
            minecraft.font.draw(stack, textComponent, 233.0F, 183.0F, -8355712);
        }

        minecraft.font.draw(stack, Component.translatable("ars_nouveau.spell_book_gui.clear").getString(), 177.0F, 183.0F, -8355712);
    }

    public void onCraftingSlotClick(Button button) {
        if (button instanceof CraftingButton craftingButton) {
            craftingButton.clear();
        }
        validate();
    }

    public void onGlyphClick(Button button) {
        if (button instanceof GlyphButton glyphButton) {
            if (!glyphButton.validationErrors.isEmpty()) return;
            Optional<CraftingButton> slot = craftingCells.stream().filter(cell -> cell.abstractSpellPart == null).findFirst();
            slot.ifPresent(cell -> {
                cell.abstractSpellPart = glyphButton.abstractSpellPart;
                cell.spellTag = glyphButton.abstractSpellPart.getRegistryName();
                validate();
            });
        }
    }
}
