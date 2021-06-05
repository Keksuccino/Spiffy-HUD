package de.keksuccino.fancyhud.api;

import de.keksuccino.fancyhud.customization.CustomizationHandler;
import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.AirBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.ArmorBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.BossBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.CrosshairHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.ExperienceJumpBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.FoodMountHealthHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.HotbarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.OverlayMessageHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.PlayerHealthHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.SelectedItemNameHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.TitleHudElement;
import net.minecraft.client.Minecraft;

public class IngameHud {
	
	private static IngameHud instance;
	
	/**
	 * Returns the currently active {@link CustomizableIngameGui} instance.<br>
	 * The editor uses a different instance than the normal HUD.
	 */
	public CustomizableIngameGui getGui() {
		if (Minecraft.getInstance().currentScreen instanceof LayoutEditorScreen) {
			return ((LayoutEditorScreen)Minecraft.getInstance().currentScreen).ingameHud;
		}
		return CustomizationHandler.INGAME_GUI;
	}
	
	/**
	 * If the customization light mode is enabled.<br>
	 * When light mode is enabled, the vanilla HUD does not get overridden and all vanilla HUD element customizations are disabled.<br>
	 * In this mode, you can't customize vanilla elements, it's only possible to add customization items like images, text, etc. to the HUD.<br><br>
	 * 
	 * <b>If enabled, you don't need to care about getting customized vanilla element positions, as they are all just the default ones.</b>
	 */
	public boolean isLightModeEnabled() {
		return CustomizationHandler.isLightModeEnabled();
	}
	
	/**
	 * The player air bar.
	 */
	public AirBarHudElement getAirBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.airBarElement;
		}
		return null;
	}
	
	/**
	 * The player armor bar.
	 */
	public ArmorBarHudElement getArmorBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.armorBarElement;
		}
		return null;
	}
	
	/**
	 * The boss bar element with <b>all</b> active boss bars.<br>
	 */
	public BossBarHudElement getBossBarElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.bossBarElement;
		}
		return null;
	}
	
	/**
	 * The crosshair.
	 */
	public CrosshairHudElement getCrosshair() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.crosshairElement;
		}
		return null;
	}
	
	/**
	 * The element that contains the player experience bar and the horse jump bar.
	 */
	public ExperienceJumpBarHudElement getExperienceJumpBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.experienceJumpBarElement;
		}
		return null;
	}
	
	/**
	 * The element that contains the player food bar and the mount health bar.
	 */
	public FoodMountHealthHudElement getFoodMountHealthBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.foodMountHealthElement;
		}
		return null;
	}
	
	/**
	 * The hotbar.
	 */
	public HotbarHudElement getHotbar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.hotbarElement;
		}
		return null;
	}
	
	/**
	 * The overlay/status message element.<br>
	 * Used to display the "you can only sleep at night" text and more.
	 */
	public OverlayMessageHudElement getOverlayMessageElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.overlayMessageElement;
		}
		return null;
	}
	
	/**
	 * The player health bar.
	 */
	public PlayerHealthHudElement getPlayerHealthBar() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.healthElement;
		}
		return null;
	}
	
	/**
	 * The element that shows the name of the selected hotbar item when switching it.
	 */
	public SelectedItemNameHudElement getSelectedItemNameElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.selectedItemNameElement;
		}
		return null;
	}
	
	/**
	 * The big (top) line of the title.<br>
	 * The title is the big message text rendered in the middle of the screen.
	 */
	public TitleHudElement getTitleElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.titleElement;
		}
		return null;
	}
	
	/**
	 * The smaller (bottom) line of the title.<br>
	 * The title is the big message text rendered in the middle of the screen.
	 */
	public TitleHudElement getSubTitleElement() {
		CustomizableIngameGui h = this.getGui();
		if (h != null) {
			return h.subtitleElement;
		}
		return null;
	}

	public static IngameHud getInstance() {
		if (instance == null) {
			instance = new IngameHud();
		}
		return instance;
	}

}
