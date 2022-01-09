package de.keksuccino.spiffyhud.customization.items;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;

public class MirroredPlayerCustomizationItem extends CustomizationItemBase {

    public float scale = 30.0F;
    public boolean autoRotatePlayer = true;
    public float bodyRotationX = 180;
    public float bodyRotationY = 0;
    public float headRotationX = 180;
    public float headRotationY = 0;

    protected double baseEntityWidth = 0.6;
    protected double baseEntityHeight = 2;

    public MirroredPlayerCustomizationItem(PropertiesSection item) {
        super(item);

        this.value = "Mirrored Player";

        if ((this.action != null) && this.action.equalsIgnoreCase("addmirroredplayer")) {

            String scaleString = item.getEntryValue("scale");
            if ((scaleString != null) && MathUtils.isFloat(scaleString)) {
                this.scale = Float.parseFloat(scaleString);
            }

            String rotX = item.getEntryValue("headrotationx");
            if (rotX != null) {
                rotX = rotX.replace(" ", "");
                if (MathUtils.isFloat(rotX)) {
                    this.headRotationX = Float.parseFloat(rotX);
                }
            }

            String rotY = item.getEntryValue("headrotationy");
            if (rotY != null) {
                rotY = rotY.replace(" ", "");
                if (MathUtils.isFloat(rotY)) {
                    this.headRotationY = Float.parseFloat(rotY);
                }
            }

            String bodyrotX = item.getEntryValue("bodyrotationx");
            if (bodyrotX != null) {
                bodyrotX = bodyrotX.replace(" ", "");
                if (MathUtils.isFloat(bodyrotX)) {
                    this.bodyRotationX = Float.parseFloat(bodyrotX);
                }
            }

            String bodyrotY = item.getEntryValue("bodyrotationy");
            if (bodyrotY != null) {
                bodyrotY = bodyrotY.replace(" ", "");
                if (MathUtils.isFloat(bodyrotY)) {
                    this.bodyRotationY = Float.parseFloat(bodyrotY);
                }
            }

            String autoRot = item.getEntryValue("rotateplayer");
            if (autoRot != null) {
                if (autoRot.replace(" ", "").equalsIgnoreCase("false")) {
                    this.autoRotatePlayer = false;
                }
            }

        }
    }

    @Override
    public void render(PoseStack matrix) {
        if (this.shouldRender()) {
            LocalPlayer p = Minecraft.getInstance().player;
            if (p != null) {
                this.width = (int) (baseEntityWidth * this.scale);
                this.height = (int) (baseEntityHeight * this.scale);
                int entityX = this.getPosX() + (int)((this.baseEntityWidth * this.scale) / 2);
                int entityY = this.getPosY() + (int)(this.baseEntityHeight * this.scale);
                int rotationX = entityX - MouseInput.getMouseX();
                int rotationY = entityY - MouseInput.getMouseY();
                renderPlayerEntity(entityX, entityY, this.scale, rotationX, rotationY, p, this);
            }
        }
    }

    public void renderPlayerEntity(int posX, int posY, float scale, float mouseX, float mouseY, LivingEntity player,  MirroredPlayerCustomizationItem item) {
        float f = (float)Math.atan((mouseX / 40.0F));
        float f1 = (float)Math.atan((mouseY / 40.0F));
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(posX, posY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1;
        if (item.autoRotatePlayer) {
            quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        } else {
            quaternion1 = Vector3f.XP.rotationDegrees(item.bodyRotationY);
        }
        quaternion.mul(quaternion1);
        posestack1.mulPose(quaternion);
        float f2 = player.yBodyRot;
        float f3 = player.getYRot();
        float f4 = player.getXRot();
        float f5 = player.yHeadRotO;
        float f6 = player.yHeadRot;
        if (item.autoRotatePlayer) {
            player.yBodyRot = 180.0F + f * 20.0F;
            player.setYRot(180.0F + f * 40.0F);
            player.setXRot(-f1 * 20.0F);
            player.yHeadRot = player.getYRot();
            player.yHeadRotO = player.getYRot();
        } else {
            player.yBodyRot = item.bodyRotationX;
            player.setYRot(item.bodyRotationY);
            player.setXRot(item.headRotationY);
            player.yHeadRot = item.headRotationX;
            player.yHeadRotO = item.headRotationX;
        }
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        player.yBodyRot = f2;
        player.setYRot(f3);
        player.setXRot(f4);
        player.yHeadRotO = f5;
        player.yHeadRot = f6;
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

}
