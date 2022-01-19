package de.keksuccino.spiffyhud.api.item;

import de.keksuccino.spiffyhud.SpiffyHud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Deprecated
public class CustomizationItemRegistry {
	
	private Map<String, CustomizationItemContainer> elements = new TreeMap<String, CustomizationItemContainer>();
	
	private static CustomizationItemRegistry instance;

	@Deprecated
	public void register(CustomizationItemContainer container) {
		if (!elements.containsKey(container.elementIdentifier)) {
			this.elements.put(container.elementIdentifier, container);
		} else {
			SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: CustomizationElementRegistry#register():");
			SpiffyHud.LOGGER.error("Invalid element identifier '" + container.elementIdentifier + "' found!");
			SpiffyHud.LOGGER.error("Customization element with the same identifier already exists!");
		}
	}

	@Deprecated
	public Map<String, CustomizationItemContainer> getElements() {
		return this.elements;
	}

	@Deprecated
	public List<CustomizationItemContainer> getElementsAsList() {
		List<CustomizationItemContainer> l = new ArrayList<CustomizationItemContainer>();
		l.addAll(this.elements.values());
		return l;
	}

	@Deprecated
	public CustomizationItemContainer getElement(String identifier) {
		return this.elements.get(identifier);
	}

	@Deprecated
	public boolean elementExists(String identifier) {
		return this.elements.containsKey(identifier);
	}

	@Deprecated
	public static CustomizationItemRegistry getInstance() {
		if (instance == null) {
			instance = new CustomizationItemRegistry();
		}
		return instance;
	}

}
