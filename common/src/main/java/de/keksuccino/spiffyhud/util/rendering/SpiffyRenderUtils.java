package de.keksuccino.spiffyhud.util.rendering;

import com.mojang.blaze3d.vertex.*;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinGuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import java.util.function.Function;

public class SpiffyRenderUtils {

    /**
     * Draws a textured quad with the U texture coordinates swapped so that the image appears mirrored horizontally.
     *
     * @param graphics             The graphics context.
     * @param renderTypeGetter     Function to get the render type for the texture.
     * @param atlasLocation        The texture atlas.
     * @param x                    The screen X coordinate.
     * @param y                    The screen Y coordinate.
     * @param blitOffset           The z-level (blit offset).
     * @param u                    The source U coordinate (left edge) of the texture.
     * @param v                    The source V coordinate (top edge) of the texture.
     * @param width                The width of the quad.
     * @param height               The height of the quad.
     * @param textureWidth         The width of the texture.
     * @param textureHeight        The height of the texture.
     * @param color                The color to apply to the texture.
     */
    public static void blitMirrored(
            GuiGraphics graphics,
            Function<ResourceLocation, RenderType> renderTypeGetter,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int blitOffset,
            int u,
            int v,
            int width,
            int height,
            int textureWidth,
            int textureHeight,
            int color) {
        
        // Calculate texture coordinates.
        float minU = (u + width) / (float) textureWidth;
        float maxU = u / (float) textureWidth;
        float minV = v / (float) textureHeight;
        float maxV = (v + height) / (float) textureHeight;

        Matrix4f matrix = graphics.pose().last().pose();
        VertexConsumer vertexConsumer = ((IMixinGuiGraphics)graphics).getBufferSource_FancyMenu().getBuffer(renderTypeGetter.apply(atlasLocation));
        
        int red = ARGB.red(color);
        int green = ARGB.green(color);
        int blue = ARGB.blue(color);
        int alpha = ARGB.alpha(color);

        vertexConsumer.addVertex(matrix, (float)x, (float)y, (float)blitOffset).setUv(minU, minV).setColor(red, green, blue, alpha);
        vertexConsumer.addVertex(matrix, (float)x, (float)(y + height), (float)blitOffset).setUv(minU, maxV).setColor(red, green, blue, alpha);
        vertexConsumer.addVertex(matrix, (float)(x + width), (float)(y + height), (float)blitOffset).setUv(maxU, maxV).setColor(red, green, blue, alpha);
        vertexConsumer.addVertex(matrix, (float)(x + width), (float)y, (float)blitOffset).setUv(maxU, minV).setColor(red, green, blue, alpha);

        graphics.flush();
    }

    /**
     * Draws a textured quad with the U texture coordinates swapped so that the image appears mirrored horizontally.
     * Uses default RenderType.text and white color.
     */
    public static void blitMirrored(
            GuiGraphics graphics,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int blitOffset,
            int u,
            int v,
            int width,
            int height,
            int textureWidth,
            int textureHeight) {

        blitMirrored(
            graphics,
            RenderType::text,
            atlasLocation,
            x,
            y,
            blitOffset,
            u,
            v,
            width,
            height,
            textureWidth,
            textureHeight,
            -1  // White (fully opaque)
        );
    }

    /**
     * Draws a sprite horizontally mirrored (flipped along the vertical axis).
     * This method is specifically designed for sprite resources and handles sprite texture coordinates.
     *
     * @param graphics         The graphics context.
     * @param renderTypeGetter Function to get the render type for the texture.
     * @param sprite           The sprite resource location.
     * @param x                The screen X coordinate.
     * @param y                The screen Y coordinate.
     * @param width            The width of the sprite.
     * @param height           The height of the sprite.
     * @param color            The color to apply to the sprite.
     */
    public static void blitSpriteMirrored(
            GuiGraphics graphics,
            Function<ResourceLocation, RenderType> renderTypeGetter,
            ResourceLocation sprite,
            int x,
            int y,
            int width,
            int height,
            int color) {

        TextureAtlasSprite atlasSprite = Minecraft.getInstance().getGuiSprites().getSprite(sprite);

        // For horizontal mirroring, swap U coordinates
        float minU = atlasSprite.getU1();
        float maxU = atlasSprite.getU0();
        float minV = atlasSprite.getV0();
        float maxV = atlasSprite.getV1();

        Matrix4f matrix = graphics.pose().last().pose();
        VertexConsumer vertexConsumer = ((IMixinGuiGraphics)graphics).getBufferSource_FancyMenu().getBuffer(renderTypeGetter.apply(atlasSprite.atlasLocation()));

        int red = ARGB.red(color);
        int green = ARGB.green(color);
        int blue = ARGB.blue(color);
        int alpha = ARGB.alpha(color);

        vertexConsumer.addVertex(matrix, (float)x, (float)y, 0.0f).setUv(minU, minV).setColor(red, green, blue, alpha);
        vertexConsumer.addVertex(matrix, (float)x, (float)(y + height), 0.0f).setUv(minU, maxV).setColor(red, green, blue, alpha);
        vertexConsumer.addVertex(matrix, (float)(x + width), (float)(y + height), 0.0f).setUv(maxU, maxV).setColor(red, green, blue, alpha);
        vertexConsumer.addVertex(matrix, (float)(x + width), (float)y, 0.0f).setUv(maxU, minV).setColor(red, green, blue, alpha);

        graphics.flush();
    }

    /**
     * Draws a sprite horizontally mirrored (flipped along the vertical axis).
     * Uses default RenderType.guiTextured and white color.
     */
    public static void blitSpriteMirrored(
            GuiGraphics graphics,
            ResourceLocation sprite,
            int x,
            int y,
            int width,
            int height) {

        blitSpriteMirrored(
            graphics,
            RenderType::guiTextured,
            sprite,
            x,
            y,
            width,
            height,
            -1  // White (fully opaque)
        );
    }

    public static void blitSprite(
            GuiGraphics graphics,
            Function<ResourceLocation, RenderType> renderTypeGetter,
            ResourceLocation sprite,
            int textureWidth,
            int textureHeight,
            int uPosition,
            int vPosition,
            int x,
            int y,
            int uWidth,
            int vHeight,
            int color
    ) {
        TextureAtlasSprite textureAtlasSprite = ((de.keksuccino.spiffyhud.mixin.mixins.common.client.IMixinGuiGraphics)graphics).get_sprites_Spiffy().getSprite(sprite);
        GuiSpriteScaling guiSpriteScaling = ((de.keksuccino.spiffyhud.mixin.mixins.common.client.IMixinGuiGraphics)graphics).get_sprites_Spiffy().getSpriteScaling(textureAtlasSprite);
        if (guiSpriteScaling instanceof GuiSpriteScaling.Stretch) {
            ((de.keksuccino.spiffyhud.mixin.mixins.common.client.IMixinGuiGraphics)graphics).invoke_private_blitSprite_Spiffy(renderTypeGetter, textureAtlasSprite, textureWidth, textureHeight, uPosition, vPosition, x, y, uWidth, vHeight, color);
        } else {
            graphics.enableScissor(x, y, x + uWidth, y + vHeight);
            graphics.blitSprite(renderTypeGetter, sprite, x - uPosition, y - vPosition, textureWidth, textureHeight, color);
            graphics.disableScissor();
        }
    }

    /**
     * Returns the given color with the given alpha applied.
     *
     * @param color The original color value (ARGB format)
     * @param alpha The alpha value to apply (0.0f to 1.0f)
     *
     * @return The color with the new alpha value applied
     */
    public static int colorWithAlpha(int color, float alpha) {
        int alphaComponent = Math.round(alpha * 255.0f);
        // Extract color components
        int red = ARGB.red(color);
        int green = ARGB.green(color);
        int blue = ARGB.blue(color);
        // Create new color with the specified alpha
        return ARGB.color(alphaComponent, red, green, blue);
    }

}
