package de.keksuccino.spiffyhud.api.hud;

import net.minecraft.client.util.math.MatrixStack;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.spiffyhud.customization.items.vanilla.VanillaCustomizationItem;

@Deprecated
public class CustomVanillaCustomizationItem extends VanillaCustomizationItem {
	
	public HudElementContainer container;

	@Deprecated
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
