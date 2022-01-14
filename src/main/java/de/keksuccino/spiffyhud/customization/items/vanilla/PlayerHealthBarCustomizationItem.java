package de.keksuccino.spiffyhud.customization.items.vanilla;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.PlayerHealthHudElement;
import net.minecraft.client.util.math.MatrixStack;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement.BarAlignment;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class PlayerHealthBarCustomizationItem extends VanillaCustomizationItem {

	public boolean hideWhenFull = false;
	
	public BarAlignment barAlignment = BarAlignment.LEFT;
	
	public PlayerHealthBarCustomizationItem(IngameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Player Health Bar", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "bottom-centered";
		}
		
		String align = props.getEntryValue("alignment");
		if ((align != null)) {
			BarAlignment a = BarAlignment.byName(align);
			if (a != null) {
				this.barAlignment = a;
			}
		}
		
		String hideFull = props.getEntryValue("hidewhenfull");
		if ((hideFull != null) && hideFull.equalsIgnoreCase("true")) {
			this.hideWhenFull = true;
		}
		
	}
	
	@Override
	public void render(MatrixStack matrix) {

		PlayerHealthHudElement he = ((PlayerHealthHudElement)this.element);
		
		if (this.hideWhenFull || !this.isSecondItemOfThisType) {
			he.hideWhenFull = this.hideWhenFull;
		}
		
		if (this.isOriginalOrientation) {
			he.isDefaultPos = true;
		}
		
		if (!this.orientation.equals("bottom-centered")) {
			this.isOriginalOrientation = false;
		}

		if (this.posX == Integer.MAX_VALUE) {
			this.posX = -51; //-91
			this.isOriginalPosX = true;
		}
		if ((this.posX != -51) || !this.isOriginalOrientation) {
			this.isOriginalPosX = false;
		}

		if (this.posY == Integer.MAX_VALUE) {
			this.posY = -30; //-39
			this.isOriginalPosY = true;
		}
		if ((this.posY != -30) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}

		if (this.posX != -51) {
			((PlayerHealthHudElement)this.element).isDefaultPos = false;
		}
		
		if ((this.barAlignment != BarAlignment.LEFT) || !this.isSecondItemOfThisType) {
			((PlayerHealthHudElement)this.element).alignment = this.barAlignment;
		}
		
		super.render(matrix);
	}

}
