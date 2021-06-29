package de.keksuccino.fancyhud.customization.items;

import com.mojang.blaze3d.matrix.MatrixStack;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

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
//        System.out.println("CURRENT SCALE: " + scale);
        if (this.shouldRender()) {
            ClientPlayerEntity p = Minecraft.getInstance().player;
            if (p != null) {
                this.width = (int) (baseEntityWidth * this.scale);
                this.height = (int) (baseEntityHeight * this.scale);
                int entityX = this.getPosX() + (int)((this.baseEntityWidth * this.scale) / 2);
                int entityY = this.getPosY() + (int)(this.baseEntityHeight * this.scale);
                int rotationX = entityX - MouseInput.getMouseX();
                int rotationY = entityY - MouseInput.getMouseY();
//                if (!this.autoRotatePlayer) {
//                    rotationX = 0;
//                    rotationY = 0;
//                }
                renderPlayerEntity(entityX, entityY, this.scale, rotationX, rotationY, p, this);
            }
        }
    }

    protected static void renderPlayerEntity(int posX, int posY, float scale, float mouseX, float mouseY, ClientPlayerEntity player, MirroredPlayerCustomizationItem item) {
        float f = (float)Math.atan(mouseX / 40.0F);
        float f1 = (float)Math.atan(mouseY / 40.0F);

        RenderSystem.pushMatrix();

        RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);

        MatrixStack matrix = new MatrixStack();
        matrix.translate(0.0D, 0.0D, 1000.0D);
        matrix.scale(scale, scale, scale);

        Quaternion q = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion q2;
        if (item.autoRotatePlayer) {
            q2 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        } else {
            q2 = Vector3f.XP.rotationDegrees(item.bodyRotationY);
        }
        q.multiply(q2);
        matrix.rotate(q);

        float f2 = player.renderYawOffset;
        float f3 = player.rotationYaw;
        float f4 = player.rotationPitch;
        float f5 = player.prevRotationYawHead;
        float f6 = player.rotationYawHead;

        if (item.autoRotatePlayer) {
            player.renderYawOffset = 180.0F + f * 20.0F;
            player.rotationYaw = 180.0F + f * 40.0F;
            player.rotationPitch = -f1 * 20.0F;
            player.rotationYawHead = player.rotationYaw;
            player.prevRotationYawHead = player.rotationYaw;
        } else {
            player.renderYawOffset = item.bodyRotationX;
            player.rotationYaw = item.bodyRotationY;
            player.rotationPitch = item.headRotationY;
            player.rotationYawHead = item.headRotationX;
            player.prevRotationYawHead = item.headRotationX;
        }

        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        q.conjugate();
        entityrenderermanager.setCameraOrientation(q);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.renderEntityStatic(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrix, irendertypebuffer$impl, 15728880);
        });
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);

        player.renderYawOffset = f2;
        player.rotationYaw = f3;
        player.rotationPitch = f4;
        player.prevRotationYawHead = f5;
        player.rotationYawHead = f6;

        RenderSystem.popMatrix();
    }

}
