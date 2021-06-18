package de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class PotionIconsHudElement extends IngameHudElement {

	public PotionIconsHudElement(CustomizableIngameGui handler) {
		super(handler);

		//		this.width = 15;
		//		this.height = 15;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (this.fireEvents) {
			if (this.handler.pre(ElementType.POTION_ICONS, matrix)) return;
		}

		if (this.visible) {

			this.renderPotionIconsRaw(matrix);

		}

		if (this.fireEvents) {
			this.handler.post(ElementType.POTION_ICONS, matrix);
		}

	}

	protected void renderPotionIconsRaw(MatrixStack matrix) {

		Collection<EffectInstance> collection = this.mc.player.getActivePotionEffects();
		if (!collection.isEmpty()) {
			RenderSystem.enableBlend();
			int i = 0;
			int j = 0;
			PotionSpriteUploader potionspriteuploader = this.mc.getPotionSpriteUploader();
			List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());
			this.mc.getTextureManager().bindTexture(ContainerScreen.INVENTORY_BACKGROUND);

			int elementHeight = 25;
			int bottomOffset = 0;
			
			//handle top row
			for(EffectInstance effectinstance : Ordering.natural().reverse().sortedCopy(collection)) {
				Effect effect = effectinstance.getPotion();
				
				if (!effect.isBeneficial()) {
					continue;
				}
				
				if (!effectinstance.shouldRenderHUD()) continue;
				this.mc.getTextureManager().bindTexture(ContainerScreen.INVENTORY_BACKGROUND);
				if (effectinstance.isShowIcon()) {
					int k = this.x - 25;
					int l = this.y;
					if (this.mc.isDemo()) {
						l += 15;
					}
					
					++i;
					this.width = (25 * i) - 1;
					bottomOffset = (25 * i) - 1 - 25;
					k = k + 25 * i;

					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					float f = 1.0F;
					if (effectinstance.isAmbient()) {
						this.blit(matrix, k, l, 165, 166, 24, 24);
					} else {
						this.blit(matrix, k, l, 141, 166, 24, 24);
						if (effectinstance.getDuration() <= 200) {
							int i1 = 10 - effectinstance.getDuration() / 20;
							f = MathHelper.clamp((float)effectinstance.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float)effectinstance.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp((float)i1 / 10.0F * 0.25F, 0.0F, 0.25F);
						}
					}

					TextureAtlasSprite textureatlassprite = potionspriteuploader.getSprite(effect);
					int j1 = k;
					int k1 = l;
					float f1 = f;
					list.add(() -> {
						this.mc.getTextureManager().bindTexture(textureatlassprite.getAtlasTexture().getTextureLocation());
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
						blit(matrix, j1 + 3, k1 + 3, this.getBlitOffset(), 18, 18, textureatlassprite);
					});
					effectinstance.renderHUDEffect(this, matrix, k, l, this.getBlitOffset(), f);
				}
			}
			
			//handle bottom row
			for(EffectInstance effectinstance : Ordering.natural().reverse().sortedCopy(collection)) {
				Effect effect = effectinstance.getPotion();
				
				if (effect.isBeneficial()) {
					continue;
				}
				
				if (!effectinstance.shouldRenderHUD()) continue;
				this.mc.getTextureManager().bindTexture(ContainerScreen.INVENTORY_BACKGROUND);
				if (effectinstance.isShowIcon()) {
					int k = this.x - 25 + bottomOffset;
					int l = this.y;
					if (this.mc.isDemo()) {
						l += 15;
					}

					++j;

					int tempWidth = 25 * j - 1;
					if ((bottomOffset + 25) < tempWidth) {
						this.width = tempWidth;
					}
					
					if ((bottomOffset == 0)) {
						k = k + 25 * j;
					} else {
						k = k - 25 * j;
						k += 50 + 1;
					}
					l += 26;
					elementHeight = 25 + 26;

					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					float f = 1.0F;
					if (effectinstance.isAmbient()) {
						this.blit(matrix, k, l, 165, 166, 24, 24);
					} else {
						this.blit(matrix, k, l, 141, 166, 24, 24);
						if (effectinstance.getDuration() <= 200) {
							int i1 = 10 - effectinstance.getDuration() / 20;
							f = MathHelper.clamp((float)effectinstance.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float)effectinstance.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp((float)i1 / 10.0F * 0.25F, 0.0F, 0.25F);
						}
					}

					TextureAtlasSprite textureatlassprite = potionspriteuploader.getSprite(effect);
					int j1 = k;
					int k1 = l;
					float f1 = f;
					list.add(() -> {
						this.mc.getTextureManager().bindTexture(textureatlassprite.getAtlasTexture().getTextureLocation());
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
						blit(matrix, j1 + 3, k1 + 3, this.getBlitOffset(), 18, 18, textureatlassprite);
					});
					effectinstance.renderHUDEffect(this, matrix, k, l, this.getBlitOffset(), f);
				}
			}
			
			this.height = elementHeight - 1;

			list.forEach(Runnable::run);
		}

	}

}
