package de.keksuccino.spiffyhud.customization.items;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

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
    public void render(MatrixStack matrix) {
        if (this.shouldRender()) {
            ClientPlayerEntity p = MinecraftClient.getInstance().player;
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
        MatrixStack posestack = RenderSystem.getModelViewStack();
        posestack.push();
        posestack.translate(posX, posY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack posestack1 = new MatrixStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale(scale, scale, scale);
        Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
        Quaternion quaternion1;
        if (item.autoRotatePlayer) {
            quaternion1 = Vec3f.POSITIVE_X.getDegreesQuaternion(f1 * 20.0F);
        } else {
            quaternion1 = Vec3f.POSITIVE_X.getDegreesQuaternion(item.bodyRotationY);
        }
        quaternion.hamiltonProduct(quaternion1);
        posestack1.multiply(quaternion);
        float f2 = player.bodyYaw;
        float f3 = player.getYaw();
        float f4 = player.getPitch();
        float f5 = player.prevHeadYaw;
        float f6 = player.headYaw;
        if (item.autoRotatePlayer) {
            player.bodyYaw = 180.0F + f * 20.0F;
            player.setYaw(180.0F + f * 40.0F);
            player.setPitch(-f1 * 20.0F);
            player.headYaw = player.getYaw();
            player.prevHeadYaw = player.getYaw();
        } else {
            player.bodyYaw = item.bodyRotationX;
            player.setYaw(item.bodyRotationY);
            player.setPitch(item.headRotationY);
            player.headYaw = item.headRotationX;
            player.prevHeadYaw = item.headRotationX;
        }
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityrenderdispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion1.conjugate();
        entityrenderdispatcher.setRotation(quaternion1);
        entityrenderdispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate bufferSource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, bufferSource, 15728880);
        });
        bufferSource.draw();
        entityrenderdispatcher.setRenderShadows(true);
        player.bodyYaw = f2;
        player.setYaw(f3);
        player.setPitch(f4);
        player.prevHeadYaw = f5;
        player.headYaw = f6;
        posestack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }

}
