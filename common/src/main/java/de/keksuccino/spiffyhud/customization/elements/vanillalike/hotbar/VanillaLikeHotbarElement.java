package de.keksuccino.spiffyhud.customization.elements.vanillalike.hotbar;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.spiffyhud.util.rendering.ItemRenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeHotbarElement extends AbstractElement {

    // Sprite resources for the hotbar in 1.21.5
    private static final ResourceLocation HOTBAR_SPRITE = ResourceLocation.withDefaultNamespace("hud/hotbar");
    private static final ResourceLocation HOTBAR_SELECTION_SPRITE = ResourceLocation.withDefaultNamespace("hud/hotbar_selection");
    private static final ResourceLocation HOTBAR_OFFHAND_LEFT_SPRITE = ResourceLocation.withDefaultNamespace("hud/hotbar_offhand_left");
    private static final ResourceLocation HOTBAR_OFFHAND_RIGHT_SPRITE = ResourceLocation.withDefaultNamespace("hud/hotbar_offhand_right");

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 22;

    public VanillaLikeHotbarElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    /**
     * Renders the hotbar element.
     * The hotbar background, selection highlight, offhand icons, and slots are all drawn relative to
     * the element's absolute position and size.
     */
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Get the absolute position and size of this element.
        int elementX = this.getAbsoluteX();
        int elementY = this.getAbsoluteY();
        int elementWidth = this.getAbsoluteWidth();   // Expected width: 182
        int elementHeight = this.getAbsoluteHeight(); // Expected height: 22

        // Only proceed if the player is available.
        if (Minecraft.getInstance().player == null) return;

        // Render the hotbar within the element's bounds.
        this.renderHotbar(graphics, partialTick, elementX, elementY, elementWidth, elementHeight);
    }

    /**
     * Renders the hotbar background, selection highlight, offhand icons, and item slots.
     *
     * @param graphics       The graphics context for rendering.
     * @param partialTick    The partial tick time for animations.
     * @param elementX       The X position of the element.
     * @param elementY       The Y position of the element.
     * @param elementWidth   The width of the element.
     * @param elementHeight  The height of the element.
     */
    private void renderHotbar(GuiGraphics graphics, float partialTick, int elementX, int elementY, int elementWidth, int elementHeight) {
        // Get the current player.
        Player player = this.getCameraPlayer();
        if (player == null) {
            return;
        }

        // Calculate color with opacity
        int color = ARGB.color(Math.round(this.opacity * 255f), 255, 255, 255);

        // Get the player's offhand item and determine the opposite of the main arm.
        ItemStack offhandItem = player.getOffhandItem();
        HumanoidArm oppositeMainArm = player.getMainArm().getOpposite();

        // Draw the hotbar background using the element's bounds.
        graphics.blitSprite(RenderType::guiTextured, HOTBAR_SPRITE, elementX, elementY, elementWidth, elementHeight, color);

        // Draw the selection highlight around the currently selected hotbar slot.
        int selectedSlot = player.getInventory().getSelectedSlot();
        // The highlight is drawn with a 1-pixel offset relative to the hotbar background.
        graphics.blitSprite(RenderType::guiTextured, HOTBAR_SELECTION_SPRITE, elementX - 1 + selectedSlot * 20, elementY - 1, 24, 23, color);

        // Render offhand icons if applicable.
        if (!isEditor()) {
            if (!offhandItem.isEmpty()) {
                if (oppositeMainArm == HumanoidArm.LEFT) {
                    // Render offhand icon on the left side of the hotbar.
                    graphics.blitSprite(RenderType::guiTextured, HOTBAR_OFFHAND_LEFT_SPRITE, elementX - 29, elementY - 1, 29, 24, color);
                } else {
                    // Render offhand icon on the right side of the hotbar.
                    graphics.blitSprite(RenderType::guiTextured, HOTBAR_OFFHAND_RIGHT_SPRITE, elementX + elementWidth, elementY - 1, 29, 24, color);
                }
            }
        } else {
            // In editor mode, display both offhand icons for demonstration purposes.
            graphics.blitSprite(RenderType::guiTextured, HOTBAR_OFFHAND_LEFT_SPRITE, elementX - 29, elementY - 1, 29, 24, color);
            graphics.blitSprite(RenderType::guiTextured, HOTBAR_OFFHAND_RIGHT_SPRITE, elementX + elementWidth, elementY - 1, 29, 24, color);
        }

        // Calculate starting positions for rendering the 9 hotbar slots.
        int slotStartX = elementX + 3; // 3 pixels padding from the left edge.
        int slotY = elementY + 3;      // 3 pixels padding from the top edge.
        int renderSeed = 1;

        // Render each of the 9 main hotbar slots.
        for (int slotIndex = 0; slotIndex < 9; slotIndex++) {
            int slotX = slotStartX + slotIndex * 20; // Each slot is spaced 20 pixels apart.
            renderSlot(graphics, slotX, slotY, partialTick, player, player.getInventory().getItem(slotIndex), renderSeed++, color);
        }

        // Render the offhand slot if there is an offhand item.
        if (!offhandItem.isEmpty()) {
            if (oppositeMainArm == HumanoidArm.LEFT) {
                renderSlot(graphics, elementX - 26, slotY, partialTick, player, offhandItem, renderSeed++, color);
            } else {
                renderSlot(graphics, elementX + elementWidth + 10, slotY, partialTick, player, offhandItem, renderSeed++, color);
            }
        }
    }

    /**
     * Renders a single item slot, including any pop animation and decorations.
     *
     * @param graphics      The graphics context for rendering.
     * @param slotX         The X position of the slot.
     * @param slotY         The Y position of the slot.
     * @param partialTick   The partial tick time for animations.
     * @param player        The current player.
     * @param stack         The item stack to render.
     * @param renderSeed    A seed value for randomized rendering effects.
     * @param color         The color to apply to the rendering.
     */
    private void renderSlot(GuiGraphics graphics, int slotX, int slotY, float partialTick, Player player, ItemStack stack, int renderSeed, int color) {
        // Do not render if the item stack is empty.
        if (stack.isEmpty()) {
            return;
        }

        // Determine if the item has a pop animation.
        float popTimeRemaining = (float) stack.getPopTime() - partialTick;
        if (popTimeRemaining > 0.0f) {
            // Calculate scaling factor based on the pop animation.
            float scaleFactor = 1.0f + popTimeRemaining / 5.0f;
            graphics.pose().pushPose();
            // Translate to the center of the slot.
            graphics.pose().translate(slotX + 8, slotY + 12, 0.0f);
            // Apply scaling transformation for the pop effect.
            graphics.pose().scale(1.0f / scaleFactor, (scaleFactor + 1.0f) / 2.0f, 1.0f);
            // Translate back to the original position.
            graphics.pose().translate(-(slotX + 8), -(slotY + 12), 0.0f);
        }

        // Render the item within the slot.
        ItemRenderingUtils.setItemOpacity(this.opacity);
        graphics.renderItem(player, stack, slotX, slotY, renderSeed);
        ItemRenderingUtils.resetItemOpacity();

        // If a pop animation was applied, revert the transformation.
        if (popTimeRemaining > 0.0f) {
            graphics.pose().popPose();
        }

        // Render additional item decorations such as count overlays.
        graphics.renderItemDecorations(Minecraft.getInstance().font, stack, slotX, slotY);
    }

    /**
     * Retrieves the current camera player.
     *
     * @return The current player if available, otherwise null.
     */
    @Nullable
    private Player getCameraPlayer() {
        return (Minecraft.getInstance().getCameraEntity() instanceof Player p) ? p : null;
    }

    @Override
    public int getAbsoluteWidth() {
        return BAR_WIDTH;
    }

    @Override
    public int getAbsoluteHeight() {
        return BAR_HEIGHT;
    }
}
