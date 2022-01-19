package de.keksuccino.spiffyhud.api;

import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.AirBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.ArmorBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.BossBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.CrosshairHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.ExperienceJumpBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.FoodMountHealthHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.HotbarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.OverlayMessageHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.PlayerHealthHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.SelectedItemNameHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.TitleHudElement;
import net.minecraft.client.Minecraft;

@Deprecated
public class IngameHud {
	
	private static IngameHud instance;

	@Deprecated
	public CustomizableIngameGui getGui() {
		if (Minecraft.getInstance().screen instanceof LayoutEditorScreen) {
			return ((LayoutEditorScreen)Minecraft.getInstance().screen).ingameHud;
		}
		return CustomizationHandler.INGAME_GUI;
	}

	@Deprecated
	public boolean isLightModeEnabled() {
		return CustomizationHandler.isLightModeEnabled();
	}

	@Deprecated
	public AirBarHudElement getAirBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.airBarElement;
		}
		return null;
	}

	@Deprecated
	public ArmorBarHudElement getArmorBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.armorBarElement;
		}
		return null;
	}

	@Deprecated
	public BossBarHudElement getBossBarElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.bossBarElement;
		}
		return null;
	}

	@Deprecated
	public CrosshairHudElement getCrosshair() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.crosshairElement;
		}
		return null;
	}

	@Deprecated
	public ExperienceJumpBarHudElement getExperienceJumpBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.experienceJumpBarElement;
		}
		return null;
	}

	@Deprecated
	public FoodMountHealthHudElement getFoodMountHealthBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.foodMountHealthElement;
		}
		return null;
	}

	@Deprecated
	public HotbarHudElement getHotbar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.hotbarElement;
		}
		return null;
	}

	@Deprecated
	public OverlayMessageHudElement getOverlayMessageElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.overlayMessageElement;
		}
		return null;
	}

	@Deprecated
	public PlayerHealthHudElement getPlayerHealthBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.healthElement;
		}
		return null;
	}

	@Deprecated
	public SelectedItemNameHudElement getSelectedItemNameElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.selectedItemNameElement;
		}
		return null;
	}

	@Deprecated
	public TitleHudElement getTitleElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.titleElement;
		}
		return null;
	}

	@Deprecated
	public TitleHudElement getSubTitleElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.subtitleElement;
		}
		return null;
	}

	@Deprecated
	public static IngameHud getInstance() {
		if (instance == null) {
			instance = new IngameHud();
		}
		return instance;
	}

}
