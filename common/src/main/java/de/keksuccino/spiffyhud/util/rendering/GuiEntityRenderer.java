package de.keksuccino.spiffyhud.util.rendering;

import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class GuiEntityRenderer {

    private static final float DEFAULT_FACING = 180.0f; // Default inventory facing
    private static final float MAX_BODY_DELTA = 30.0f;    // Body rotation cap relative to default
    private static final float MAX_HEAD_DELTA = 30.0f;    // Head rotation cap relative to rendered body

    // Instance fields to track rendered rotations.
    // For body rotation:
    private float lastOriginalBodyRotation = Float.NaN;
    private float renderedBodyRotation = DEFAULT_FACING;
    // For head rotation:
    private float lastOriginalHeadRotation = Float.NaN;
    private float renderedHeadRotation = DEFAULT_FACING;

    /**
     * Renders a living entity into a GUI box.
     * <p>
     * The entity is scaled using its bounding dimensions from Pose.STANDING so that its bounding box fits
     * entirely within the target box defined by (posX, posY, boxWidth, boxHeight) and is centered.
     * <p>
     * The rendered body rotation is updated by applying only the incremental difference (delta)
     * from the previous original body rotation value, then clamped to lie within ±30° of DEFAULT_FACING.
     * The head rotation is similarly updated based on the incremental change, but then clamped to within ±30°
     * relative to the rendered body rotation. This prevents either from drifting too far, even if the original
     * rotations continue to increase.
     * After rendering, the entity's original rotations are restored.
     *
     * @param graphics  the GUI graphics context used for rendering
     * @param posX      the x-coordinate of the top-left corner of the target box
     * @param posY      the y-coordinate of the top-left corner of the target box
     * @param boxWidth  the width of the target box (in pixels)
     * @param boxHeight the height of the target box (in pixels)
     * @param opacity   the opacity of the rendered entity (0.0 = fully transparent, 1.0 = fully opaque)
     * @param entity    the LivingEntity to render
     */
    public void renderEntity(GuiGraphics graphics, int posX, int posY, int boxWidth, int boxHeight, float opacity, LivingEntity entity) {

        // Obtain the entity's bounding dimensions using Pose.STANDING.
        EntityDimensions dimensions = entity.getDimensions(Pose.STANDING);
        // Compute scale factors so that the entity's bounding box fits entirely within the target box.
        float scaleFromWidth = (float) boxWidth / dimensions.width();
        float scaleFromHeight = (float) boxHeight / dimensions.height();
        float uniformScale = Math.min(scaleFromWidth, scaleFromHeight);

        // Save original rotation values for later restoration.
        float origYBodyRot = entity.yBodyRot;
        float origYRot = entity.getYRot();
        float origXRot = entity.getXRot();
        float origYHeadRot = entity.yHeadRot;
        float origYHeadRotO = entity.yHeadRotO;

        // ----- Update Body Rotation Using Delta and Clamp -----
        float currentOriginalBody = origYBodyRot;
        if (Float.isNaN(lastOriginalBodyRotation)) {
            lastOriginalBodyRotation = currentOriginalBody;
            renderedBodyRotation = currentOriginalBody;
        }
        // Compute the incremental difference (wrapped properly).
        float bodyDelta = Mth.wrapDegrees(currentOriginalBody - lastOriginalBodyRotation);
        renderedBodyRotation += bodyDelta;
        // Clamp the rendered body rotation to within DEFAULT_FACING ± MAX_BODY_DELTA.
        renderedBodyRotation = Mth.clamp(renderedBodyRotation, DEFAULT_FACING - MAX_BODY_DELTA, DEFAULT_FACING + MAX_BODY_DELTA);
        lastOriginalBodyRotation = currentOriginalBody;
        // Override the entity's body rotation for rendering.
        entity.yBodyRot = renderedBodyRotation;

        // ----- Update Head Rotation Using Delta and Clamp Relative to Body -----
        float currentOriginalHead = origYHeadRot;
        if (Float.isNaN(lastOriginalHeadRotation)) {
            lastOriginalHeadRotation = currentOriginalHead;
            renderedHeadRotation = currentOriginalHead;
        }
        float headDelta = Mth.wrapDegrees(currentOriginalHead - lastOriginalHeadRotation);
        float newRenderedHead = renderedHeadRotation + headDelta;
        // Clamp the head rotation so it stays within ±MAX_HEAD_DELTA of the rendered body rotation.
        newRenderedHead = Mth.clamp(newRenderedHead, renderedBodyRotation - MAX_HEAD_DELTA, renderedBodyRotation + MAX_HEAD_DELTA);
        lastOriginalHeadRotation = currentOriginalHead;
        renderedHeadRotation = newRenderedHead;
        // Override the entity's head rotation.
        entity.yHeadRot = newRenderedHead;
        entity.yHeadRotO = newRenderedHead;
        entity.setYRot(newRenderedHead);

        // ----- Transformation and Rendering -----
        graphics.pose().pushPose();
        // Translate to the center of the target box.
        graphics.pose().translate(posX + boxWidth / 2.0, posY + boxHeight / 2.0, 50.0);
        // Apply uniform scaling (note: negative on Z to mirror correctly).
        graphics.pose().mulPose(new Matrix4f().scaling(uniformScale, uniformScale, -uniformScale));
        // Apply a base rotation: rotate 180° about the Z-axis so the entity faces the viewer.
        Quaternionf baseRotation = new Quaternionf().rotateZ((float) Math.PI);
        graphics.pose().mulPose(baseRotation);
        // Shift upward so that the entity's bounding box is vertically centered.
        // Since the pivot is at the feet, translate upward by half of the entity's height.
        graphics.pose().translate(0.0, -(dimensions.height() / 2.0f), 0.0);

        // Setup lighting for GUI entity rendering.
        Lighting.setupForEntityInInventory();
        // Retrieve the entity render dispatcher and disable shadow rendering.
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadow(false);
        graphics.drawSpecial(multiBufferSource -> {
            ItemRenderingUtils.setItemOpacity(opacity);
            BlockRenderingUtils.setBlockOpacity(opacity);
            EntityRenderingUtils.setLivingEntityOpacity(opacity);
            dispatcher.render(entity, 0.0, 0.0, 0.0, 1.0f, graphics.pose(), multiBufferSource, 15728880);
            BlockRenderingUtils.resetBlockOpacity();
            EntityRenderingUtils.resetLivingEntityOpacity();
            ItemRenderingUtils.resetItemOpacity();
        });
        graphics.flush();
        dispatcher.setRenderShadow(true);
        graphics.pose().popPose();
        Lighting.setupFor3DItems();

        // Restore the entity's original rotation values.
        entity.yBodyRot = origYBodyRot;
        entity.setYRot(origYRot);
        entity.setXRot(origXRot);
        entity.yHeadRot = origYHeadRot;
        entity.yHeadRotO = origYHeadRotO;

    }

}
