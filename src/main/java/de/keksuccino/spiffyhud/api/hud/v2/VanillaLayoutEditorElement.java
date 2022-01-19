package de.keksuccino.spiffyhud.api.hud.v2;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.api.IngameHud;
import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.VanillaLayoutElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Layout editor elements can be seen as a holder for the actual vanilla element in the editor.<br>
 * It allows the user to edit the element. It contains the right-click context menu when right-clicking an element and handlers to move the element and more.
 */
public class VanillaLayoutEditorElement extends VanillaLayoutElement {

	public VanillaHudElementContainer container;
	
	public VanillaLayoutEditorElement(VanillaHudElementContainer container, SimpleVanillaCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
		this.container = container;
		this.container.onInitEditor(this, this.rightclickMenu);
	}
	
	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY) {
		
		super.render(matrix, mouseX, mouseY);

		if (IngameHud.getInstance().getGui().isEditor()) {
			Window w = MinecraftClient.getInstance().getWindow();
			this.container.element.render(matrix, w.getScaledWidth(), w.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());
		}
		
	}

	@Override
	public List<PropertiesSection> getProperties() {
		
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		PropertiesSection sec = new PropertiesSection("customization");

		sec.addEntry("action", "custom_vanilla_layout_element:" + this.container.getIdentifier());
		
		if (!this.getVanillaObject().isOriginalOrientation) {
			sec.addEntry("orientation", this.object.orientation);
		}
		if (!this.getVanillaObject().isOriginalPosX) {
			sec.addEntry("x", "" + this.object.posX);
		}
		if (!this.getVanillaObject().isOriginalPosY) {
			sec.addEntry("y", "" + this.object.posY);
		}
		if (!this.getVanillaObject().vanillaVisible) {
			sec.addEntry("visible", "" + this.getVanillaObject().vanillaVisible);
		}
		
		Map<String, String> customProps = this.container.getProperties();
		if (customProps != null) {
			for (Map.Entry<String, String> m : customProps.entrySet()) {
				if (!sec.hasEntry(m.getKey())) {
					sec.addEntry(m.getKey(), m.getValue());
				} else {
					printPropertiesKeyError(m.getKey());
				}
			}
		}
		
		l.add(sec);
		
		return l;
		
	}
	
	protected void printPropertiesKeyError(String key) {
		SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: CustomVanillaLayoutElement#getProperties():");
		SpiffyHud.LOGGER.error("Invalid properties key '" + key + "' found for HUD element '" + this.container.getIdentifier() + "'!");
		SpiffyHud.LOGGER.error("This key already exists or is reserved by the system and can't be used!");
	}

	@Override
	public void resetElement() {
		PropertiesSection props = new PropertiesSection("customization");
		this.object = new SimpleVanillaCustomizationItem(this.container, props, false);
		this.container.onResetElement();
	}

}
