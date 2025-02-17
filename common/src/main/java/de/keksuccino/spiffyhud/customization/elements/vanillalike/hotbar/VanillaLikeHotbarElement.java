package de.keksuccino.spiffyhud.customization.elements.vanillalike.hotbar;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeHotbarElement extends AbstractElement {

    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public VanillaLikeHotbarElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        int x = this.getAbsoluteX();
        int y = this.getAbsoluteY();
        int w = this.getAbsoluteWidth();
        int h = this.getAbsoluteHeight();

        ElementMobilizer.mobilize(graphics, -((getScreenWidth() / 2) - 91), -(getScreenHeight() - 22), x, y, () -> {

            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);

            if (Minecraft.getInstance().player == null) return;

            //-------------------------------

            this.renderHotbar(partial, graphics);

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

    }

    private void renderHotbar(float partial, GuiGraphics graphics) {

        float f;
        int o;
        int n;
        int m;
        Player player = this.getCameraPlayer();
        if (player == null) {
            return;
        }
        ItemStack itemStack = player.getOffhandItem();
        HumanoidArm humanoidArm = player.getMainArm().getOpposite();
        int i = getScreenWidth() / 2;
        int j = 182;
        int k = 91;
        graphics.pose().pushPose();
        //Tweak to Vanilla logic
        if (!isEditor()) {
            graphics.pose().translate(0.0f, 0.0f, -90.0f);
        }
        //--------------------
        graphics.blit(WIDGETS_LOCATION, i - 91, getScreenHeight() - 22, 0, 0, 182, 22);
        graphics.blit(WIDGETS_LOCATION, i - 91 - 1 + player.getInventory().selected * 20, getScreenHeight() - 22 - 1, 0, 22, 24, 22);
        //Tweak to Vanilla logic (isEditor)
        if (!isEditor()) {
            if (!itemStack.isEmpty()) {
                if (humanoidArm == HumanoidArm.LEFT) {
                    graphics.blit(WIDGETS_LOCATION, i - 91 - 29, getScreenHeight() - 23, 24, 22, 29, 24);
                } else {
                    graphics.blit(WIDGETS_LOCATION, i + 91, getScreenHeight() - 23, 53, 22, 29, 24);
                }
            }
        } else {
            // Render both sides in editor for better showing how wide the hotbar can be to both sides
            graphics.blit(WIDGETS_LOCATION, i - 91 - 29, getScreenHeight() - 23, 24, 22, 29, 24);
            graphics.blit(WIDGETS_LOCATION, i + 91, getScreenHeight() - 23, 53, 22, 29, 24);
        }
        //------------------
        graphics.pose().popPose();
        int l = 1;
        for (m = 0; m < 9; ++m) {
            n = i - 90 + m * 20 + 2;
            o = getScreenHeight() - 16 - 3;
            this.renderSlot(graphics, n, o, partial, player, player.getInventory().items.get(m), l++);
        }
        if (!itemStack.isEmpty()) {
            m = getScreenHeight() - 16 - 3;
            if (humanoidArm == HumanoidArm.LEFT) {
                this.renderSlot(graphics, i - 91 - 26, m, partial, player, itemStack, l++);
            } else {
                this.renderSlot(graphics, i + 91 + 10, m, partial, player, itemStack, l++);
            }
        }
        //Tweak to Vanilla logic
//        RenderSystem.enableBlend();
//        if (Minecraft.getInstance().options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR && (f = Minecraft.getInstance().player.getAttackStrengthScale(0.0f)) < 1.0f) {
//            n = getScreenHeight() - 20;
//            o = i + 91 + 6;
//            if (humanoidArm == HumanoidArm.RIGHT) {
//                o = i - 91 - 22;
//            }
//            int p = (int)(f * 19.0f);
//            graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, o, n, 18, 18);
//            graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - p, o, n + 18 - p, 18, p);
//        }
//        RenderSystem.disableBlend();
        //----------------------
    }

    private void renderSlot(GuiGraphics guiGraphics, int x, int y, float partialTick, Player player, ItemStack stack, int seed) {
        if (stack.isEmpty()) {
            return;
        }
        float f = (float)stack.getPopTime() - partialTick;
        if (f > 0.0f) {
            float g = 1.0f + f / 5.0f;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x + 8, y + 12, 0.0f);
            guiGraphics.pose().scale(1.0f / g, (g + 1.0f) / 2.0f, 1.0f);
            guiGraphics.pose().translate(-(x + 8), -(y + 12), 0.0f);
        }
        guiGraphics.renderItem(player, stack, x, y, seed);
        if (f > 0.0f) {
            guiGraphics.pose().popPose();
        }
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
    }

    @Nullable
    private Player getCameraPlayer() {
        return (Minecraft.getInstance().getCameraEntity() instanceof Player p) ? p : null;
    }

    @Override
    public int getAbsoluteWidth() {
        return 182;
    }

    @Override
    public int getAbsoluteHeight() {
        return 22;
    }

}
