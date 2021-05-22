package de.keksuccino.fancyhud.customization.helper;

import java.awt.Color;
import java.io.File;
import java.util.List;

import com.google.common.io.Files;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.FancyHud;
import de.keksuccino.fancyhud.customization.CustomizationHandler;
import de.keksuccino.fancyhud.customization.CustomizationPropertiesHandler;
import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.fancyhud.customization.helper.ui.UIBase;
import de.keksuccino.fancyhud.customization.helper.ui.content.CustomizationButton;
import de.keksuccino.fancyhud.customization.helper.ui.content.FHContextMenu;
import de.keksuccino.fancyhud.customization.helper.ui.content.MenuBar;
import de.keksuccino.fancyhud.customization.helper.ui.content.MenuBar.ElementAlignment;
import de.keksuccino.fancyhud.customization.helper.ui.popup.FHYesNoPopup;
import de.keksuccino.konkrete.file.FileUtils;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.AdvancedImageButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.properties.PropertiesSet;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;

public class CustomizationHelperUI extends UIBase {
	
	public static MenuBar bar;

	protected static int tick = 0;
	
	public static CustomizationHelperScreen currentHelperScreen;
	
	protected static final ResourceLocation CLOSE_BUTTON_TEXTURE = new ResourceLocation("fancyhud", "close_btn.png");
	protected static final ResourceLocation RELOAD_BUTTON_TEXTURE = new ResourceLocation("keksuccino", "/filechooser/back_icon.png");
	
	public static void updateUI() {
		try {
			
			boolean extended = true;
			if (bar != null) {
				extended = bar.isExtended();
			}
			
			bar = new MenuBar();
			
			/** HUD TAB **/
			FHContextMenu hudMenu = new FHContextMenu();
			hudMenu.setAutoclose(true);
			bar.addChild(hudMenu, "fh.ui.tab.hud", ElementAlignment.LEFT);
			
			FHContextMenu layoutsMenu = new FHContextMenu();
			layoutsMenu.setAutoclose(true);
			hudMenu.addChild(layoutsMenu);
			
			CustomizationButton newLayoutButton = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.ui.hud.layouts.new"), true, (press) -> {
				LayoutEditorScreen.isActive = true;
				Minecraft.getInstance().displayGuiScreen(new LayoutEditorScreen());
			});
			newLayoutButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.ui.hud.layouts.new.desc"), "%n%"));
			layoutsMenu.addContent(newLayoutButton);
			
			ManageLayoutsContextMenu manageLayoutsMenu = new ManageLayoutsContextMenu();
			manageLayoutsMenu.setAutoclose(true);
			layoutsMenu.addChild(manageLayoutsMenu);
			
			CustomizationButton manageLayoutsButton = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.ui.hud.layouts.manage"), true, (press) -> {
				manageLayoutsMenu.setParentButton((AdvancedButton) press);
				manageLayoutsMenu.openMenuAt(press);
			});
			manageLayoutsButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.ui.hud.layouts.manage.desc"), "%n%"));
			layoutsMenu.addContent(manageLayoutsButton);
			
			CustomizationButton layoutsButton = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.ui.hud.layouts"), true, (press) -> {
				layoutsMenu.setParentButton((AdvancedButton) press);
				layoutsMenu.openMenuAt(0, press.y);
			});
			layoutsButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.ui.hud.layouts.desc"), "%n%"));
			hudMenu.addContent(layoutsButton);
			
			CustomizationButton hudTab = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.ui.hud"), true, (press) -> {
				hudMenu.setParentButton((AdvancedButton) press);
				hudMenu.openMenuAt(press.x, press.y + press.getHeightRealms());
			});
			bar.addElement(hudTab, "fh.ui.tab.hud", ElementAlignment.LEFT, false);
			
			/** TOOLS TAB **/
			FHContextMenu toolsMenu = new FHContextMenu();
			toolsMenu.setAutoclose(true);
			bar.addChild(toolsMenu, "fh.ui.tab.tools", ElementAlignment.LEFT);
			
			String backgroundOverlayLabel = Locals.localize("fancyhud.helper.ui.tools.backgroundoverlay.on");
			if (!CustomizationHelperScreen.renderBackgroundOverlay) {
				backgroundOverlayLabel = Locals.localize("fancyhud.helper.ui.tools.backgroundoverlay.off");
			}
			CustomizationButton backgroundOverlayButton = new CustomizationButton(0, 0, 0, 0, backgroundOverlayLabel, true, (press) -> {
				if (CustomizationHelperScreen.renderBackgroundOverlay) {
					CustomizationHelperScreen.renderBackgroundOverlay = false;
					((CustomizationButton)press).setMessage(Locals.localize("fancyhud.helper.ui.tools.backgroundoverlay.off"));
				} else {
					CustomizationHelperScreen.renderBackgroundOverlay = true;
					((CustomizationButton)press).setMessage(Locals.localize("fancyhud.helper.ui.tools.backgroundoverlay.on"));
				}
			});
			backgroundOverlayButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.ui.tools.backgroundoverlay.desc"), "%n%"));
			toolsMenu.addContent(backgroundOverlayButton);
			
			CustomizationButton toolsTab = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.ui.tools"), true, (press) -> {
				toolsMenu.setParentButton((AdvancedButton) press);
				toolsMenu.openMenuAt(press.x, press.y + press.getHeightRealms());
			});
			bar.addElement(toolsTab, "fh.ui.tab.tools", ElementAlignment.LEFT, false);
			
			/** CLOSE HELPER BUTTON **/
			AdvancedImageButton closeGuiButtonTab = new AdvancedImageButton(20, 20, 20, 20, CLOSE_BUTTON_TEXTURE, true, (press) -> {
				Minecraft.getInstance().displayGuiScreen(null);
			}) {
				@Override
				public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
					this.width = this.height;
					super.render(matrix, mouseX, mouseY, partialTicks);
				}
			};
			closeGuiButtonTab.ignoreLeftMouseDownClickBlock = true;
			closeGuiButtonTab.ignoreBlockedInput = true;
			closeGuiButtonTab.enableRightclick = true;
			closeGuiButtonTab.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.ui.closehelper"), "%n%"));
			bar.addElement(closeGuiButtonTab, "fh.ui.tab.closehelper", ElementAlignment.RIGHT, false);
			
			/** RELOAD BUTTON **/
			AdvancedImageButton reloadButtonTab = new AdvancedImageButton(20, 20, 20, 20, RELOAD_BUTTON_TEXTURE, true, (press) -> {
				CustomizationHandler.reloadSystem();
			}) {
				@Override
				public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
					this.width = this.height;
					super.render(matrix, mouseX, mouseY, partialTicks);
				}
			};
			reloadButtonTab.ignoreLeftMouseDownClickBlock = true;
			reloadButtonTab.ignoreBlockedInput = true;
			reloadButtonTab.enableRightclick = true;
			reloadButtonTab.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.ui.reload.desc"), "%n%"));
			bar.addElement(reloadButtonTab, "fh.ui.tab.reload", ElementAlignment.RIGHT, false);
			
			/** EXPAND BUTTON **/
			AdvancedButton expandButton = bar.getElement("menubar.default.extendbtn");
			if (expandButton != null) {
				if (expandButton instanceof AdvancedImageButton) {
					if (!extended) {
						((AdvancedImageButton)expandButton).setImage(MenuBar.EXPAND_BTN_TEXTURE);
						expandButton.setDescription(Locals.localize("fancyhud.helper.menubar.expand"));
					}
				}
			}
			
			bar.setExtended(extended);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void render(MatrixStack matrix, Screen screen) {
		try {
			
			if (bar != null) {
				if (!PopupHandler.isPopupActive()) {

					RenderUtils.setZLevelPre(matrix, 400);

					renderUnicodeWarning(matrix, screen);

					RenderUtils.setZLevelPost(matrix);

					bar.render(matrix, screen);

				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void renderUnicodeWarning(MatrixStack matrix, Screen screen) {
		if (Minecraft.getInstance().gameSettings.forceUnicodeFont) {
			String title = Locals.localize("fancyhud.helper.ui.warning");
			int w = Minecraft.getInstance().fontRenderer.getStringWidth(title);
			String[] lines = StringUtils.splitLines(Locals.localize("fancyhud.helper.ui.warning.unicode"), "%n%");
			for (String s : lines) {
				int w2 = Minecraft.getInstance().fontRenderer.getStringWidth(s);
				if (w2 > w) {
					w = w2;
				}
			}
			
			int x = screen.width - w - 5;
			int y = (int) ((bar.getHeight() + 5) * UIBase.getUIScale());
			
			RenderSystem.enableBlend();
			
			int h = 13;
			if (lines.length > 0) {
				h += 10*lines.length;
			}
			fill(matrix, x - 4, y, x + w + 2, y + h, new Color(230, 15, 0, 240).getRGB());

			drawString(matrix, Minecraft.getInstance().fontRenderer, title, x, y + 2, Color.WHITE.getRGB());
			
			int i = 0;
			for (String s : lines) {
				drawString(matrix, Minecraft.getInstance().fontRenderer, s, x, y + 13 + i, Color.WHITE.getRGB());
				i += 10;
			}
			
			RenderSystem.disableBlend();
		}
	}

	private static class ManageLayoutsContextMenu extends FHContextMenu {

		private ManageLayoutsSubContextMenu manageSubPopup;
		
		public ManageLayoutsContextMenu() {
			
			this.manageSubPopup = new ManageLayoutsSubContextMenu();
			this.addChild(this.manageSubPopup);
			
		}

		public void openMenuAt(Widget parentBtn) {
			this.content.clear();
			
			List<PropertiesSet> enabled = CustomizationPropertiesHandler.getProperties();
			if (!enabled.isEmpty()) {
				for (PropertiesSet s : enabled) {
					List<PropertiesSection> secs = s.getPropertiesOfType("customization-meta");
					if (secs.isEmpty()) {
						secs = s.getPropertiesOfType("type-meta");
					}
					if (!secs.isEmpty()) {
						String name = "<missing name>";
						PropertiesSection meta = secs.get(0);
						File f = new File(meta.getEntryValue("path"));
						if (f.isFile()) {
							name = Files.getNameWithoutExtension(f.getName());
							
							int totalactions = s.getProperties().size() - 1;
							CustomizationButton layoutEntryBtn = new CustomizationButton(0, 0, 0, 0, "§a" + name, (press) -> {
								this.manageSubPopup.setParentButton((AdvancedButton) press);
								this.manageSubPopup.openMenuAt(0, press.y, f, false);
							});
							layoutEntryBtn.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.buttons.customization.managelayouts.layout.btndesc", Locals.localize("fancyhud.helper.buttons.customization.managelayouts.enabled"), "" + totalactions), "%n%"));
							this.addContent(layoutEntryBtn);
						}
					}
				}
			}
			
			List<PropertiesSet> disabled = CustomizationPropertiesHandler.getDisabledProperties();
			if (!disabled.isEmpty()) {
				for (PropertiesSet s : disabled) {
					List<PropertiesSection> secs = s.getPropertiesOfType("customization-meta");
					if (secs.isEmpty()) {
						secs = s.getPropertiesOfType("type-meta");
					}
					if (!secs.isEmpty()) {
						String name = "<missing name>";
						PropertiesSection meta = secs.get(0);
						File f = new File(meta.getEntryValue("path"));
						if (f.isFile()) {
							name = Files.getNameWithoutExtension(f.getName());
							
							int totalactions = s.getProperties().size() - 1;
							CustomizationButton layoutEntryBtn = new CustomizationButton(0, 0, 0, 0, "§c" + name, (press) -> {
								this.manageSubPopup.setParentButton((AdvancedButton) press);
								this.manageSubPopup.openMenuAt(0, press.y, f, true);
							});
							layoutEntryBtn.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.buttons.customization.managelayouts.layout.btndesc", Locals.localize("fancyhud.helper.buttons.customization.managelayouts.disabled"), "" + totalactions), "%n%"));
							this.addContent(layoutEntryBtn);
						}
					}
				}
			}
			
			if (enabled.isEmpty() && disabled.isEmpty()) {
				CustomizationButton emptyBtn = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.creator.empty"), (press) -> {});
				this.addContent(emptyBtn);
			}

			this.openMenuAt(parentBtn.x - this.getWidth() - 2, parentBtn.y);
		}
		
		@Override
		public void render(MatrixStack matrix, int mouseX, int mouseY) {
			super.render(matrix, mouseX, mouseY);
			
			if (this.manageSubPopup != null) {
				this.manageSubPopup.render(matrix, mouseX, mouseY);
				if (!this.isOpen()) {
					this.manageSubPopup.closeMenu();
				}
			}
		}
		
		@Override
		public void closeMenu() {
			if (!this.manageSubPopup.isHovered()) {
				super.closeMenu();
			}
		}
		
		public void forceCloseMenu() {
			super.closeMenu();
		}
		
		@Override
		public boolean isHovered() {
			if (this.manageSubPopup.isOpen() && this.manageSubPopup.isHovered()) {
				return true;
			} else {
				return super.isHovered();
			}
		}
		
	}

	private static class ManageLayoutsSubContextMenu extends FHContextMenu {

		public void openMenuAt(int x, int y, File layout, boolean disabled) {
			
			this.content.clear();
			
			String toggleLabel = Locals.localize("fancyhud.helper.buttons.customization.managelayouts.disable");
			if (disabled) {
				toggleLabel = Locals.localize("fancyhud.helper.buttons.customization.managelayouts.enable");
			}
			CustomizationButton toggleLayoutBtn = new CustomizationButton(0, 0, 0, 0, toggleLabel, (press) -> {
				if (disabled) {
					String name = FileUtils.generateAvailableFilename(FancyHud.CUSTOMIZATION_DIR.getPath(), Files.getNameWithoutExtension(layout.getName()), "fhlayout");
					FileUtils.copyFile(layout, new File(FancyHud.CUSTOMIZATION_DIR.getPath() + "/" + name));
					layout.delete();
				} else {
					String disPath = FancyHud.CUSTOMIZATION_DIR.getPath() + "/.disabled";
					String name = FileUtils.generateAvailableFilename(disPath, Files.getNameWithoutExtension(layout.getName()), "fhlayout");
					FileUtils.copyFile(layout, new File(disPath + "/" + name));
					layout.delete();
				}
				CustomizationHandler.reloadSystem();
				if ((this.parent != null) && (this.parent instanceof ManageLayoutsContextMenu)) {
					((ManageLayoutsContextMenu)this.parent).forceCloseMenu();
				}
			});
			this.addContent(toggleLayoutBtn);

			CustomizationButton editLayoutBtn = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.ui.hud.layouts.manage.edit"), (press) -> {
				CustomizationHandler.editLayout(layout);
			});
			editLayoutBtn.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.ui.hud.layouts.manage.edit.desc"), "%n%"));
			this.addContent(editLayoutBtn);
			
			CustomizationButton openInTextEditorBtn = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.buttons.customization.managelayouts.openintexteditor"), (press) -> {
				CustomizationHandler.openFile(layout);
			});
			openInTextEditorBtn.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.buttons.customization.managelayouts.openintexteditor.desc"), "%n%"));
			this.addContent(openInTextEditorBtn);
			
			CustomizationButton deleteLayoutBtn = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.buttons.customization.managelayouts.delete"), (press) -> {
				PopupHandler.displayPopup(new FHYesNoPopup(300, new Color(0, 0, 0, 0), 240, (call) -> {
					if (call) {
						if (layout.exists()) {
							layout.delete();
							CustomizationHandler.reloadSystem();
							if ((this.parent != null) && (this.parent instanceof ManageLayoutsContextMenu)) {
								((ManageLayoutsContextMenu)this.parent).forceCloseMenu();
							}
						}
					}
				}, Locals.localize("fancyhud.helper.buttons.customization.managelayouts.delete.msg"), "", "", "", ""));
				CustomizationHandler.reloadSystem();
				if ((this.parent != null) && (this.parent instanceof ManageLayoutsContextMenu)) {
					((ManageLayoutsContextMenu)this.parent).forceCloseMenu();
				}
			});
			this.addContent(deleteLayoutBtn);
			
			this.openMenuAt(x, y);
			
		}
	}

}
