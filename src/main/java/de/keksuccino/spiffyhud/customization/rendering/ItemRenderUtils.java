package de.keksuccino.spiffyhud.customization.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRenderUtils extends GuiComponent {

    public static void renderGuiItem(int x, int y, float partial, ItemStack stack, float scale, boolean renderStackSize, boolean renderDurability) {

        Minecraft mc = Minecraft.getInstance();

        x = (int) (x / scale);
        y = (int) (y / scale);

        if (!stack.isEmpty()) {

            PoseStack matrix = RenderSystem.getModelViewStack();

            //-------------------- 1
            //Scale the item element
            matrix.pushPose();
            matrix.scale(scale, scale, scale);
            RenderSystem.applyModelViewMatrix();

            float f = (float)stack.getPopTime() - partial;
            if (f > 0.0F) {
                float f1 = 1.0F + f / 5.0F;
                matrix.pushPose();
                matrix.translate((x + 8), (y + 12), 0.0D);
                matrix.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                matrix.translate((-(x + 8)), (-(y + 12)), 0.0D);
                RenderSystem.applyModelViewMatrix();
            }

            //Renders the actual item and effects(?)
            mc.getItemRenderer().renderAndDecorateItem(stack, x, y);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            if (f > 0.0F) {
                matrix.popPose();
                RenderSystem.applyModelViewMatrix();
            }

            matrix.popPose();
            RenderSystem.applyModelViewMatrix();
            //-------------------- 1 end

            //-------------------- 2
            //Renders the stack size and durab bar
            matrix.pushPose();
            matrix.scale(scale, scale, scale);
            mc.getItemRenderer().renderGuiItemDecorations(mc.font, stack, x, y);
            RenderSystem.applyModelViewMatrix();
            matrix.popPose();
            RenderSystem.applyModelViewMatrix();
            //-------------------- 2 end

        }

    }

    public static Item getItemByName(String name) {
        Item i = null;
        if (name.contains(":")) {
            String domain = name.split("[:]", 2)[0];
            String nameItem = name.split("[:]", 2)[1];
            i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(domain, nameItem));
            if (i != null) {
                return i;
            }
        }
        i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
        return i;
    }

//    protected static void renderItemOverlayIntoGUI(Font fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text, boolean renderStackSize, boolean renderDurability) {
//        if (!stack.isEmpty()) {
//            PoseStack matrixstack = new PoseStack();
//            if ((stack.getCount() != 1 || text != null) && renderStackSize) {
//                String s = text == null ? String.valueOf(stack.getCount()) : text;
//                matrixstack.translate(0.0D, 0.0D, (double)(Minecraft.getInstance().getItemRenderer().blitOffset + 200.0F));
//                IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
//                fr.drawInBatch(s, (float)(xPosition + 19 - 2 - fr.width(s)), (float)(yPosition + 6 + 3), 16777215, true, matrixstack.last().pose(), irendertypebuffer$impl, false, 0, 15728880);
//                irendertypebuffer$impl.endBatch();
//            }
//
//            if (stack.getItem().showDurabilityBar(stack) && renderDurability) {
//                RenderSystem.disableDepthTest();
//                RenderSystem.disableTexture();
//                RenderSystem.disableAlphaTest();
//                RenderSystem.disableBlend();
//                Tessellator tessellator = Tessellator.getInstance();
//                BufferBuilder bufferbuilder = tessellator.getBuilder();
//                double health = stack.getItem().getDurabilityForDisplay(stack);
//                int i = Math.round(13.0F - (float)health * 13.0F);
//                int j = stack.getItem().getRGBDurabilityForDisplay(stack);
//                draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
//                draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
//                RenderSystem.enableBlend();
//                RenderSystem.enableAlphaTest();
//                RenderSystem.enableTexture();
//                RenderSystem.enableDepthTest();
//            }
//
//            LocalPlayer clientplayerentity = Minecraft.getInstance().player;
//            float f3 = clientplayerentity == null ? 0.0F : clientplayerentity.getCooldowns().getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
//            if (f3 > 0.0F) {
//                RenderSystem.disableDepthTest();
//                RenderSystem.disableTexture();
//                RenderSystem.enableBlend();
//                RenderSystem.defaultBlendFunc();
//                Tessellator tessellator1 = Tessellator.getInstance();
//                BufferBuilder bufferbuilder1 = tessellator1.getBuilder();
//                draw(bufferbuilder1, xPosition, yPosition + Mth.floor(16.0F * (1.0F - f3)), 16, Mth.ceil(16.0F * f3), 255, 255, 255, 127);
//                RenderSystem.enableTexture();
//                RenderSystem.enableDepthTest();
//            }
//
//        }
//    }
//
//    protected static void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
//        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
//        renderer.vertex((double)(x + 0), (double)(y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
//        renderer.vertex((double)(x + 0), (double)(y + height), 0.0D).color(red, green, blue, alpha).endVertex();
//        renderer.vertex((double)(x + width), (double)(y + height), 0.0D).color(red, green, blue, alpha).endVertex();
//        renderer.vertex((double)(x + width), (double)(y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
//        Tessellator.getInstance().end();
//    }

}
