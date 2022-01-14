package de.keksuccino.spiffyhud.customization.items.custombars;

import java.awt.Color;
import java.io.File;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.items.CustomizationItemBase;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.konkrete.resources.ExternalTextureResourceLocation;
import de.keksuccino.konkrete.resources.TextureHandler;

public abstract class CustomBarCustomizationItemBase extends CustomizationItemBase {
	
	public BarDirection direction = BarDirection.RIGHT;
	
	public Color barColor = new Color(0, 0, 0);
	public Color backgroundColor = new Color(0, 0, 0, 50);
	
	public Identifier barTexture = null;
	public Identifier backgroundTexture = null;
	public Identifier barEndTexture = null;
	public int barEndTextureWidth = 10;
	public int barEndTextureHeight = 10;
	
	public String barColorHex = null;
	public String backgroundColorHex = null;
	public String barTexturePath = null;
	public String backgroundTexturePath = null;
	public String barEndTexturePath = null;

	protected int currentPercentWidthHeight = 0;
	
	public CustomBarCustomizationItemBase(PropertiesSection item) {
		super(item);
		this.init(item);
	}
	
	public void init(PropertiesSection item) {
		
		this.barColorHex = item.getEntryValue("barcolor");
		this.backgroundColorHex = item.getEntryValue("backgroundcolor");
		this.barTexturePath = item.getEntryValue("bartexture");
		this.backgroundTexturePath = item.getEntryValue("backgroundtexture");
		this.barEndTexturePath = item.getEntryValue("barendtexture");

		String barEndWidthString = item.getEntryValue("barendtexturewidth");
		if ((barEndWidthString != null) && MathUtils.isInteger(barEndWidthString)) {
			this.barEndTextureWidth = Integer.parseInt(barEndWidthString);
		}

		String barEndHeightString = item.getEntryValue("barendtextureheight");
		if ((barEndHeightString != null) && MathUtils.isInteger(barEndHeightString)) {
			this.barEndTextureHeight = Integer.parseInt(barEndHeightString);
		}
		
		String barDirec = item.getEntryValue("direction");
		if (barDirec != null) {
			this.direction = BarDirection.byName(barDirec);
		}
		
		this.updateItem();
		
	}

	protected void renderBar(MatrixStack matrix) {

		if (this.barTexture == null) {

			if (this.direction == BarDirection.RIGHT) {
				RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.currentPercentWidthHeight, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
			}
			if (this.direction == BarDirection.LEFT) {
				RenderUtils.fill(matrix, this.getPosX() + this.width - this.currentPercentWidthHeight, this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
			}
			if (this.direction == BarDirection.UP) {
				RenderUtils.fill(matrix, this.getPosX(), this.getPosY() + this.height - this.currentPercentWidthHeight, this.getPosX() + this.width, this.getPosY() + this.height, this.barColor.getRGB(), 1.0F);
			}
			if (this.direction == BarDirection.DOWN) {
				RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.currentPercentWidthHeight, this.barColor.getRGB(), 1.0F);
			}

		} else {

			int mainTextureWidth = this.width;
			if (this.barEndTexture != null) {
				mainTextureWidth -= this.barEndTextureWidth;
			}

			RenderUtils.bindTexture(this.barTexture);
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

			if (this.direction == BarDirection.RIGHT) {
				drawTexture(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.currentPercentWidthHeight, this.height, mainTextureWidth, this.height);
			}
			if (this.direction == BarDirection.LEFT) {
				int i = (mainTextureWidth - this.currentPercentWidthHeight);
				drawTexture(matrix, this.getPosX() + i, this.getPosY(), i, 0.0F, this.currentPercentWidthHeight, this.height, mainTextureWidth, this.height);
			}
			if (this.direction == BarDirection.UP) {
				int i = (this.height - this.currentPercentWidthHeight);
				drawTexture(matrix, this.getPosX(), this.getPosY() + i, 0.0F, i, mainTextureWidth, this.currentPercentWidthHeight, mainTextureWidth, this.height);
			}
			if (this.direction == BarDirection.DOWN) {
				drawTexture(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, mainTextureWidth, this.currentPercentWidthHeight, mainTextureWidth, this.height);
			}

			if (this.barEndTexture != null) {
				RenderUtils.bindTexture(this.barEndTexture);

				if (this.direction == BarDirection.RIGHT) {
					drawTexture(matrix, this.getPosX() + this.currentPercentWidthHeight, this.getPosY(), 0.0F, 0.0F, this.barEndTextureWidth, this.barEndTextureHeight, this.barEndTextureWidth, this.barEndTextureHeight);
				}
				if (this.direction == BarDirection.LEFT) {
					int i = (mainTextureWidth - this.currentPercentWidthHeight) - this.barEndTextureWidth;
					drawTexture(matrix, this.getPosX() + i, this.getPosY(), i, 0.0F, this.barEndTextureWidth, this.barEndTextureHeight, this.barEndTextureWidth, this.barEndTextureHeight);
				}
				if (this.direction == BarDirection.UP) {
					int i = (this.height - this.currentPercentWidthHeight) - this.barEndTextureHeight;
					drawTexture(matrix, this.getPosX(), this.getPosY() + i, 0.0F, i, this.barEndTextureWidth, this.barEndTextureHeight, this.barEndTextureWidth, this.barEndTextureHeight);
				}
				if (this.direction == BarDirection.DOWN) {
					drawTexture(matrix, this.getPosX(), this.getPosY() + this.currentPercentWidthHeight, 0.0F, 0.0F, this.barEndTextureWidth, this.barEndTextureHeight, this.barEndTextureWidth, this.barEndTextureHeight);
				}
			}

			RenderSystem.disableBlend();

		}

	}

	protected void renderBarBackground(MatrixStack matrix) {

		if (this.backgroundTexture == null) {

			RenderUtils.fill(matrix, this.getPosX(), this.getPosY(), this.getPosX() + this.width, this.getPosY() + this.height, this.backgroundColor.getRGB(), 1.0F);

		} else {

			RenderUtils.bindTexture(this.backgroundTexture);
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexture(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.height, this.width, this.height);
			RenderSystem.disableBlend();

		}

	}
	
	public void updateItem() {
		
		if (this.barColorHex != null) {
			Color c = RenderUtils.getColorFromHexString(this.barColorHex);
			if (c != null) {
				this.barColor = c;
			}
		}

		if (this.backgroundColorHex != null) {
			Color c = RenderUtils.getColorFromHexString(this.backgroundColorHex);
			if (c != null) {
				this.backgroundColor = c;
			}
		}

		if (this.barTexturePath != null) {
			File f = new File(this.barTexturePath);
			if (f.exists() && f.isFile() && (f.getPath().toLowerCase().endsWith(".jpg") || f.getPath().toLowerCase().endsWith(".jpeg") || f.getPath().toLowerCase().endsWith(".png"))) {
				ExternalTextureResourceLocation er = TextureHandler.getResource(this.barTexturePath);
				if (er != null) {
					er.loadTexture();
					this.barTexture = er.getResourceLocation();
				}
			}
		} else {
			this.barTexture = null;
		}

		if (this.backgroundTexturePath != null) {
			File f = new File(this.backgroundTexturePath);
			if (f.exists() && f.isFile() && (f.getPath().toLowerCase().endsWith(".jpg") || f.getPath().toLowerCase().endsWith(".jpeg") || f.getPath().toLowerCase().endsWith(".png"))) {
				ExternalTextureResourceLocation er = TextureHandler.getResource(this.backgroundTexturePath);
				if (er != null) {
					er.loadTexture();
					this.backgroundTexture = er.getResourceLocation();
				}
			}
		} else {
			this.backgroundTexture = null;
		}

		if (this.barEndTexturePath != null) {
			File f = new File(this.barEndTexturePath);
			if (f.exists() && f.isFile() && (f.getPath().toLowerCase().endsWith(".jpg") || f.getPath().toLowerCase().endsWith(".jpeg") || f.getPath().toLowerCase().endsWith(".png"))) {
				ExternalTextureResourceLocation er = TextureHandler.getResource(this.barEndTexturePath);
				if (er != null) {
					er.loadTexture();
					this.barEndTexture = er.getResourceLocation();
				}
			}
		} else {
			this.barEndTexture = null;
		}
		
	}
	
	protected boolean isEditor() {
		return (MinecraftClient.getInstance().currentScreen instanceof LayoutEditorScreen);
	}
	
	public static enum BarDirection {
		LEFT("left"),
		RIGHT("right"),
		UP("up"),
		DOWN("down");
		
		private String name;
		
		BarDirection(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
		
		public static BarDirection byName(String name) {
			for (BarDirection d : BarDirection.values()) {
				if (d.name.equals(name)) {
					return d;
				}
			}
			return BarDirection.LEFT;
		}
	}

}
