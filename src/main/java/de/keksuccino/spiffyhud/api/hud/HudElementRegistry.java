package de.keksuccino.spiffyhud.api.hud;

import de.keksuccino.spiffyhud.SpiffyHud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HudElementRegistry {
	
	private Map<String, HudElementContainer> elements = new TreeMap<String, HudElementContainer>();
	
	private static HudElementRegistry instance;

	public void register(HudElementContainer container) {
		if (!elements.containsKey(container.elementIdentifier)) {
			this.elements.put(container.elementIdentifier, container);
		} else {
			SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: HudElementRegistry#register():", "Invalid element identifier '" + container.elementIdentifier + "' found!", "HUD element with the same identifier already exists!");
		}
	}
	
	public Map<String, HudElementContainer> getElements() {
		return this.elements;
	}
	
	public List<HudElementContainer> getElementsAsList() {
		List<HudElementContainer> l = new ArrayList<HudElementContainer>();
		l.addAll(this.elements.values());
		return l;
	}
	
	public HudElementContainer getElement(String identifier) {
		return this.elements.get(identifier);
	}
	
	public boolean elementExists(String identifier) {
		return this.elements.containsKey(identifier);
	}
	
	public static HudElementRegistry getInstance() {
		if (instance == null) {
			instance = new HudElementRegistry();
		}
		return instance;
	}

}
