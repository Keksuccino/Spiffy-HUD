package de.keksuccino.fancyhud.customization.helper.editor;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.io.Files;
import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.api.item.CustomizationItemContainer;
import de.keksuccino.fancyhud.api.item.CustomizationItemRegistry;
import de.keksuccino.fancyhud.customization.CustomizationHandler;
import de.keksuccino.fancyhud.customization.CustomizationPropertiesHandler;
import de.keksuccino.fancyhud.customization.helper.CustomizationHelperScreen;
import de.keksuccino.fancyhud.customization.helper.editor.elements.LayoutElement;
import de.keksuccino.fancyhud.customization.helper.editor.elements.custombars.LayoutCustomAirBar;
import de.keksuccino.fancyhud.customization.helper.editor.elements.custombars.LayoutCustomArmorBar;
import de.keksuccino.fancyhud.customization.helper.editor.elements.custombars.LayoutCustomExpBar;
import de.keksuccino.fancyhud.customization.helper.editor.elements.custombars.LayoutCustomFoodBar;
import de.keksuccino.fancyhud.customization.helper.editor.elements.custombars.LayoutCustomHealthBar;
import de.keksuccino.fancyhud.customization.helper.editor.elements.vanilla.VanillaLayoutElement;
import de.keksuccino.fancyhud.customization.helper.ui.UIBase;
import de.keksuccino.fancyhud.customization.helper.ui.content.CustomizationButton;
import de.keksuccino.fancyhud.customization.helper.ui.content.FHContextMenu;
import de.keksuccino.fancyhud.customization.helper.ui.content.MenuBar;
import de.keksuccino.fancyhud.customization.helper.ui.content.MenuBar.ElementAlignment;
import de.keksuccino.fancyhud.customization.helper.ui.popup.ChooseFilePopup;
import de.keksuccino.fancyhud.customization.helper.ui.popup.DynamicValueInputPopup;
import de.keksuccino.fancyhud.customization.helper.ui.popup.FHYesNoPopup;
import de.keksuccino.fancyhud.customization.items.custombars.CustomAirBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.custombars.CustomArmorBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.custombars.CustomExpBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.custombars.CustomFoodBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.custombars.CustomHealthBarCustomizationItem;
import de.keksuccino.fancyhud.customization.items.ShapeCustomizationItem.Shape;
import de.keksuccino.fancyhud.customization.rendering.slideshow.SlideshowHandler;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.content.AdvancedImageButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.gui.screens.popup.TextInputPopup;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.properties.PropertiesSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class LayoutEditorUI extends UIBase {
	
	public MenuBar bar;
	public LayoutEditorScreen parent;

	protected int tick = 0;
	
	protected static final ResourceLocation CLOSE_BUTTON_TEXTURE = new ResourceLocation("fancyhud", "close_btn.png");
	
	public LayoutEditorUI(LayoutEditorScreen parent) {
		this.parent = parent;
		this.updateUI();
	}
	
	public void updateUI() {
		try {
			
			boolean extended = true;
			if (bar != null) {
				extended = bar.isExtended();
			}
			
			bar = new MenuBar();
			bar.setExtended(extended);
			
			/** LAYOUT TAB **/
			FHContextMenu layoutMenu = new FHContextMenu();
			layoutMenu.setAutoclose(true);
			bar.addChild(layoutMenu, "fm.editor.ui.tab.layout", ElementAlignment.LEFT);
			
			AdvancedButton newLayoutButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.layout.new"), true, (press) -> {
				this.displayUnsavedWarning((call) -> {
					if (call) {
						Minecraft.getInstance().displayGuiScreen(new LayoutEditorScreen());
					}
				});
			});
			layoutMenu.addContent(newLayoutButton);
			
			OpenLayoutContextMenu openLayoutMenu = new OpenLayoutContextMenu(this);
			openLayoutMenu.setAutoclose(true);
			layoutMenu.addChild(openLayoutMenu);
			
			AdvancedButton openLayoutButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.layout.open"), true, (press) -> {
				openLayoutMenu.setParentButton((AdvancedButton) press);
				openLayoutMenu.openMenuAt(0, press.y);
			});
			layoutMenu.addContent(openLayoutButton);
			
			AdvancedButton layoutSaveButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.layout.save"), true, (press) -> {
				this.parent.saveLayout();
			});
			layoutMenu.addContent(layoutSaveButton);
			
			AdvancedButton layoutSaveAsButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.layout.saveas"), true, (press) -> {
				this.parent.saveLayoutAs();
			});
			layoutMenu.addContent(layoutSaveAsButton);
			
			LayoutPropertiesContextMenu layoutPropertiesMenu = new LayoutPropertiesContextMenu(this.parent, false);
			layoutPropertiesMenu.setAutoclose(true);
			layoutMenu.addChild(layoutPropertiesMenu);
			
			AdvancedButton layoutPropertiesButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.layout.properties"), true, (press) -> {
				layoutPropertiesMenu.setParentButton((AdvancedButton) press);
				layoutPropertiesMenu.openMenuAt(0, press.y);
			});
			layoutMenu.addContent(layoutPropertiesButton);
			
			AdvancedButton exitButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.exit"), true, (press) -> {
				this.closeEditor();
			});
			layoutMenu.addContent(exitButton);
			
			CustomizationButton layoutTab = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.layout"), true, (press) -> {
				layoutMenu.setParentButton((AdvancedButton) press);
				layoutMenu.openMenuAt(press.x, press.y + press.getHeight());
			});
			bar.addElement(layoutTab, "fm.editor.ui.tab.layout", ElementAlignment.LEFT, false);
			
			/** EDIT TAB **/
			FHContextMenu editMenu = new FHContextMenu();
			editMenu.setAutoclose(true);
			bar.addChild(editMenu, "fm.editor.ui.tab.edit", ElementAlignment.LEFT);
			
			AdvancedButton undoButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.edit.undo"), true, (press) -> {
				this.parent.history.stepBack();
				try {
					((LayoutEditorScreen)Minecraft.getInstance().currentScreen).ui.bar.getChild("fm.editor.ui.tab.edit").openMenuAt(editMenu.getX(), editMenu.getY());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			editMenu.addContent(undoButton);
			
			AdvancedButton redoButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.edit.redo"), true, (press) -> {
				this.parent.history.stepForward();
				try {
					((LayoutEditorScreen)Minecraft.getInstance().currentScreen).ui.bar.getChild("fm.editor.ui.tab.edit").openMenuAt(editMenu.getX(), editMenu.getY());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			editMenu.addContent(redoButton);
			
			editMenu.addSeparator();
			
			AdvancedButton copyButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.edit.copy"), true, (press) -> {
				this.parent.copySelectedElements();
			});
			editMenu.addContent(copyButton);
			
			AdvancedButton pasteButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.edit.paste"), true, (press) -> {
				this.parent.pasteElements();
			});
			editMenu.addContent(pasteButton);
			
			CustomizationButton editTab = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.edit"), true, (press) -> {
				editMenu.setParentButton((AdvancedButton) press);
				editMenu.openMenuAt(press.x, press.y + press.getHeight());
			});
			bar.addElement(editTab, "fm.editor.ui.tab.edit", ElementAlignment.LEFT, false);
			
			/** ELEMENT TAB **/
			FHContextMenu elementMenu = new FHContextMenu();
			elementMenu.setAutoclose(true);
			bar.addChild(elementMenu, "fm.editor.ui.tab.element", ElementAlignment.LEFT);
			
			NewElementContextMenu newElementMenu = new NewElementContextMenu(this.parent);
			newElementMenu.setAutoclose(true);
			elementMenu.addChild(newElementMenu);
			
			AdvancedButton newElementButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.element.new"), true, (press) -> {
				newElementMenu.setParentButton((AdvancedButton) press);
				newElementMenu.openMenuAt(0, press.y);
			});
			elementMenu.addContent(newElementButton);
			
			HiddenVanillaElementsContextMenu hiddenVanillaMenu = new HiddenVanillaElementsContextMenu(this.parent);
			hiddenVanillaMenu.setAutoclose(true);
			elementMenu.addChild(hiddenVanillaMenu);
			
			AdvancedButton hiddenVanillaButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.elements.vanilla.managedeleted"), true, (press) -> {
				hiddenVanillaMenu.setParentButton((AdvancedButton) press);
				hiddenVanillaMenu.openMenuAt(0, press.y);
			});
			elementMenu.addContent(hiddenVanillaButton);
			
			CustomizationButton elementTab = new CustomizationButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.ui.element"), true, (press) -> {
				elementMenu.setParentButton((AdvancedButton) press);
				elementMenu.openMenuAt(press.x, press.y + press.getHeight());
			});
			bar.addElement(elementTab, "fm.editor.ui.tab.element", ElementAlignment.LEFT, false);
			
			/** CLOSE GUI BUTTON TAB **/
			AdvancedImageButton exitEditorButtonTab = new AdvancedImageButton(20, 20, 0, 0, CLOSE_BUTTON_TEXTURE, true, (press) -> {
				this.closeEditor();
			}) {
				@Override
				public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
					this.width = this.height;
					super.render(matrix, mouseX, mouseY, partialTicks);
				}
			};
			exitEditorButtonTab.ignoreLeftMouseDownClickBlock = true;
			exitEditorButtonTab.ignoreBlockedInput = true;
			exitEditorButtonTab.enableRightclick = true;
			exitEditorButtonTab.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.ui.exit.desc"), "%n%"));
			bar.addElement(exitEditorButtonTab, "fm.editor.ui.tab.exit", ElementAlignment.RIGHT, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void render(MatrixStack matrix, Screen screen) {
		try {

			if (bar != null) {
				if (!PopupHandler.isPopupActive()) {
					if (screen instanceof LayoutEditorScreen) {

						bar.render(matrix, screen);

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void displayUnsavedWarning(Consumer<Boolean> callback) {
		PopupHandler.displayPopup(new FHYesNoPopup(300, new Color(0, 0, 0, 0), 240, callback, Locals.localize("fancyhud.helper.editor.ui.unsavedwarning")));
	}
	
	public void closeEditor() {
		this.displayUnsavedWarning((call) -> {
			if (call) {
				
				LayoutEditorScreen.isActive = false;
				
				CustomizationHandler.stopSounds();
				CustomizationHandler.resetSounds();
				CustomizationHandler.reloadSystem();

				Minecraft.getInstance().getMainWindow().setGuiScale(Minecraft.getInstance().getMainWindow().calcGuiScale(Minecraft.getInstance().gameSettings.guiScale, Minecraft.getInstance().getForceUnicodeFont()));
				this.parent.height = Minecraft.getInstance().getMainWindow().getScaledHeight();
				this.parent.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
				
				Minecraft.getInstance().displayGuiScreen(new CustomizationHelperScreen());
				
			}
		});
	}

	private static class OpenLayoutContextMenu extends FHContextMenu {

		private LayoutEditorUI ui;
		
		public OpenLayoutContextMenu(LayoutEditorUI ui) {
			this.ui = ui;
		}
		
		@Override
		public void openMenuAt(int x, int y, int screenWidth, int screenHeight) {
			
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
							AdvancedButton layoutEntryBtn = new AdvancedButton(0, 0, 0, 0, "§a" + name, (press) -> {
								this.ui.displayUnsavedWarning((call) -> {
									CustomizationHandler.editLayout(f);
								});
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
							AdvancedButton layoutEntryBtn = new AdvancedButton(0, 0, 0, 0, "§c" + name, (press) -> {
								this.ui.displayUnsavedWarning((call) -> {
									CustomizationHandler.editLayout(f);
								});
							});
							layoutEntryBtn.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.buttons.customization.managelayouts.layout.btndesc", Locals.localize("fancyhud.helper.buttons.customization.managelayouts.disabled"), "" + totalactions), "%n%"));
							this.addContent(layoutEntryBtn);
						}
					}
				}
			}
			
			if (enabled.isEmpty() && disabled.isEmpty()) {
				AdvancedButton emptyBtn = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.creator.empty"), (press) -> {});
				this.addContent(emptyBtn);
			}
			
			super.openMenuAt(x, y, screenWidth, screenHeight);
		}
		
	}
	
	public static class LayoutPropertiesContextMenu extends FHContextMenu {
		
		private LayoutEditorScreen parent;
		
		private AdvancedButton renderingOrderBackgroundButton;
		private AdvancedButton renderingOrderForegroundButton;
		
		private boolean isRightclickOpened;
		
		public LayoutPropertiesContextMenu(LayoutEditorScreen parent, boolean openedByRightclick) {
			this.parent = parent;
			this.isRightclickOpened = openedByRightclick;
		}
		
		@Override
		public void openMenuAt(int x, int y, int screenWidth, int screenHeight) {
			
			this.content.clear();
			
//			/** BACKGROUND OPTIONS **/
//			AdvancedButton backgroundOptionsButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.backgroundoptions"), true, (press) -> {
//				PopupHandler.displayPopup(new BackgroundOptionsPopup(this.parent));
//			});
//			this.addContent(backgroundOptionsButton);
//			
//			this.addSeparator();

			/** RENDERING ORDER **/
			FHContextMenu renderingOrderMenu = new FHContextMenu();
			renderingOrderMenu.setAutoclose(true);
			this.addChild(renderingOrderMenu);
			
			this.renderingOrderBackgroundButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder.background"), true, (press) -> {
				((AdvancedButton)press).setMessage("§a" + Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder.background"));
				this.renderingOrderForegroundButton.setMessage(Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder.foreground"));
				if (!this.parent.renderorder.equals("background")) {
					this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
				}
				
				this.parent.renderorder = "background";
			});
			renderingOrderMenu.addContent(renderingOrderBackgroundButton);
			
			this.renderingOrderForegroundButton = new AdvancedButton(0, 0, 0, 16, "§a" + Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder.foreground"), true, (press) -> {
				((AdvancedButton)press).setMessage("§a" + Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder.foreground"));
				this.renderingOrderBackgroundButton.setMessage(Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder.background"));
				if (!this.parent.renderorder.equals("foreground")) {
					this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
				}
				
				this.parent.renderorder = "foreground";
			});
			renderingOrderMenu.addContent(renderingOrderForegroundButton);
			
			if (this.parent.renderorder.equals("background")) {
				renderingOrderForegroundButton.setMessage(Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder.foreground"));
				renderingOrderBackgroundButton.setMessage("§a" + Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder.background"));
			}
			
			AdvancedButton renderingOrderButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.renderorder"), true, (press) -> {
				renderingOrderMenu.setParentButton((AdvancedButton) press);
				renderingOrderMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
			});
			renderingOrderButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.properties.renderingorder.desc"), "%n%"));
			this.addContent(renderingOrderButton);
			
			//TODO reimplementieren
//			/** WINDOW SIZE RESTRICTIONS **/
//			FHContextMenu windowSizeMenu = new FHContextMenu();
//			windowSizeMenu.setAutoclose(true);
//			this.addChild(windowSizeMenu);
//			
//			AdvancedButton biggerThanButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.creator.windowsize.biggerthan"), true, (press) -> {
//				PopupHandler.displayPopup(new WindowSizePopup(this.parent, ActionType.BIGGERTHAN));
//			});
//			windowSizeMenu.addContent(biggerThanButton);
//			
//			AdvancedButton smallerThanButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.creator.windowsize.smallerthan"), true, (press) -> {
//				PopupHandler.displayPopup(new WindowSizePopup(this.parent, ActionType.SMALLERTHAN));
//			});
//			windowSizeMenu.addContent(smallerThanButton);
//			
//			AdvancedButton windowSizeButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.windowsize"), true, (press) -> {
//				windowSizeMenu.setParentButton((AdvancedButton) press);
//				windowSizeMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
//			});
//			this.addContent(windowSizeButton);
			
			/** SHOW IN **/
			String showInLabel = Locals.localize("fancyhud.helper.editor.properties.showin.both");
			if (this.parent.showIn.equalsIgnoreCase("singleplayer")) {
				showInLabel = Locals.localize("fancyhud.helper.editor.properties.showin.singleplayer");
			}
			if (this.parent.showIn.equalsIgnoreCase("multiplayer")) {
				showInLabel = Locals.localize("fancyhud.helper.editor.properties.showin.multiplayer");
			}
			AdvancedButton showInButton = new AdvancedButton(0, 0, 0, 16, showInLabel, true, (press) -> {
				if (this.parent.showIn.equalsIgnoreCase("both")) {
					((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.properties.showin.singleplayer"));
					this.parent.showIn = "singleplayer";
				} else if (this.parent.showIn.equalsIgnoreCase("singleplayer")) {
					((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.properties.showin.multiplayer"));
					this.parent.showIn = "multiplayer";
				} else if (this.parent.showIn.equalsIgnoreCase("multiplayer")) {
					((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.properties.showin.both"));
					this.parent.showIn = "both";
				}
			});
			showInButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.properties.showin.btn.desc"), "%n%"));
			this.addContent(showInButton);
			
			/** REQUIRED MODS **/
			AdvancedButton requiredModsButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.requiredmods"), true, (press) -> {
				TextInputPopup p = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.layoutoptions.requiredmods.desc"), null, 240, (call) -> {
					if (call != null) {
						if (this.parent.requiredmods != call) {
							this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
						}
						
						this.parent.requiredmods = call;
					}
				});
				if (this.parent.requiredmods != null) {
					p.setText(this.parent.requiredmods);
				}
				PopupHandler.displayPopup(p);
			});
			this.addContent(requiredModsButton);
			
			/** MC VERSION **/
			FHContextMenu mcVersionMenu = new FHContextMenu();
			mcVersionMenu.setAutoclose(true);
			this.addChild(mcVersionMenu);
			
			AdvancedButton minMcVersionButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.version.minimum"), true, (press) -> {
				TextInputPopup p = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.layoutoptions.version.minimum.mc"), null, 240, (call) -> {
					if (call != null) {
						if (this.parent.minimumMC != call) {
							this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
						}
						
						this.parent.minimumMC = call;
					}
				});
				if (this.parent.minimumMC != null) {
					p.setText(this.parent.minimumMC);
				}
				PopupHandler.displayPopup(p);
			});
			mcVersionMenu.addContent(minMcVersionButton);
			
			AdvancedButton maxMcVersionButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.version.maximum"), true, (press) -> {
				TextInputPopup p = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.layoutoptions.version.maximum.mc"), null, 240, (call) -> {
					if (call != null) {
						if (this.parent.maximumMC != call) {
							this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
						}
						
						this.parent.maximumMC = call;
					}
				});
				if (this.parent.maximumMC != null) {
					p.setText(this.parent.maximumMC);
				}
				PopupHandler.displayPopup(p);
			});
			mcVersionMenu.addContent(maxMcVersionButton);
			
			AdvancedButton mcVersionButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.version.mc"), true, (press) -> {
				mcVersionMenu.setParentButton((AdvancedButton) press);
				mcVersionMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
			});
			this.addContent(mcVersionButton);
			
			/** FH VERSION **/
			FHContextMenu fmVersionMenu = new FHContextMenu();
			fmVersionMenu.setAutoclose(true);
			this.addChild(fmVersionMenu);
			
			AdvancedButton minFmVersionButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.version.minimum"), true, (press) -> {
				TextInputPopup p = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.layoutoptions.version.minimum.fh"), null, 240, (call) -> {
					if (call != null) {
						if (this.parent.minimumFH != call) {
							this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
						}
						
						this.parent.minimumFH = call;
					}
				});
				if (this.parent.minimumFH != null) {
					p.setText(this.parent.minimumFH);
				}
				PopupHandler.displayPopup(p);
			});
			fmVersionMenu.addContent(minFmVersionButton);
			
			AdvancedButton maxFmVersionButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.version.maximum"), true, (press) -> {
				TextInputPopup p = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.layoutoptions.version.maximum.fh"), null, 240, (call) -> {
					if (call != null) {
						if (this.parent.maximumFH != call) {
							this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
						}
						
						this.parent.maximumFH = call;
					}
				});
				if (this.parent.maximumFH != null) {
					p.setText(this.parent.maximumFH);
				}
				PopupHandler.displayPopup(p);
			});
			fmVersionMenu.addContent(maxFmVersionButton);
			
			AdvancedButton fmVersionButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.layoutoptions.version.fh"), true, (press) -> {
				fmVersionMenu.setParentButton((AdvancedButton) press);
				fmVersionMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
			});
			this.addContent(fmVersionButton);
			
			this.addSeparator();
			
			/** TOGGLE VIGNETTE **/
			String vigString = Locals.localize("fancyhud.helper.editor.properties.vignette.on");
			if (!this.parent.ingameHud.showVignette) {
				vigString = Locals.localize("fancyhud.helper.editor.properties.vignette.off");
			}
			AdvancedButton vignetteButton = new AdvancedButton(0, 0, 0, 16, vigString, (press) -> {
				if (this.parent.ingameHud.showVignette) {
					this.parent.ingameHud.showVignette = false;
					((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.properties.vignette.off"));
				} else {
					this.parent.ingameHud.showVignette = true;
					((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.properties.vignette.on"));
				}
			});
			this.addContent(vignetteButton);
			
			if (this.isRightclickOpened) {
				this.addSeparator();
			}
			
			/** PASTE **/
			AdvancedButton pasteButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.editor.ui.edit.paste"), (press) -> {
				this.parent.pasteElements();
			});
			if (this.isRightclickOpened) {
				this.addContent(pasteButton);
			}
			
			/** NEW ELEMENT **/
			NewElementContextMenu newElementMenu = new NewElementContextMenu(this.parent);
			newElementMenu.setAutoclose(true);
			this.addChild(newElementMenu);
			
			AdvancedButton newElementButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.editor.ui.layoutproperties.newelement"), (press) -> {
				newElementMenu.setParentButton((AdvancedButton) press);
				newElementMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
			});
			if (this.isRightclickOpened) {
				this.addContent(newElementButton);
			}
			
			
			super.openMenuAt(x, y, screenWidth, screenHeight);
		}
		
	}
	
	public static class NewElementContextMenu extends FHContextMenu {
		
		private LayoutEditorScreen parent;
		
		public NewElementContextMenu(LayoutEditorScreen parent) {
			this.parent = parent;
		}
		
		@Override
		public void openMenuAt(int x, int y, int screenWidth, int screenHeight) {
			
			this.content.clear();
			
			/** IMAGE **/
			AdvancedButton imageButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.image"), (press) -> {
				PopupHandler.displayPopup(new ChooseFilePopup(this.parent::addTexture, "jpg", "jpeg", "png", "gif"));
			});
			this.addContent(imageButton);

			/** WEB IMAGE **/
			AdvancedButton webImageButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.webimage"), (press) -> {
				PopupHandler.displayPopup(new DynamicValueInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.web.enterurl"), null, 240, this.parent::addWebTexture));
			});
			this.addContent(webImageButton);
			
			/** TEXT **/
			AdvancedButton textButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.text"), (press) -> {
				PopupHandler.displayPopup(new DynamicValueInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.add.text.newtext") + ":", null, 240, this.parent::addText));
			});
			this.addContent(textButton);
			
			/** WEB TEXT **/
			AdvancedButton webTextButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.webtext"), (press) -> {
				PopupHandler.displayPopup(new DynamicValueInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.web.enterurl"), null, 240, this.parent::addWebText));
			});
			this.addContent(webTextButton);
			
			/** SPLASH TEXT **/
			FHContextMenu splashMenu = new FHContextMenu();
			splashMenu.setAutoclose(true);
			this.addChild(splashMenu);
			
			AdvancedButton singleSplashButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.creator.add.splash.single"), true, (press) -> {
				PopupHandler.displayPopup(new DynamicValueInputPopup(new Color(0, 0, 0, 0), Locals.localize("fancyhud.helper.creator.add.splash.single.desc"), null, 240, this.parent::addSingleSplashText));
			});
			singleSplashButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.creator.add.splash.single.desc"), "%n%"));
			splashMenu.addContent(singleSplashButton);
			
			AdvancedButton multiSplashButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.creator.add.splash.multi"), true, (press) -> {
				PopupHandler.displayPopup(new ChooseFilePopup(this.parent::addMultiSplashText, "txt"));
			});
			multiSplashButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.creator.add.splash.multi.desc"), "%n%"));
			splashMenu.addContent(multiSplashButton);
			
			AdvancedButton splashButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.splash"), (press) -> {
				splashMenu.setParentButton((AdvancedButton) press);
				splashMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
			});
			this.addContent(splashButton);

//			/** PLAYER ENTITY **/
//			AdvancedButton playerEntityButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.playerentity"), (press) -> {
//				this.parent.addPlayerEntity();
//			});
//			this.addContent(playerEntityButton);

			/** SLIDESHOW **/
			FHContextMenu slideshowMenu = new FHContextMenu();
			slideshowMenu.setAutoclose(true);
			this.addChild(slideshowMenu);

			AdvancedButton inputSlideshowButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.slideshow.entername"), true, (press) -> {
				PopupHandler.displayPopup(new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("fancyhud.helper.creator.add.slideshow.entername.title") + ":", null, 240, this.parent::addSlideshow));
			});
			slideshowMenu.addContent(inputSlideshowButton);
			
			slideshowMenu.addSeparator();
			
			for (String s : SlideshowHandler.getSlideshowNames()) {
				String name = s;
				if (Minecraft.getInstance().fontRenderer.getStringWidth(name) > 90) {
					name = Minecraft.getInstance().fontRenderer.trimStringToWidth(name, 90) + "..";
				}
				
				AdvancedButton slideshowB = new AdvancedButton(0, 0, 0, 20, name, true, (press) -> {
					if (SlideshowHandler.slideshowExists(s)) {
						this.parent.addSlideshow(s);
					}
				});
				slideshowMenu.addContent(slideshowB);
			}

			AdvancedButton slideshowButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.slideshow"), (press) -> {
				slideshowMenu.setParentButton((AdvancedButton) press);
				slideshowMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
			});
			this.addContent(slideshowButton);

			/** SHAPE **/
			FHContextMenu shapesMenu = new FHContextMenu();
			shapesMenu.setAutoclose(true);
			this.addChild(shapesMenu);

			AdvancedButton addRectangleButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.shapes.rectangle"), (press) -> {
				this.parent.addShape(Shape.RECTANGLE);
			});
			shapesMenu.addContent(addRectangleButton);
			
			AdvancedButton shapesButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.shapes"), (press) -> {
				shapesMenu.setParentButton((AdvancedButton) press);
				shapesMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
			});
			this.addContent(shapesButton);
			
			this.addSeparator();
			
			/** CUSTOM HEALTH BAR **/
			AdvancedButton customHealthButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.customhealthbar"), (press) -> {
				this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
				
				PropertiesSection sec = new PropertiesSection("customization");
				sec.addEntry("action", "addcustomhealthbar");
				sec.addEntry("width", "100");
				sec.addEntry("height", "20");
				sec.addEntry("x", "0");
				sec.addEntry("y", "" + (int)(this.parent.ui.bar.getHeight() * UIBase.getUIScale()));
				
				CustomHealthBarCustomizationItem i = new CustomHealthBarCustomizationItem(sec);
				this.parent.addContent(new LayoutCustomHealthBar(i, this.parent));
			});
			this.addContent(customHealthButton);
			
			/** CUSTOM FOOD BAR **/
			AdvancedButton customFoodButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.customfoodbar"), (press) -> {
				this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
				
				PropertiesSection sec = new PropertiesSection("customization");
				sec.addEntry("action", "addcustomfoodbar");
				sec.addEntry("width", "100");
				sec.addEntry("height", "20");
				sec.addEntry("x", "0");
				sec.addEntry("y", "" + (int)(this.parent.ui.bar.getHeight() * UIBase.getUIScale()));
				
				CustomFoodBarCustomizationItem i = new CustomFoodBarCustomizationItem(sec);
				this.parent.addContent(new LayoutCustomFoodBar(i, this.parent));
			});
			this.addContent(customFoodButton);
			
			/** CUSTOM ARMOR BAR **/
			AdvancedButton customArmorButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.customarmorbar"), (press) -> {
				this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
				
				PropertiesSection sec = new PropertiesSection("customization");
				sec.addEntry("action", "addcustomarmorbar");
				sec.addEntry("width", "100");
				sec.addEntry("height", "20");
				sec.addEntry("x", "0");
				sec.addEntry("y", "" + (int)(this.parent.ui.bar.getHeight() * UIBase.getUIScale()));
				
				CustomArmorBarCustomizationItem i = new CustomArmorBarCustomizationItem(sec);
				this.parent.addContent(new LayoutCustomArmorBar(i, this.parent));
			});
			this.addContent(customArmorButton);
			
			/** CUSTOM AIR BAR **/
			AdvancedButton customAirButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.customairbar"), (press) -> {
				this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
				
				PropertiesSection sec = new PropertiesSection("customization");
				sec.addEntry("action", "addcustomairbar");
				sec.addEntry("width", "100");
				sec.addEntry("height", "20");
				sec.addEntry("x", "0");
				sec.addEntry("y", "" + (int)(this.parent.ui.bar.getHeight() * UIBase.getUIScale()));
				
				CustomAirBarCustomizationItem i = new CustomAirBarCustomizationItem(sec);
				this.parent.addContent(new LayoutCustomAirBar(i, this.parent));
			});
			this.addContent(customAirButton);
			
			/** CUSTOM EXP BAR **/
			AdvancedButton customExpButton = new AdvancedButton(0, 0, 0, 20, Locals.localize("fancyhud.helper.creator.add.customexpbar"), (press) -> {
				this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
				
				PropertiesSection sec = new PropertiesSection("customization");
				sec.addEntry("action", "addcustomexpbar");
				sec.addEntry("width", "100");
				sec.addEntry("height", "20");
				sec.addEntry("x", "0");
				sec.addEntry("y", "" + (int)(this.parent.ui.bar.getHeight() * UIBase.getUIScale()));
				
				CustomExpBarCustomizationItem i = new CustomExpBarCustomizationItem(sec);
				this.parent.addContent(new LayoutCustomExpBar(i, this.parent));
			});
			this.addContent(customExpButton);
			
			this.addSeparator();
			
			/** CUSTOM ITEMS **/
			for (CustomizationItemContainer c : CustomizationItemRegistry.getInstance().getElements().values()) {
				
				AdvancedButton newCustomItemButton = new AdvancedButton(0, 0, 0, 20, c.displayName, (press) -> {
					this.parent.addCustomItem(c);
				});
				this.addContent(newCustomItemButton);
				
			}
			
			super.openMenuAt(x, y, screenWidth, screenHeight);
		}
		
	}

	public static class MultiselectContextMenu extends FHContextMenu {

		private LayoutEditorScreen parent;

		public MultiselectContextMenu(LayoutEditorScreen parent) {
			this.parent = parent;
		}
		
		@Override
		public void openMenuAt(int x, int y, int screenWidth, int screenHeight) {
			
			this.content.clear();
			
			if (this.parent.isObjectFocused()) {

				this.parent.focusedObjectsCache = this.parent.getFocusedObjects();
				
				this.parent.multiselectStretchedX = false;
				this.parent.multiselectStretchedY = false;
				
				/** DELETE ALL **/
				AdvancedButton deleteBtn = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.multiselect.object.deleteall"), true, (press) -> {
					this.parent.deleteFocusedObjects();
				});
				deleteBtn.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.creator.multiselect.object.deleteall.btndesc"), "%n%"));
				this.addContent(deleteBtn);
				
				/** STRETCH ALL **/
				FHContextMenu stretchMenu = new FHContextMenu();
				stretchMenu.setAutoclose(true);
				this.addChild(stretchMenu);

				AdvancedButton stretchXBtn = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.object.stretch.x"), true, (press) -> {
					this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
					
					for (LayoutElement o : this.parent.focusedObjectsCache) {
						if (o.isStretchable()) {
							o.setStretchedX(!this.parent.multiselectStretchedX, false);
						}
					}
					
					this.parent.multiselectStretchedX = !this.parent.multiselectStretchedX;
					
					if (!this.parent.multiselectStretchedX) {
						press.setMessage(new StringTextComponent(Locals.localize("fancyhud.helper.creator.object.stretch.x")));
					} else {
						press.setMessage(new StringTextComponent("§a" + Locals.localize("fancyhud.helper.creator.object.stretch.x")));
					}

				});
				stretchMenu.addContent(stretchXBtn);
				
				AdvancedButton stretchYBtn = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.object.stretch.y"), true, (press) -> {
					this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
					
					for (LayoutElement o : this.parent.focusedObjectsCache) {
						if (o.isStretchable()) {
							o.setStretchedY(!this.parent.multiselectStretchedY, false);
						}
					}
					
					this.parent.multiselectStretchedY = !this.parent.multiselectStretchedY;
					
					if (!this.parent.multiselectStretchedY) {
						press.setMessage(new StringTextComponent(Locals.localize("fancyhud.helper.creator.object.stretch.y")));
					} else {
						press.setMessage(new StringTextComponent("§a" + Locals.localize("fancyhud.helper.creator.object.stretch.y")));
					}
					
				});
				stretchMenu.addContent(stretchYBtn);
				
				AdvancedButton stretchBtn = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.multiselect.object.stretchall"), true, (press) -> {
					stretchMenu.setParentButton((AdvancedButton) press);
					stretchMenu.openMenuAt(0, press.y, screenWidth, screenHeight);
				});
				stretchBtn.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.creator.multiselect.object.stretchall.btndesc"), "%n%"));
				this.addContent(stretchBtn);
				
				/** COPY **/
				AdvancedButton copyButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.editor.ui.edit.copy"), (press) -> {
					this.parent.copySelectedElements();
				});
				this.addContent(copyButton);
				
				/** PASTE **/
				AdvancedButton pasteButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.editor.ui.edit.paste"), (press) -> {
					this.parent.pasteElements();
				});
				this.addContent(pasteButton);
				
			}
			
			
			super.openMenuAt(x, y, screenWidth, screenHeight);
		}

	}
	
	public static class HiddenVanillaElementsContextMenu extends FHContextMenu {

		private LayoutEditorScreen parent;

		public HiddenVanillaElementsContextMenu(LayoutEditorScreen parent) {
			this.parent = parent;
		}
		
		@Override
		public void openMenuAt(int x, int y, int screenWidth, int screenHeight) {
			
			this.content.clear();
			this.separators.clear();
			
			List<VanillaLayoutElement> hidden = new ArrayList<VanillaLayoutElement>();
			
			for (LayoutElement e : this.parent.content) {
				if (e instanceof VanillaLayoutElement) {
					if (!((VanillaLayoutElement) e).getVanillaObject().vanillaVisible) {
						hidden.add((VanillaLayoutElement) e);
					}
				}
			}
			
			if (hidden.isEmpty()) {
				AdvancedButton emptyButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.empty"), true, (press) -> {});
				this.addContent(emptyButton);
			} else {
				for (VanillaLayoutElement e : hidden) {
					
					String name = e.object.value;
					
					AdvancedButton hiddenButton = new AdvancedButton(0, 0, 0, 0, name, true, (press) -> {
						this.parent.history.saveSnapshot(this.parent.history.createSnapshot());
						e.getVanillaObject().vanillaVisible = true;
						e.getVanillaObject().element.visible = true;
						this.closeMenu();
					});
					hiddenButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.managedeleted.entry.desc"), "%n%"));
					this.addContent(hiddenButton);
					
				}
			}
			
			super.openMenuAt(x, y, screenWidth, screenHeight);
		}

	}

}
