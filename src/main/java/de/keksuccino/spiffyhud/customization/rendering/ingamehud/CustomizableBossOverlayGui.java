package de.keksuccino.spiffyhud.customization.rendering.ingamehud;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ClientBossInfo;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.network.play.server.SUpdateBossInfoPacket;
import net.minecraft.network.play.server.SUpdateBossInfoPacket.Operation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class CustomizableBossOverlayGui extends BossOverlayGui {

	protected static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
	
	protected CustomizableIngameGui handler;
	
	public CustomizableBossOverlayGui(Minecraft clientIn, CustomizableIngameGui handler) {
		super(clientIn);
		this.handler = handler;
		
		if (this.handler.isEditor()) {
			this.setupEditorMode();
		}
	}
	
	//render
	@Override
	public void func_238484_a_(MatrixStack matrix) {

		Minecraft mc = Minecraft.getInstance();
		Map<UUID, ClientBossInfo> m = this.getBossInfoMap();

		if (!m.isEmpty()) {
			int posX = this.handler.bossBarElement.x;
			int posY = this.handler.bossBarElement.y;

			for(ClientBossInfo info : m.values()) {
				RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(matrix, mc.getMainWindow(), info, posX, posY, 10 + mc.fontRenderer.FONT_HEIGHT);
				if (!event.isCanceled()) {
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
					this.renderProgress(matrix, posX, posY, info);
					ITextComponent itextcomponent = info.getName();
					int l = mc.fontRenderer.getStringPropertyWidth(itextcomponent);
					int i1 = (this.handler.bossBarElement.x + 91) - (l / 2);
					int j1 = posY - 9;
					mc.fontRenderer.drawTextWithShadow(matrix, itextcomponent, (float)i1, (float)j1, 16777215);
				}
				posY += event.getIncrement();
				ForgeHooksClient.bossBarRenderPost(matrix, mc.getMainWindow());
				if (posY >= mc.getMainWindow().getScaledHeight() / 3) {
					break;
				}
			}

		}

	}
	
	protected void renderProgress(MatrixStack matrix, int posX, int posY, BossInfo info) {
		this.blit(matrix, posX, posY, 0, info.getColor().ordinal() * 5 * 2, 182, 5);
		if (info.getOverlay() != BossInfo.Overlay.PROGRESS) {
			this.blit(matrix, posX, posY, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
		}

		int i = (int)(info.getPercent() * 183.0F);
		if (i > 0) {
			this.blit(matrix, posX, posY, 0, info.getColor().ordinal() * 5 * 2 + 5, i, 5);
			if (info.getOverlay() != BossInfo.Overlay.PROGRESS) {
				this.blit(matrix, posX, posY, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
			}
		}

	}
	
	public Map<UUID, ClientBossInfo> getBossInfoMap() {
		try {
			Field f = ObfuscationReflectionHelper.findField(BossOverlayGui.class, "field_184060_g");
			return (Map<UUID, ClientBossInfo>) f.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void setupEditorMode() {
		BossInfo info = new BossInfo(UUID.randomUUID(), new StringTextComponent("Boss Name"), Color.PURPLE, Overlay.PROGRESS){};
		this.read(new SUpdateBossInfoPacket(Operation.ADD, info));
	}

}
