//TODO übernehmen
package de.keksuccino.spiffyhud.api.hud.v2;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.spiffyhud.customization.items.vanilla.VanillaCustomizationItem;

/**
 * Just like normal elements, every vanilla element has a customization item.<br>
 * In case of vanilla elements, the customization item works more like a ticker to update stuff, it doesn't directly render the vanilla element.
 */
public class SimpleVanillaCustomizationItem extends VanillaCustomizationItem {
	
	public VanillaHudElementContainer container;
	
	public SimpleVanillaCustomizationItem(VanillaHudElementContainer container, PropertiesSection properties, boolean isSecondItemOfThisType) {
		super(container.element, container.getDisplayName(), properties, isSecondItemOfThisType);
		
		this.container = container;
		
		if (this.isOriginalOrientation) {
			this.orientation = container.defaultOrientation;
		}
		
		this.container.onUpdateElement(properties);
	}
	
	@Override
	public void render(MatrixStack matrix) {
		
		this.container.onTick(this);
		
		super.render(matrix);
		
	}

}
