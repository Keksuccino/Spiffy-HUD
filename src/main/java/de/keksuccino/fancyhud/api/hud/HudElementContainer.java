package de.keksuccino.fancyhud.api.hud;

import java.util.Map;

import javax.annotation.Nullable;

import de.keksuccino.fancyhud.customization.helper.ui.content.FHContextMenu;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;

public abstract class HudElementContainer {

	public final String elementIdentifier;
	public IngameHudElement element;
	
	public String displayName = "Hud Element";

	public int defaultWidth = 20;
	public int defaultHeight = 20;
	public int defaultPosX = 0;
	public int defaultPosY = 0;
	public String defaultOrientation = "top-left";

	/**
	 * This container is used to register a custom HUD element that should act like vanilla elements.<br>
	 * It will be part of the HUD by default and can't be fully deleted from it (only hidden like normal vanilla elements).
	 * 
	 * @param uniqueElementIdentifier The unique identifier of your element. Identifiers should look like {@code some.unique.identifier}.
	 * @param element The {@link IngameHudElement} instance of your element. This is the actual element that is rendered in the HUD later.
	 * @param displayName The display name that is shown in the layout editor.
	 * @param width The element width. Vanilla elements are not designed to get resized by the user. Only change this later if your element changes it's size by itself.
	 * @param height The element height. Vanilla elements are not designed to get resized by the user. Only change this later if your element changes it's size by itself.
	 * @param defaultOrientation The default orientation. This is the origin point from where the position of this element is calculated. Supported orientations are: {@code top-left, mid-left, bottom-left, top-centered, mid-centered, bottom-centered, top-right, mid-right, bottom-right}.
	 * @param defaultPosX = The default X position from the origin point (orientation) of this element.
	 * @param defaultPosY = The default Y position from the origin point (orientation) of this element.
	 */
	public HudElementContainer(String uniqueElementIdentifier, IngameHudElement element, String displayName, int width, int height, String defaultOrientation, int defaultPosX, int defaultPosY) {
		this.elementIdentifier = uniqueElementIdentifier;
		this.element = element;
		this.displayName = displayName;
		this.defaultWidth = width;
		this.defaultHeight = height;
		this.defaultPosX = defaultPosX;
		this.defaultPosY = defaultPosY;
		this.defaultOrientation = defaultOrientation;
		
		element.width = this.defaultWidth;
		element.height = this.defaultHeight;
	}
	
	/**
	 * Called to reset all custom element variables to it's defaults.<br><br>
	 * 
	 * Use this to reset your element's non-default variables, otherwise, customizations for these variables will still be applied even if the layout containing the customizations isn't active anymore.<br>
	 * If you don't have any, leave it empty.
	 */
	public abstract void onResetElement();
	
	/**
	 * Called to apply/update customizations for the element.<br>
	 * Is called when the screen gets resized or the ingame HUD needs another kind of update.<br><br>
	 * 
	 * Here you need to apply all of your <b>custom</b> customization properties of the {@link PropertiesSection} arg to your property cache variables.<br>
	 * <b>It's not recommended to apply customizations directly to the {@link IngameHudElement} here!</b><br>
	 * Make a variable in this {@link HudElementContainer} instance and apply customizations to this variable first. That's what I call the "property cache variables".<br><br>
	 * 
	 * Default properties ({@code orientation}, {@code x}, {@code y} and {@code visibility}) get applied/updated automatically.
	 * 
	 * @param properties All properties from the layout file that need to be applied to the element. Will always contain all serialized properties that were previously written to the layout file via {@link HudElementContainer#getProperties()}.
	 */
	public abstract void onUpdateElement(PropertiesSection properties);
	
	/**
	 * Called every render tick to update values and do other stuff.<br><br>
	 * This is the right place to apply customizations from your property cache variables to the actual {@link IngameHudElement}.
	 * 
	 * @param item The currently active {@link CustomVanillaCustomizationItem} that handles all customizations for the element.
	 */
	public void onTick(CustomVanillaCustomizationItem item) {
		
		if (!item.orientation.equals(this.defaultOrientation)) {
			item.isOriginalOrientation = false;
		}

		if (item.posX == Integer.MAX_VALUE) {
			item.posX = this.defaultPosX;
			item.isOriginalPosX = true;
		}
		if ((item.posX != this.defaultPosX) || !item.isOriginalOrientation) {
			item.isOriginalPosX = false;
		}

		if (item.posY == Integer.MAX_VALUE) {
			item.posY = this.defaultPosY;
			item.isOriginalPosY = true;
		}
		if ((item.posY != this.defaultPosY) || !item.isOriginalOrientation) {
			item.isOriginalPosY = false;
		}
		
	}
	
	/**
	 * Called when the editor gets updated or initialized.<br>
	 * Can be used to add custom entries to the rightclick context menu of the element and do other stuff that needs to be done on init.
	 * 
	 * @param layoutElement Handles everything around the HUD element in the editor and renders the element-related editor UI like the element border when the element is focused.
	 * @param rightclickContextMenu The context menu that appears when you rightclick the element in the editor. This menu already contains default actions every element needs and is part of the <b>layoutElement</b>.
	 */
	public abstract void onInitEditor(CustomVanillaLayoutElement layoutElement, FHContextMenu rightclickContextMenu);
	
	/**
	 * Returns a {@link Map} with the <b>current</b> properties of the HUD element.<br>
	 * This is used by the editor to write the element properties to the layout file.<br><br>
	 * 
	 * Can be used to add custom properties to the element.<br>
	 * Default properties ({@code orientation}, {@code x}, {@code y} and {@code visibility}) get added automatically, so you don't need to add these.<br>
	 * <b>Keep in mind that you need to handle (apply) custom properties in {@link HudElementContainer#onUpdateElement(PropertiesSection)}!</b><br><br> 
	 * 
	 * <b><b>It's really important to only save customized variables (variables that don't have the default value) here! Otherwise they could override real customizations.</b></b><br><br>
	 * 
	 * The first {@link String} of the {@link Map} is the properties key and the second one the actual value.
	 */
	@Nullable
	public abstract Map<String, String> getProperties();

}