package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.vertex.PoseStack;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

public abstract class IngameHudElement extends GuiComponent {
	
	protected CustomizableIngameGui handler;
	
	public boolean fireEvents = true;
	
	public boolean visible = true;
	public int width = 20;
	public int height = 20;
	public int x = 10;
	public int y = 10;
	public float scale = 1.0F;
	public float rotation = 0.0F;
	public float opacity = 1.0F;
	
	protected Minecraft mc = Minecraft.getInstance();
	
	public IngameHudElement(CustomizableIngameGui handler) {
		this.handler = handler;
	}
	
	public abstract void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks);
	
	public CustomizableIngameGui getHandler() {
		return this.handler;
	}
	
	/**
	 * Only for internal use. It's not recommended to manually set the handler at any point!
	 */
	public void setHandler(CustomizableIngameGui handler) {
		this.handler = handler;
	}
	
	public static enum BarAlignment {
		LEFT("left"),
		RIGHT("right");
		
		String name;
		
		private BarAlignment(String name) {
			this.name = name;
		}
		
		public static BarAlignment byName(String name) {
			for (BarAlignment a : BarAlignment.values()) {
				if (a.name.equals(name)) {
					return a;
				}
			}
			return null;
		}
		
		public String getName() {
			return this.name;
		}
		
	}

}
