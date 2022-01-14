package de.keksuccino.spiffyhud.customization.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRenderUtils extends DrawableHelper {

    public static void renderGuiItem(int x, int y, float partial, ItemStack stack, float scale, boolean renderStackSize, boolean renderDurability) {

        MinecraftClient mc = MinecraftClient.getInstance();

        x = (int) (x / scale);
        y = (int) (y / scale);

        if (!stack.isEmpty()) {

            MatrixStack posestack = RenderSystem.getModelViewStack();

            //Scale the item element
            posestack.scale(scale, scale, scale);

            float f = (float)stack.getBobbingAnimationTime() - partial;
            if (f > 0.0F) {
                float f1 = 1.0F + f / 5.0F;
                posestack.push();
                posestack.translate((x + 8), (y + 12), 0.0D);
                posestack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                posestack.translate((-(x + 8)), (-(y + 12)), 0.0D);
                RenderSystem.applyModelViewMatrix();
            }

            //Renders the actual item and effects(?)
            mc.getItemRenderer().renderInGui(stack, x, y);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            if (f > 0.0F) {
                posestack.pop();
                RenderSystem.applyModelViewMatrix();
            }

            //Renders the stack size and durab bar
            posestack.push();
            posestack.scale(scale, scale, scale);
            mc.getItemRenderer().renderGuiItemOverlay(mc.textRenderer, stack, x, y);
            posestack.pop();

        }

    }

    public static Item getItemByName(String name) {
        Item i = null;
        if (name.contains(":")) {
            String domain = name.split("[:]", 2)[0];
            String nameItem = name.split("[:]", 2)[1];
            i = Registry.ITEM.get(new Identifier(domain, nameItem));
            if (i != null) {
                return i;
            }
        }
        i = Registry.ITEM.get(new Identifier(name));
        return i;
    }

}
