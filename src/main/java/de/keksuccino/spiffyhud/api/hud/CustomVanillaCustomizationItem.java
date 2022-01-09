package de.keksuccino.spiffyhud.api.hud;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.spiffyhud.customization.items.vanilla.VanillaCustomizationItem;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class CustomVanillaCustomizationItem extends VanillaCustomizationItem {
	
	public HudElementContainer container;
	
	public CustomVanillaCustomizationItem(HudElementContainer container, PropertiesSection properties, boolean isSecondItemOfThisType) {
		super(container.element, container.displayName, properties, isSecondItemOfThisType);
		
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
