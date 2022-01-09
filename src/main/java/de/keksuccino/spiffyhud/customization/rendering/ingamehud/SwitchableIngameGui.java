package de.keksuccino.spiffyhud.customization.rendering.ingamehud;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.gui.SpectatorGui;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.client.gui.overlay.PlayerTabOverlayGui;

public class SwitchableIngameGui extends CustomizableIngameGui {

	public final CustomizableIngameGui customizableHud;
	public final IngameGui vanillaGui;
	
	public boolean overrideGui = true;
	
	//TODO eventuell nicht mit switchable arbeiten, sondern einfach einen neustart verlangen, wenn auf Light mode geswitcht wird (oder weg davon)
	//- für lightmode einfach zweite instanz von CustomizableIngameGui erstellen, um davon immer
	//  standard positionen zu bekommen (in der instanz verhindern, dass layouts geladen werden, evtl durch isEditor = true)
	
	public SwitchableIngameGui(Minecraft mc, boolean isEditor) {
		super(mc, isEditor);
		this.customizableHud = new CustomizableIngameGui(mc, isEditor);
		this.vanillaGui = new IngameGui(mc);
	}
	
	@Override
	public void renderIngameGui(MatrixStack matrix, float partialTicks) {
		if (this.overrideGui) {
			this.customizableHud.renderIngameGui(matrix, partialTicks);
		} else {
			this.vanillaGui.renderIngameGui(matrix, partialTicks);
		}
	}
	
	@Override
	public BossOverlayGui getBossOverlay() {
		if (this.overrideGui) {
			return this.customizableHud.getBossOverlay();
		} else {
			return this.vanillaGui.getBossOverlay();
		}
	}
	
	@Override
	public PlayerTabOverlayGui getTabList() {
		if (this.overrideGui) {
			return this.customizableHud.getTabList();
		} else {
			return this.vanillaGui.getTabList();
		}
	}
	
	@Override
	public SpectatorGui getSpectatorGui() {
		if (this.overrideGui) {
			return this.customizableHud.getSpectatorGui();
		} else {
			return this.vanillaGui.getSpectatorGui();
		}
	}
	
	@Override
	public NewChatGui getChatGUI() {
		if (this.overrideGui) {
			return this.customizableHud.getChatGUI();
		} else {
			return this.vanillaGui.getChatGUI();
		}
	}
	
	@Override
	public int getTicks() {
		if (this.overrideGui) {
			return this.customizableHud.getTicks();
		} else {
			return this.vanillaGui.getTicks();
		}
	}

}
