//TODO übernehmen
package de.keksuccino.spiffyhud.api.hud.v2;

import java.util.Map;

import javax.annotation.Nullable;

import de.keksuccino.spiffyhud.customization.helper.ui.content.FHContextMenu;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;

public abstract class VanillaHudElementContainer {

    private final String elementIdentifier;

    public IngameHudElement element;

    public int defaultWidth = 20;
    public int defaultHeight = 20;
    public int defaultPosX = 0;
    public int defaultPosY = 0;
    public String defaultOrientation = "top-left";

    /**
     * A vanilla HUD element container.<br>
     * Is used to register a custom vanilla-like HUD element.<br>
     * It will be part of the HUD by default and can't be completely deleted from it (only hidden like normal vanilla elements).<br><br>
     *
     * Needs to be registered to the {@link VanillaHudElementRegistry}.
     *
     * @param uniqueElementIdentifier The unique identifier of your element. Identifiers should look like {@code some.unique.identifier}.
     * @param element The {@link IngameHudElement} instance of your element. This is the actual element that is rendered in the HUD later.
     * @param width The element width. Vanilla elements are not designed to get resized by the user. Only change this later if your element changes it's size by itself.
     * @param height The element height. Vanilla elements are not designed to get resized by the user. Only change this later if your element changes it's size by itself.
     * @param defaultOrientation The default orientation. This is the origin point from where the position of this element is calculated. Supported orientations are: {@code top-left, mid-left, bottom-left, top-centered, mid-centered, bottom-centered, top-right, mid-right, bottom-right}.
     * @param defaultPosX = The default X position from the origin point (orientation) of this element.
     * @param defaultPosY = The default Y position from the origin point (orientation) of this element.
     */
    public VanillaHudElementContainer(String uniqueElementIdentifier, IngameHudElement element, int width, int height, String defaultOrientation, int defaultPosX, int defaultPosY) {
        this.elementIdentifier = uniqueElementIdentifier;
        this.element = element;
        this.defaultWidth = width;
        this.defaultHeight = height;
        this.defaultPosX = defaultPosX;
        this.defaultPosY = defaultPosY;
        this.defaultOrientation = defaultOrientation;

        element.width = this.defaultWidth;
        element.height = this.defaultHeight;
    }

    /**
     * Called to reset all custom element variables to its defaults.<br><br>
     *
     * Use this to reset your element's non-default variables, otherwise, customizations for these variables will still be applied even if the layout containing the customizations isn't active anymore.<br>
     * If you don't have any, leave it empty.
     */
    public abstract void onResetElement();

    /**
     * Called to apply/update customizations for the element.<br>
     * Is called when the screen gets resized or the in-game HUD needs another kind of update.<br><br>
     *
     * Here you need to apply all of your <b>custom</b> customization properties of the {@link PropertiesSection} arg to your property cache variables.<br>
     * <b>It's not recommended to apply customizations directly to the {@link IngameHudElement} here!</b><br>
     * Make a variable in this {@link VanillaHudElementContainer} instance and apply customizations to this variable first. That's what I call the "property cache variables".<br><br>
     *
     * Default properties ({@code orientation}, {@code x}, {@code y} and {@code visibility}) get applied/updated automatically.
     *
     * @param properties All properties from the layout file that need to be applied to the element. Will always contain all serialized properties that were previously written to the layout file via {@link VanillaHudElementContainer#getProperties()}.
     */
    public abstract void onUpdateElement(PropertiesSection properties);

    /**
     * Called every render tick to update values and do other stuff.<br><br>
     * This is the right place to apply customizations from your property cache variables to the actual {@link IngameHudElement}.
     *
     * @param element The currently active {@link SimpleVanillaCustomizationItem} that handles all customizations for the element.
     */
    public void onTick(SimpleVanillaCustomizationItem element) {

        if (!element.orientation.equals(this.defaultOrientation)) {
            element.isOriginalOrientation = false;
        }

        if (element.posX == Integer.MAX_VALUE) {
            element.posX = this.defaultPosX;
            element.isOriginalPosX = true;
        }
        if ((element.posX != this.defaultPosX) || !element.isOriginalOrientation) {
            element.isOriginalPosX = false;
        }

        if (element.posY == Integer.MAX_VALUE) {
            element.posY = this.defaultPosY;
            element.isOriginalPosY = true;
        }
        if ((element.posY != this.defaultPosY) || !element.isOriginalOrientation) {
            element.isOriginalPosY = false;
        }

    }

    /**
     * Called when the editor gets updated or initialized.<br>
     * Can be used to add custom entries to the right-click context menu of the element and do other stuff that needs to be done on init.
     *
     * @param layoutElement Handles everything around the HUD element in the editor and renders the element-related editor UI like the element border when the element is focused.
     * @param rightClickContextMenu The context menu that appears when you right-click the element in the editor. This menu already contains default actions every element needs and is part of the <b>layoutElement</b>.
     */
    public abstract void onInitEditor(VanillaLayoutEditorElement layoutElement, FHContextMenu rightClickContextMenu);

    /**
     * Returns a {@link Map} with the <b>current</b> properties of the HUD element.<br>
     * This is used by the editor to write the element properties to the layout file.<br><br>
     *
     * Can be used to add custom properties to the element.<br>
     * Default properties ({@code orientation}, {@code x}, {@code y} and {@code visibility}) get added automatically, so you don't need to add these.<br>
     * <b>Keep in mind that you need to handle (apply) custom properties in {@link VanillaHudElementContainer#onUpdateElement(PropertiesSection)}!</b><br><br>
     *
     * <b><b>It's really important to only save customized variables (variables that don't have the default value) here! Otherwise they could override real customizations.</b></b><br><br>
     *
     * The first {@link String} of the {@link Map} is the properties key and the second one the actual value.
     */
    @Nullable
    public abstract Map<String, String> getProperties();

    /**
     * Returns the display name of this element. Used in the layout editor.<br><br>
     *
     * You can localize the name here.
     */
    public abstract String getDisplayName();

    public String getIdentifier() {
        return this.elementIdentifier;
    }

}