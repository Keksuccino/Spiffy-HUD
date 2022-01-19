//TODO übernehmen
package de.keksuccino.spiffyhud.api.hud.v2;

import de.keksuccino.spiffyhud.SpiffyHud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanillaHudElementRegistry {

    protected static Map<String, VanillaHudElementContainer> hudElements = new HashMap<String, VanillaHudElementContainer>();

    /**
     * Register your own HUD elements here.
     */
    public static void registerElement(VanillaHudElementContainer element) {
        if (element != null) {
            if (element.getIdentifier() != null) {
                if (hudElements.containsKey(element.getIdentifier())) {
                    SpiffyHud.LOGGER.warn("[SPIFFY HUD] WARNING! A HUD element with the identifier '" + element.getIdentifier() + "' is already registered! Overriding element!");
                }
                hudElements.put(element.getIdentifier(), element);
            } else {
                SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR! Element identifier cannot be null for HudElementContainers!");
            }
        }
    }

    /**
     * Unregister a previously added element.
     */
    public static void unregisterElement(String elementIdentifier) {
        hudElements.remove(elementIdentifier);
    }

    /**
     * Get all registered elements as list.
     */
    public static List<VanillaHudElementContainer> getElements() {
        List<VanillaHudElementContainer> l = new ArrayList<>();
        l.addAll(hudElements.values());
        return l;
    }

    /**
     * Get a registered element by its identifier.
     */
    public static VanillaHudElementContainer getElement(String itemIdentifier) {
        return hudElements.get(itemIdentifier);
    }

}
