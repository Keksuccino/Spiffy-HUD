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
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class MirroredPlayerCustomizationItem extends CustomizationItemBase {

    public float scale = 30.0F;
    public boolean rotatePlayer = true;

    protected double baseEntityWidth = 0.6;
    protected double baseEntityHeight = 2;

    public MirroredPlayerCustomizationItem(PropertiesSection item) {
        super(item);

        this.value = "Mirrored Player";

        if ((this.action != null) && this.action.equalsIgnoreCase("addmirroredplayer")) {

            String scaleString = item.getEntryValue("scale");
            if ((scaleString != null) && MathUtils.isInteger(scaleString)) {
                this.scale = Integer.parseInt(scaleString);
            }

            String rotatePlayerString = item.getEntryValue("rotateplayer");
            if ((rotatePlayerString != null) && rotatePlayerString.equalsIgnoreCase("false")) {
                this.rotatePlayer = false;
            }

        }
    }

    @Override
    public void render(MatrixStack matrix) {
        if (this.shouldRender()) {
            ClientPlayerEntity p = Minecraft.getInstance().player;
            if (p != null) {
                this.width = (int) (baseEntityWidth * this.scale);
                this.height = (int) (baseEntityHeight * this.scale);
                int entityX = this.getPosX() + (int)((this.baseEntityWidth * this.scale) / 2);
                int entityY = this.getPosY() + (int)(this.baseEntityHeight * this.scale);
                int rotationX = entityX - MouseInput.getMouseX();
                int rotationY = entityY - MouseInput.getMouseY();
                if (!this.rotatePlayer) {
                    rotationX = 0;
                    rotationY = 0;
                }
                renderPlayerEntity(entityX, entityY, this.scale, rotationX, rotationY, p);
            }
        }
    }

    protected static void renderPlayerEntity(int posX, int posY, float scale, float mouseX, float mouseY, ClientPlayerEntity player) {
        float f = (float)Math.atan(mouseX / 40.0F);
        float f1 = (float)Math.atan(mouseY / 40.0F);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        float f2 = player.renderYawOffset;
        float f3 = player.rotationYaw;
        float f4 = player.rotationPitch;
        float f5 = player.prevRotationYawHead;
        float f6 = player.rotationYawHead;
        player.renderYawOffset = 180.0F + f * 20.0F;
        player.rotationYaw = 180.0F + f * 40.0F;
        player.rotationPitch = -f1 * 20.0F;
        player.rotationYawHead = player.rotationYaw;
        player.prevRotationYawHead = player.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.renderEntityStatic(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
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
