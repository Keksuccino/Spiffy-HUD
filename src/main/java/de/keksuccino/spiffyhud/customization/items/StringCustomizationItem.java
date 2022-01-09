package de.keksuccino.spiffyhud.customization.items;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.dynamicvalues.DynamicValueHelper;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import de.keksuccino.spiffyhud.customization.rendering.RainbowText;
import net.minecraft.util.Mth;

public class StringCustomizationItem extends CustomizationItemBase {
	
	public float scale = 1.0F;
	public boolean shadow = false;
	public Alignment alignment = Alignment.LEFT;
	
	public boolean rainbowMode = false;
	public String rainbowStartColorHex1 = null;
	public String rainbowStartColorHex2 = null;
	public String rainbowEndColorHex1 = null;
	public String rainbowEndColorHex2 = null;
	
	public RainbowText rainbowText = new RainbowText("");
	
	public String valueRaw;
	
	public StringCustomizationItem(PropertiesSection item) {
		super(item);

		if ((this.action != null) && this.action.equalsIgnoreCase("addtext")) {
			
			this.valueRaw = item.getEntryValue("value");
			this.updateValue();
			
			String sh = item.getEntryValue("shadow");
			if ((sh != null)) {
				if (sh.equalsIgnoreCase("true")) {
					this.shadow = true;
				}
			}
			
			String sc = item.getEntryValue("scale");
			if ((sc != null) && MathUtils.isFloat(sc)) {
				this.scale = Float.parseFloat(sc);
			}
			
			String rainbow = item.getEntryValue("rainbowmode");
			if ((rainbow != null) && rainbow.equalsIgnoreCase("true")) {
				this.rainbowMode = true;
			}
			String rainbowSpeed = item.getEntryValue("rainbowspeed");
			if ((rainbowSpeed != null) && MathUtils.isFloat(rainbowSpeed)) {
				this.rainbowText.setSpeed(Float.parseFloat(rainbowSpeed));
			}
			this.rainbowStartColorHex1 = item.getEntryValue("rainbowstartcolor1");
			this.rainbowStartColorHex2 = item.getEntryValue("rainbowstartcolor2");
			this.rainbowEndColorHex1 = item.getEntryValue("rainbowendcolor1");
			this.rainbowEndColorHex2 = item.getEntryValue("rainbowendcolor2");
			
			this.updateRainbowColors();
			
		}
	}
	
	protected void updateValue() {
		
		if (this.valueRaw != null) {
			if (!isEditorActive()) {
				this.value = DynamicValueHelper.convertFromRaw(this.valueRaw);
			} else {
				this.value = StringUtils.convertFormatCodes(this.valueRaw, "&", "§");
			}
		}
		
		this.width = (int) (Minecraft.getInstance().font.width(this.value) * this.scale);
		this.height = (int) (Minecraft.getInstance().font.lineHeight * this.scale);
		
		if (this.rainbowMode) {
			this.rainbowText.setShadow(this.shadow);
			this.rainbowText.setText(this.value);
		}
		
	}

	@Override
	public void render(PoseStack matrix) {
		if (!this.shouldRender()) {
			return;
		}
		
		this.updateValue();
		
		int x = this.getPosX();
		int y = this.getPosY();
		Font font = Minecraft.getInstance().font;

		RenderSystem.enableBlend();
		matrix.pushPose();
		matrix.scale(this.scale, this.scale, this.scale);
		if (this.rainbowMode) {
			this.rainbowText.render(matrix, x, y);
		} else {
			if (this.shadow) {
				font.drawShadow(matrix, "§f" + this.value, x, y, 0 | Mth.ceil(this.opacity * 255.0F) << 24);
			} else {
				font.draw(matrix, "§f" + this.value, x, y, 0 | Mth.ceil(this.opacity * 255.0F) << 24);
			}
		}
		matrix.popPose();
		RenderSystem.disableBlend();

	}
	
	@Override
	public int getPosX() {
		return (int) (super.getPosX() / this.scale);
	}
	
	@Override
	public int getPosY() {
		return (int) (super.getPosY() / this.scale);
	}
	
	public boolean allRainbowColorsSet() {
		return ((this.rainbowStartColorHex1 != null) && (this.rainbowStartColorHex2 != null) && (this.rainbowEndColorHex1 != null) && (this.rainbowEndColorHex2 != null));
	}
	
	public void updateRainbowColors() {
		if (this.allRainbowColorsSet()) {
			Color start1 = RenderUtils.getColorFromHexString(this.rainbowStartColorHex1);
			Color start2 = RenderUtils.getColorFromHexString(this.rainbowStartColorHex2);
			Color end1 = RenderUtils.getColorFromHexString(this.rainbowEndColorHex1);
			Color end2 = RenderUtils.getColorFromHexString(this.rainbowEndColorHex2);
			if ((start1 != null) && (start2 != null) && (end1 != null) && (end2 != null)) {
				this.rainbowText.setStartColorRange(start1, start2);
				this.rainbowText.setEndColorRange(end1, end2);
			}
		}
	}

}
