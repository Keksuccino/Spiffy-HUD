package de.keksuccino.spiffyhud.customization.items;

import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.customization.rendering.slideshow.ExternalTextureSlideshowRenderer;
import de.keksuccino.spiffyhud.customization.rendering.slideshow.SlideshowHandler;
import net.minecraft.client.util.math.MatrixStack;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class SlideshowCustomizationItem extends CustomizationItemBase {

	public ExternalTextureSlideshowRenderer renderer = null;
	
	public SlideshowCustomizationItem(PropertiesSection item) {
		super(item);
		
		if ((this.action != null) && this.action.equalsIgnoreCase("addslideshow")) {
			this.value = item.getEntryValue("name");
			if ((this.value != null) && SlideshowHandler.slideshowExists(this.value)) {
				this.renderer = SlideshowHandler.getSlideshow(this.value);
			} else {
				if (SpiffyHud.config.getOrDefault("showdebugwarnings", true)) {
					System.out.println("###################### WARNING [FANCYMENU] ######################");
					System.out.println("SLIDESHOW NOT FOUND: " + this.value);
					System.out.println("#################################################################");
				}
			}
		}
	}

	public void render(MatrixStack matrix) {

		if (!this.shouldRender()) {
			return;
		}

		int x = this.getPosX();
		int y = this.getPosY();
		
		if ((this.renderer != null) && this.renderer.isReady()) {
			int cachedX = this.renderer.x;
			int cachedY = this.renderer.y;
			int cachedWidth = this.renderer.width;
			int cachedHeight = this.renderer.height;

			this.renderer.slideshowOpacity = this.opacity;
			
			this.renderer.x = x;
			this.renderer.y = y;
			
			if (this.height > -1) {
				this.renderer.height = this.height;
			}
			if (this.width > -1) {
				this.renderer.width = this.width;
			}
			
			this.renderer.render(matrix);
			
			this.renderer.x = cachedX;
			this.renderer.y = cachedY;
			this.renderer.width = cachedWidth;
			this.renderer.height = cachedHeight;
		}
	}

}
