package de.keksuccino.spiffyhud.api;

import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.*;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

public class InGameHudOverlay {

	//TODO übernehmen
	protected static Map<String, Boolean> elementRendering = new HashMap<>();
	protected static Map<String, Boolean> elementActive = new HashMap<>();
	//-------------------
	
	/**
	 * Returns the currently active {@link CustomizableIngameGui} instance.<br>
	 * The editor uses a different instance than the normal HUD.
	 */
	public static CustomizableIngameGui getGui() {
		if (Minecraft.getInstance().screen instanceof LayoutEditorScreen) {
			return ((LayoutEditorScreen)Minecraft.getInstance().screen).ingameHud;
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
	public static boolean isLightModeEnabled() {
		return CustomizationHandler.isLightModeEnabled();
	}

	//TODO übernehmen
	/**
	 * Activate/Deactivate a specific element by its identifier.<br>
	 * If an element is deactivated, it will not get registered, which means it will not occur in the HUD and the editor.<br><br>
	 *
	 * <b>Identifiers:</b><br>
	 * - 'air' : Air bar element<br>
	 * - 'armor' : Armor bar element<br>
	 * - 'boss' : Boss bar element<br>
	 * - 'crosshair' : Crosshair element<br>
	 * - 'experience' : Experience bar element<br>
	 * - 'mountjump' : Mount jump bar element<br>
	 * - 'food' : Player food bar element<br>
	 * - 'mounthealth' : Mount health bar element<br>
	 * - 'hotbar' : Hotbar element<br>
	 * - 'overlaymessage' : Overlay message element<br>
	 * - 'playerhealth' : Player health bar element<br>
	 * - 'selecteditem' : Selected item text element<br>
	 * - 'title' : Title text element<br>
	 * - 'subtitle' : Subtitle text element<br>
	 * - 'sidebar' : Sidebar
	 */
	public static void setElementActive(String elementIdentifier, boolean isActive) {
		if (elementActive.isEmpty()) {
			isElementActive("");
		}

		if (elementIdentifier.equals("air")) {
			elementActive.put("air", isActive);
		}
		if (elementIdentifier.equals("armor")) {
			elementActive.put("armor", isActive);
		}
		if (elementIdentifier.equals("boss")) {
			elementActive.put("boss", isActive);
		}
		if (elementIdentifier.equals("crosshair")) {
			elementActive.put("crosshair", isActive);
		}
		if (elementIdentifier.equals("experience") || elementIdentifier.equals("mountjump")) {
			elementActive.put("experience", isActive);
			elementActive.put("mountjump", isActive);
		}
		if (elementIdentifier.equals("food") || elementIdentifier.equals("mounthealth")) {
			elementActive.put("food", isActive);
			elementActive.put("mounthealth", isActive);
		}
		if (elementIdentifier.equals("hotbar")) {
			elementActive.put("hotbar", isActive);
		}
		if (elementIdentifier.equals("overlaymessage")) {
			elementActive.put("overlaymessage", isActive);
		}
		if (elementIdentifier.equals("playerhealth")) {
			elementActive.put("playerhealth", isActive);
		}
		if (elementIdentifier.equals("selecteditem")) {
			elementActive.put("selecteditem", isActive);
		}
		if (elementIdentifier.equals("title")) {
			elementActive.put("title", isActive);
		}
		if (elementIdentifier.equals("subtitle")) {
			elementActive.put("subtitle", isActive);
		}
		if (elementIdentifier.equals("sidebar")) {
			elementActive.put("sidebar", isActive);
		}
	}

	//TODO übernehmen
	/**
	 * Get if and element was deactivated via the API.<br><br>
	 *
	 * <b>Identifiers:</b><br>
	 * - 'air' : Air bar element<br>
	 * - 'armor' : Armor bar element<br>
	 * - 'boss' : Boss bar element<br>
	 * - 'crosshair' : Crosshair element<br>
	 * - 'experience' : Experience bar element<br>
	 * - 'mountjump' : Mount jump bar element<br>
	 * - 'food' : Player food bar element<br>
	 * - 'mounthealth' : Mount health bar element<br>
	 * - 'hotbar' : Hotbar element<br>
	 * - 'overlaymessage' : Overlay message element<br>
	 * - 'playerhealth' : Player health bar element<br>
	 * - 'selecteditem' : Selected item text element<br>
	 * - 'title' : Title text element<br>
	 * - 'subtitle' : Subtitle text element<br>
	 * - 'sidebar' : Sidebar
	 */
	public static boolean isElementActive(String elementIdentifier) {
		if (elementActive.isEmpty()) {
			elementActive.put("air", true);
			elementActive.put("armor", true);
			elementActive.put("boss", true);
			elementActive.put("crosshair", true);
			elementActive.put("experience", true);
			elementActive.put("mountjump", true);
			elementActive.put("food", true);
			elementActive.put("mounthealth", true);
			elementActive.put("hotbar", true);
			elementActive.put("overlaymessage", true);
			elementActive.put("playerhealth", true);
			elementActive.put("selecteditem", true);
			elementActive.put("title", true);
			elementActive.put("subtitle", true);
			elementActive.put("sidebar", true);
		}
		if (elementActive.containsKey(elementIdentifier)) {
			return elementActive.get(elementIdentifier);
		}
		return false;
	}

	//TODO übernehmen
	/**
	 * Enable/Disable rendering for a specific element by its identifier.<br>
	 * If rendering is disabled for an element, it will still be part of the HUD and is customizable in the editor, it just doesn't get rendered.<br><br>
	 *
	 * <b>Identifiers:</b><br>
	 * - 'air' : Air bar element<br>
	 * - 'armor' : Armor bar element<br>
	 * - 'boss' : Boss bar element<br>
	 * - 'crosshair' : Crosshair element<br>
	 * - 'experience' : Experience bar element<br>
	 * - 'mountjump' : Mount jump bar element<br>
	 * - 'food' : Player food bar element<br>
	 * - 'mounthealth' : Mount health bar element<br>
	 * - 'hotbar' : Hotbar element<br>
	 * - 'overlaymessage' : Overlay message element<br>
	 * - 'playerhealth' : Player health bar element<br>
	 * - 'selecteditem' : Selected item text element<br>
	 * - 'title' : Title text element<br>
	 * - 'subtitle' : Subtitle text element<br>
	 * - 'sidebar' : Sidebar
	 */
	public static void setRenderingForElement(String elementIdentifier, boolean renderElement) {
		if (elementRendering.isEmpty()) {
			isRenderingEnabledForElement("");
		}

		if (elementIdentifier.equals("air")) {
			elementRendering.put("air", renderElement);
		}
		if (elementIdentifier.equals("armor")) {
			elementRendering.put("armor", renderElement);
		}
		if (elementIdentifier.equals("boss")) {
			elementRendering.put("boss", renderElement);
		}
		if (elementIdentifier.equals("crosshair")) {
			elementRendering.put("crosshair", renderElement);
		}
		if (elementIdentifier.equals("experience") || elementIdentifier.equals("mountjump")) {
			elementRendering.put("experience", renderElement);
			elementRendering.put("mountjump", renderElement);
		}
		if (elementIdentifier.equals("food") || elementIdentifier.equals("mounthealth")) {
			elementRendering.put("food", renderElement);
			elementRendering.put("mounthealth", renderElement);
		}
		if (elementIdentifier.equals("hotbar")) {
			elementRendering.put("hotbar", renderElement);
		}
		if (elementIdentifier.equals("overlaymessage")) {
			elementRendering.put("overlaymessage", renderElement);
		}
		if (elementIdentifier.equals("playerhealth")) {
			elementRendering.put("playerhealth", renderElement);
		}
		if (elementIdentifier.equals("selecteditem")) {
			elementRendering.put("selecteditem", renderElement);
		}
		if (elementIdentifier.equals("title")) {
			elementRendering.put("title", renderElement);
		}
		if (elementIdentifier.equals("subtitle")) {
			elementRendering.put("subtitle", renderElement);
		}
		if (elementIdentifier.equals("sidebar")) {
			elementRendering.put("sidebar", renderElement);
		}
	}

	//TODO übernehmen
	/**
	 * Get if rendering is enabled for an element or was disabled via the API.<br><br>
	 *
	 * <b>Identifiers:</b><br>
	 * - 'air' : Air bar element<br>
	 * - 'armor' : Armor bar element<br>
	 * - 'boss' : Boss bar element<br>
	 * - 'crosshair' : Crosshair element<br>
	 * - 'experience' : Experience bar element<br>
	 * - 'mountjump' : Mount jump bar element<br>
	 * - 'food' : Player food bar element<br>
	 * - 'mounthealth' : Mount health bar element<br>
	 * - 'hotbar' : Hotbar element<br>
	 * - 'overlaymessage' : Overlay message element<br>
	 * - 'playerhealth' : Player health bar element<br>
	 * - 'selecteditem' : Selected item text element<br>
	 * - 'title' : Title text element<br>
	 * - 'subtitle' : Subtitle text element<br>
	 * - 'sidebar' : Sidebar
	 */
	public static boolean isRenderingEnabledForElement(String elementIdentifier) {
		if (elementRendering.isEmpty()) {
			elementRendering.put("air", true);
			elementRendering.put("armor", true);
			elementRendering.put("boss", true);
			elementRendering.put("crosshair", true);
			elementRendering.put("experience", true);
			elementRendering.put("mountjump", true);
			elementRendering.put("food", true);
			elementRendering.put("mounthealth", true);
			elementRendering.put("hotbar", true);
			elementRendering.put("overlaymessage", true);
			elementRendering.put("playerhealth", true);
			elementRendering.put("selecteditem", true);
			elementRendering.put("title", true);
			elementRendering.put("subtitle", true);
			elementRendering.put("sidebar", true);
		}
		if (!isElementActive(elementIdentifier)) {
			return false;
		}
		if (elementRendering.containsKey(elementIdentifier)) {
			return elementRendering.get(elementIdentifier);
		}
		return false;
	}

	/**
	 * Gets the properties of an element by its name/identifier.<br><br>
	 *
	 * <b>Identifiers:</b><br>
	 * - 'air' : Air bar element<br>
	 * - 'armor' : Armor bar element<br>
	 * - 'boss' : Boss bar element<br>
	 * - 'crosshair' : Crosshair element<br>
	 * - 'experience' : Experience bar element<br>
	 * - 'mountjump' : Mount jump bar element<br>
	 * - 'food' : Player food bar element<br>
	 * - 'mounthealth' : Mount health bar element<br>
	 * - 'hotbar' : Hotbar element<br>
	 * - 'overlaymessage' : Overlay message element<br>
	 * - 'playerhealth' : Player health bar element<br>
	 * - 'selecteditem' : Selected item text element<br>
	 * - 'title' : Title text element<br>
	 * - 'subtitle' : Subtitle text element<br>
	 * - 'sidebar' : Sidebar
	 */
	public static Map<String, String> getPropertiesFor(String elementIdentifier) {
		if (elementIdentifier.equals("air")) {
			return getAirBarProperties();
		}
		if (elementIdentifier.equals("armor")) {
			return getArmorBarProperties();
		}
		if (elementIdentifier.equals("boss")) {
			return getBossBarProperties();
		}
		if (elementIdentifier.equals("crosshair")) {
			return getCrosshairProperties();
		}
		if (elementIdentifier.equals("experience") || elementIdentifier.equals("mountjump")) {
			return getExperienceJumpBarProperties();
		}
		if (elementIdentifier.equals("food") || elementIdentifier.equals("mounthealth")) {
			return getFoodMountHealthProperties();
		}
		if (elementIdentifier.equals("hotbar")) {
			return getHotbarProperties();
		}
		if (elementIdentifier.equals("overlaymessage")) {
			return getOverlayMessageProperties();
		}
		if (elementIdentifier.equals("playerhealth")) {
			return getPlayerHealthBarProperties();
		}
		if (elementIdentifier.equals("selecteditem")) {
			return getSelectedItemNameProperties();
		}
		if (elementIdentifier.equals("title")) {
			return getTitleProperties();
		}
		if (elementIdentifier.equals("subtitle")) {
			return getSubtitleProperties();
		}
		//TODO übernehmen
		if (elementIdentifier.equals("sidebar")) {
			return getSidebarProperties();
		}
		return null;
	}
	
	/**
	 * The player air bar element.
	 */
	public static AirBarHudElement getAirBar() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.airBarElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the air bar, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getAirBarProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getAirBar());
		return m;
	}
	
	/**
	 * The player armor bar element.
	 */
	public static ArmorBarHudElement getArmorBar() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.armorBarElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the armor bar, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getArmorBarProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getArmorBar());
		return m;
	}
	
	/**
	 * The boss bar element with <b>all</b> active boss bars.<br>
	 */
	public static BossBarHudElement getBossBar() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.bossBarElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the boss bar, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getBossBarProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getBossBar());
		return m;
	}
	
	/**
	 * The crosshair.
	 */
	public static CrosshairHudElement getCrosshair() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.crosshairElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the crosshair, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getCrosshairProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getCrosshair());
		return m;
	}
	
	/**
	 * The element that contains the player experience bar and the horse jump bar.
	 */
	public static ExperienceJumpBarHudElement getExperienceJumpBar() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.experienceJumpBarElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the experience/mount jump bar, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getExperienceJumpBarProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getExperienceJumpBar());
		return m;
	}
	
	/**
	 * The element that contains the player food bar and the mount health bar.
	 */
	public static FoodMountHealthHudElement getFoodMountHealthBar() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.foodMountHealthElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the player food/mount health bar, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getFoodMountHealthProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getFoodMountHealthBar());
		return m;
	}
	
	/**
	 * The hotbar.
	 */
	public static HotbarHudElement getHotbar() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.hotbarElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the hotbar, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getHotbarProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getHotbar());
		return m;
	}
	
	/**
	 * The overlay/status message element.<br>
	 * Used to display the "you can only sleep at night" text and more.
	 */
	public static OverlayMessageHudElement getOverlayMessage() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.overlayMessageElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the overlay message, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getOverlayMessageProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getOverlayMessage());
		return m;
	}
	
	/**
	 * The player health bar.
	 */
	public static PlayerHealthHudElement getPlayerHealthBar() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.healthElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the player health bar, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getPlayerHealthBarProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getPlayerHealthBar());
		return m;
	}
	
	/**
	 * The element that shows the name of the selected hotbar item when switching it.
	 */
	public static SelectedItemNameHudElement getSelectedItemName() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.selectedItemNameElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the selected item name, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getSelectedItemNameProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getSelectedItemName());
		return m;
	}
	
	/**
	 * The big (top) line of the title.<br>
	 * The title is the big message text rendered in the middle of the screen.
	 */
	public static TitleHudElement getTitle() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.titleElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the title, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getTitleProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getTitle());
		return m;
	}
	
	/**
	 * The smaller (bottom) line of the title.<br>
	 * The title is the big message text rendered in the middle of the screen.
	 */
	public static TitleHudElement getSubTitle() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.subtitleElement;
		}
		return null;
	}

	/**
	 * Gets the current properties of the subtitle, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getSubtitleProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getSubTitle());
		return m;
	}

	//TODO übernehmen
	/**
	 * The sidebar element.
	 */
	public static SidebarHudElement getSidebar() {
		CustomizableIngameGui h = InGameHudOverlay.getGui();
		if (h != null) {
			return h.sidebarElement;
		}
		return null;
	}

	//TODO übernehmen
	/**
	 * Gets the current properties of the sidebar, packed to a map.<br><br>
	 *
	 * <b>Properties:</b><br>
	 * - 'visible' : If the element is currently visible or not.<br>
	 * - 'width' : The current width of the element.<br>
	 * - 'height' : The current height of the element.<br>
	 * - 'x' : The current X position of the element.<br>
	 * - 'y' : The current Y position of the element.<br>
	 * - 'scale' : The current scale of the element.
	 */
	public static Map<String, String> getSidebarProperties() {
		Map<String, String> m = buildBasicPropertiesForElement(getSidebar());
		return m;
	}

	protected static Map<String, String> buildBasicPropertiesForElement(IngameHudElement element) {

		Map<String, String> m = new HashMap<>();

		if (element != null) {
			m.put("visible", "" + element.visible);
			m.put("width", "" + element.width);
			m.put("height", "" + element.height);
			m.put("x", "" + element.x);
			m.put("y", "" + element.y);
			m.put("scale", "" + element.scale);
		}

		return m;

	}

}
