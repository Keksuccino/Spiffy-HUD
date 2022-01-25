package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.SpectatorGui;
import net.minecraft.client.settings.AttackIndicatorStatus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class HotbarHudElement extends IngameHudElement {
	
	protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
	
	protected SpectatorGui spectatorGui = new SpectatorGui(mc);

	public HotbarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 182; //offhand nicht mit dazu gerechnet (+29)
		this.height = 22;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("hotbar");
		this.elementActive = InGameHudOverlay.isElementActive("hotbar");

		if (this.fireEvents) {
			if (this.handler.pre(ElementType.HOTBAR, matrix)) return;
		}

		if (this.renderElement) {
			if (this.visible) {

				if (mc.playerController.getCurrentGameType() == GameType.SPECTATOR) {
					this.spectatorGui.func_238528_a_(matrix, partialTicks);
				} else {
					this.renderHotbarRaw(matrix, scaledWidth, scaledHeight, partialTicks);
				}

			}
		}

		if (this.fireEvents) {
			this.handler.post(ElementType.HOTBAR, matrix);
		}

	}

	protected void renderHotbarRaw(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		PlayerEntity playerentity = this.getRenderViewPlayer();
		if (playerentity != null) {
			
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
			
			ItemStack itemstack = playerentity.getHeldItemOffhand();
			HandSide handside = playerentity.getPrimaryHand().opposite();
			
			this.blit(matrix, x, y, 0, 0, 182, 22);
			int currentItem = 4;
			if (!this.handler.isEditor()) {
				currentItem = playerentity.inventory.currentItem;
			}
			this.blit(matrix, x - 1 + currentItem * 20, y - 1, 0, 22, 24, 22);
			
			if (!itemstack.isEmpty() || this.handler.isEditor()) {
				if (handside == HandSide.LEFT) {
					this.blit(matrix, x - 29, y - 1, 24, 22, 29, 24);
				} else {
					this.blit(matrix, x + 182, y - 1, 53, 22, 29, 24);
				}
			}

			RenderSystem.enableRescaleNormal();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();

			if (!this.handler.isEditor()) {
				
				for(int i1 = 0; i1 < 9; ++i1) {
					int j1 = x + 1 + i1 * 20 + 2;
					int k1 = y + 3;
					this.renderHotbarItem(j1, k1, partialTicks, playerentity, playerentity.inventory.mainInventory.get(i1));
				}
				
				if (!itemstack.isEmpty()) {
					int i2 = y + 3;
					if (handside == HandSide.LEFT) {
						this.renderHotbarItem(x - 26, i2, partialTicks, playerentity, itemstack);
					} else {
						this.renderHotbarItem(x + 182 + 10, i2, partialTicks, playerentity, itemstack);
					}
				}
				
			}

			if (this.mc.gameSettings.attackIndicator == AttackIndicatorStatus.HOTBAR) {
				float f = this.mc.player.getCooledAttackStrength(0.0F);
				if (f < 1.0F) {
					int j2 = y + 2;
					int k2 = x + 182 + 6;
					if (handside == HandSide.RIGHT) {
						k2 = x - 22;
					}

					this.mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
					int l1 = (int)(f * 19.0F);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					this.blit(matrix, k2, j2, 0, 94, 18, 18);
					this.blit(matrix, k2, j2 + 18 - l1, 18, 112 - l1, 18, l1);
				}
			}

			RenderSystem.disableRescaleNormal();
			RenderSystem.disableBlend();
		}

	}

	protected void renderHotbarItem(int x, int y, float partialTicks, PlayerEntity player, ItemStack stack) {

		if (!stack.isEmpty()) {
			float f = (float)stack.getAnimationsToGo() - partialTicks;
			if (f > 0.0F) {
				RenderSystem.pushMatrix();
				float f1 = 1.0F + f / 5.0F;
				RenderSystem.translatef((float)(x + 8), (float)(y + 12), 0.0F);
				RenderSystem.scalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				RenderSystem.translatef((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
			}

			this.mc.getItemRenderer().renderItemAndEffectIntoGUI(player, stack, x, y);
			if (f > 0.0F) {
				RenderSystem.popMatrix();
			}

			this.mc.getItemRenderer().renderItemOverlays(this.mc.fontRenderer, stack, x, y);
		}

	}
	
	protected PlayerEntity getRenderViewPlayer() {
		return !(this.mc.getRenderViewEntity() instanceof PlayerEntity) ? null : (PlayerEntity)this.mc.getRenderViewEntity();
	}

}
