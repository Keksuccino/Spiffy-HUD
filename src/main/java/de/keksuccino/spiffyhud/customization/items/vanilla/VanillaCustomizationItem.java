package de.keksuccino.spiffyhud.customization.items.vanilla;

import net.minecraft.client.util.math.MatrixStack;
import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.customization.items.CustomizationItemBase;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class VanillaCustomizationItem extends CustomizationItemBase {

	public IngameHudElement element;
	public boolean isSecondItemOfThisType = false;
	
	public boolean isOriginalPosX = false;
	public boolean isOriginalPosY = false;
	public boolean isOriginalOrientation = true;
	
	public float rotation = 0.0F;
	public float scale = 1.0F;
	public boolean vanillaVisible = true;
	public boolean fireEvents = true;
	
	public VanillaCustomizationItem(IngameHudElement element, String elementDisplayName, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(props);
		
		this.isSecondItemOfThisType = isSecondItemOfThisType;
		this.element = element;
		this.value = elementDisplayName;
		
		if (props == null) {
			SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: VanillaCustomizationItem: props null");
			return;
		}
		
		if (element == null) {
			SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: VanillaCustomizationItem: element null");
			return;
		}

		String sc = props.getEntryValue("scale");
		if ((sc != null) && MathUtils.isFloat(sc)) {
			this.scale = Float.parseFloat(sc);
		}
		
		//unused
		String ro = props.getEntryValue("rotation");
		if ((ro != null) && MathUtils.isFloat(ro)) {
			this.rotation = Float.parseFloat(ro);
		}
		
		String vs = props.getEntryValue("visible");
		if ((vs != null) && vs.equalsIgnoreCase("false")) {
			this.vanillaVisible = false;
		}
		
		String events = props.getEntryValue("fireevents");
		if ((events != null) && events.equalsIgnoreCase("false")) {
			this.fireEvents = false;
		}
		
		String ori = props.getEntryValue("orientation");
		if (ori != null) {
			this.isOriginalOrientation = false;
		}
		
		if (this.isOriginalOrientation) {
			this.posX = Integer.MAX_VALUE;
			this.posY = Integer.MAX_VALUE;
		}
		
		String x = props.getEntryValue("x");
		String y = props.getEntryValue("y");
		if (x != null) {
			if (MathUtils.isInteger(x)) {
				this.posX = Integer.parseInt(x);
			}
		}
		if (y != null) {
			if (MathUtils.isInteger(y)) {
				this.posY = Integer.parseInt(y);
			}
		}
		
		this.width = this.element.width;
		this.height = this.element.height;
		
	}

	@Override
	public void render(MatrixStack matrix) {
		
		if (this.element == null) {
			SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: VanillaCustomizationItem: element null");
			return;
		}
		
		this.width = this.element.width;
		this.height = this.element.height;
		
		if (!(this.isOriginalPosX && this.isSecondItemOfThisType)) {
			this.element.x = this.getPosX();
		}
		if (!(this.isOriginalPosY && this.isSecondItemOfThisType)) {
			this.element.y = this.getPosY();
		}
		
//		this.element.rotation = this.rotation;
//		this.element.opacity = this.opacity;
		
		this.element.scale = this.scale;
		
		if (!this.vanillaVisible || !this.isSecondItemOfThisType) {
			this.element.visible = this.vanillaVisible;
		}
		
		if (!this.fireEvents || !this.isSecondItemOfThisType) {
			this.element.fireEvents = this.fireEvents;
		}
		
	}

}
