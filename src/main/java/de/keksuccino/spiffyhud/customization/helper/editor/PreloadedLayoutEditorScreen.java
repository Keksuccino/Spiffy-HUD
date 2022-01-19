package de.keksuccino.spiffyhud.customization.helper.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.keksuccino.spiffyhud.api.hud.CustomVanillaCustomizationItem;
import de.keksuccino.spiffyhud.api.hud.CustomVanillaLayoutElement;
import de.keksuccino.spiffyhud.api.hud.HudElementContainer;
import de.keksuccino.spiffyhud.api.hud.HudElementRegistry;
import de.keksuccino.spiffyhud.api.hud.v2.SimpleVanillaCustomizationItem;
import de.keksuccino.spiffyhud.api.hud.v2.VanillaHudElementContainer;
import de.keksuccino.spiffyhud.api.hud.v2.VanillaHudElementRegistry;
import de.keksuccino.spiffyhud.api.hud.v2.VanillaLayoutEditorElement;
import de.keksuccino.spiffyhud.api.item.CustomizationItem;
import de.keksuccino.spiffyhud.api.item.CustomizationItemContainer;
import de.keksuccino.spiffyhud.api.item.CustomizationItemLayoutElement;
import de.keksuccino.spiffyhud.api.item.CustomizationItemRegistry;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.*;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.custombars.*;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.string.LayoutString;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.string.LayoutWebString;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.AirBarLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.ArmorBarLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.BossBarLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.CrosshairLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.ExperienceJumpBarLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.FoodMountHealthLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.HotbarLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.OverlayMessageLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.PlayerHealthBarLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.SidebarLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.SelectedItemNameLayoutElement;
import de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla.TitleLayoutElement;
import de.keksuccino.spiffyhud.customization.items.*;
import de.keksuccino.spiffyhud.customization.items.ShapeCustomizationItem.Shape;
import de.keksuccino.spiffyhud.customization.items.custombars.*;
import de.keksuccino.spiffyhud.customization.items.vanilla.AirBarCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.ArmorBarCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.BossBarCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.CrosshairCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.ExperienceJumpBarCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.FoodMountHealthCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.HotbarCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.OverlayMessageCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.PlayerHealthBarCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.SidebarCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.SelectedItemNameCustomizationItem;
import de.keksuccino.spiffyhud.customization.items.vanilla.TitleCustomizationItem;
import de.keksuccino.spiffyhud.customization.rendering.slideshow.SlideshowHandler;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.properties.PropertiesSet;

public class PreloadedLayoutEditorScreen extends LayoutEditorScreen {

	public String single;
	
	public PreloadedLayoutEditorScreen(PropertiesSet properties) {
		super();
		
		this.customElements.clear();
		
		List<LayoutElement> con = new ArrayList<LayoutElement>();
		List<LayoutElement> vanillaCon = new ArrayList<LayoutElement>();

		List<PropertiesSection> l = properties.getPropertiesOfType("customization-meta");
		if (!l.isEmpty()) {
			PropertiesSection meta = l.get(0);
			
			this.requiredmods = meta.getEntryValue("requiredmods");
			this.minimumFH = meta.getEntryValue("minimumfhversion");
			this.maximumFH = meta.getEntryValue("maximumfhversion");
			this.minimumMC = meta.getEntryValue("minimummcversion");
			this.maximumMC = meta.getEntryValue("maximummcversion");
			
			String order = meta.getEntryValue("renderorder");
			if ((order != null) && order.equalsIgnoreCase("background")) {
				this.renderorder = "background";
			}
			
			String vig = meta.getEntryValue("showvignette");
			if ((vig != null) && vig.equalsIgnoreCase("false")) {
				this.ingameHud.showVignette = false;
			}
			
			String showInString = meta.getEntryValue("showin");
			if (showInString != null) {
				if (showInString.equals("multiplayer") || showInString.equals("singleplayer")) {
					this.showIn = showInString;
				}
			}

			String biggerthanwidth = meta.getEntryValue("biggerthanwidth");
			if (biggerthanwidth != null) {
				biggerthanwidth = biggerthanwidth.replace(" ", "");
				if (MathUtils.isInteger(biggerthanwidth)) {
					int i = Integer.parseInt(biggerthanwidth);
					this.biggerThanWidth = i;
				}
			}

			String biggerthanheight = meta.getEntryValue("biggerthanheight");
			if (biggerthanheight != null) {
				biggerthanheight = biggerthanheight.replace(" ", "");
				if (MathUtils.isInteger(biggerthanheight)) {
					int i = Integer.parseInt(biggerthanheight);
					this.biggerThanHeight = i;
				}
			}

			String smallerthanwidth = meta.getEntryValue("smallerthanwidth");
			if (smallerthanwidth != null) {
				smallerthanwidth = smallerthanwidth.replace(" ", "");
				if (MathUtils.isInteger(smallerthanwidth)) {
					int i = Integer.parseInt(smallerthanwidth);
					this.smallerThanWidth = i;
				}
			}

			String smallerthanheight = meta.getEntryValue("smallerthanheight");
			if (smallerthanheight != null) {
				smallerthanheight = smallerthanheight.replace(" ", "");
				if (MathUtils.isInteger(smallerthanheight)) {
					int i = Integer.parseInt(smallerthanheight);
					this.smallerThanHeight = i;
				}
			}
			
			this.single = meta.getEntryValue("path");
		}
		
		boolean crossSet = false;
//		boolean potionsSet = false;
		boolean bossSet = false;
		boolean hotbarSet = false;
		boolean playerHealthSet = false;
		boolean foodSet = false;
		boolean experienceSet = false;
		boolean titleSet = false;
		boolean subtitleSet = false;
		boolean armorSet = false;
		boolean airSet = false;
		boolean selectedItemNameSet = false;
		boolean overlayMessageSet = false;
		boolean sidebarSet = false;
		
		List<String> customElementsSet = new ArrayList<String>();
		
		Map<String, HudElementContainer> customElementContainers = HudElementRegistry.getInstance().getElements();

		for (PropertiesSection sec : properties.getPropertiesOfType("customization")) {
			String action = sec.getEntryValue("action");
			if (action != null) {
				
				/** ########################### VANILLA ELEMENT HANDLING ########################### **/
				
				/** CROSSHAIR **/
				if (action.equalsIgnoreCase("editcrosshair")) {
					this.crosshairLayoutElement = new CrosshairLayoutElement(new CrosshairCustomizationItem(this.ingameHud.crosshairElement, sec, false), this);
					vanillaCon.add(this.crosshairLayoutElement);
					crossSet = true;
				}
				
				/** BOSS HEALTH **/
				if (action.equalsIgnoreCase("editbosshealth")) {
					this.bossBarLayoutElement = new BossBarLayoutElement(new BossBarCustomizationItem(this.ingameHud.bossBarElement, sec, false), this);
					vanillaCon.add(this.bossBarLayoutElement);
					bossSet = true;
				}
				
				/** HOTBAR **/
				if (action.equalsIgnoreCase("edithotbar")) {
					this.hotbarLayoutElement = new HotbarLayoutElement(new HotbarCustomizationItem(this.ingameHud.hotbarElement, sec, false), this);
					vanillaCon.add(this.hotbarLayoutElement);
					hotbarSet = true;
				}
				
				/** PLAYER HEALTH BAR **/
				if (action.equalsIgnoreCase("editplayerhealthbar")) {
					this.playerHealthBarLayoutElement = new PlayerHealthBarLayoutElement(new PlayerHealthBarCustomizationItem(this.ingameHud.healthElement, sec, false), this);
					vanillaCon.add(this.playerHealthBarLayoutElement);
					playerHealthSet = true;
				}
				
				/** PLAYER FOOD BAR | MOUNT HEALTH BAR **/
				if (action.equalsIgnoreCase("editplayerfoodbar")) {
					this.foodBarLayoutElement = new FoodMountHealthLayoutElement(new FoodMountHealthCustomizationItem(this.ingameHud.foodMountHealthElement, sec, false), this);
					vanillaCon.add(this.foodBarLayoutElement);
					foodSet = true;
				}
				
				/** EXPERIENCE BAR | JUMP BAR **/
				if (action.equalsIgnoreCase("editexperiencebar")) {
					this.experienceBarLayoutElement = new ExperienceJumpBarLayoutElement(new ExperienceJumpBarCustomizationItem(this.ingameHud.experienceJumpBarElement, sec, false), this);
					vanillaCon.add(this.experienceBarLayoutElement);
					experienceSet = true;
				}
				
				/** TITLE **/
				if (action.equalsIgnoreCase("edittitle")) {
					this.titleLayoutElement = new TitleLayoutElement(new TitleCustomizationItem(this.ingameHud.titleElement, sec, false), this);
					vanillaCon.add(this.titleLayoutElement);
					titleSet = true;
				}
				
				/** SUBTITLE **/
				if (action.equalsIgnoreCase("editsubtitle")) {
					this.subtitleLayoutElement = new TitleLayoutElement(new TitleCustomizationItem(this.ingameHud.subtitleElement, sec, false), this);
					vanillaCon.add(this.subtitleLayoutElement);
					subtitleSet = true;
				}

				/** ARMOR BAR **/
				if (action.equalsIgnoreCase("editarmorbar")) {
					this.armorLayoutElement = new ArmorBarLayoutElement(new ArmorBarCustomizationItem(this.ingameHud.armorBarElement, sec, false), this);
					vanillaCon.add(this.armorLayoutElement);
					armorSet = true;
				}

				/** AIR BAR **/
				if (action.equalsIgnoreCase("editairbar")) {
					this.airLayoutElement = new AirBarLayoutElement(new AirBarCustomizationItem(this.ingameHud.airBarElement, sec, false), this);
					vanillaCon.add(this.airLayoutElement);
					airSet = true;
				}
				
				/** SELECTED ITEM NAME **/
				if (action.equalsIgnoreCase("editselecteditemname")) {
					this.selectedItemNameLayoutElement = new SelectedItemNameLayoutElement(new SelectedItemNameCustomizationItem(this.ingameHud.selectedItemNameElement, sec, false), this);
					vanillaCon.add(this.selectedItemNameLayoutElement);
					selectedItemNameSet = true;
				}
				
				/** OVERLAY MESSAGE **/
				if (action.equalsIgnoreCase("editoverlaymessage")) {
					this.overlayMessageLayoutElement = new OverlayMessageLayoutElement(new OverlayMessageCustomizationItem(this.ingameHud.overlayMessageElement, sec, false), this);
					vanillaCon.add(this.overlayMessageLayoutElement);
					overlayMessageSet = true;
				}
				
				/** SIDEBAR **/
				if (action.equalsIgnoreCase("editsidebar")) {
					this.sidebarLayoutElement = new SidebarLayoutElement(new SidebarCustomizationItem(this.ingameHud.sidebarElement, sec, false), this);
					vanillaCon.add(this.sidebarLayoutElement);
					sidebarSet = true;
				}
				
				/** ########################### CUSTOM VANILLA ELEMENT HANDLING ########################### **/

				//Deprecated (old API)
				if (action.startsWith("edit_")) {

					String id = action.split("[_]", 2)[1];
					HudElementContainer c = customElementContainers.get(id);

					if (c != null) {

						CustomVanillaLayoutElement e = new CustomVanillaLayoutElement(c, new CustomVanillaCustomizationItem(c, sec, false), this);
						this.customElements.put(id, e);
						vanillaCon.add(e);

						if (!customElementsSet.contains(id)) {
							customElementsSet.add(id);
						}

					}

				}

				//Custom vanilla HUD element handling (new API)
				if (action.startsWith("custom_vanilla_layout_element:")) {

					String identifier = action.split("[:]", 2)[1];
					VanillaHudElementContainer c = VanillaHudElementRegistry.getElement(identifier);

					if (c != null) {

						VanillaLayoutEditorElement e = new VanillaLayoutEditorElement(c, new SimpleVanillaCustomizationItem(c, sec, false), this);
						vanillaCon.add(e);

						if (!customElementsSet.contains(identifier)) {
							customElementsSet.add(identifier);
						}

					}

				}
				
				/** ########################### ITEM HANDLING ########################### **/
				
				if (action.equalsIgnoreCase("addtext")) {
					con.add(new LayoutString(new StringCustomizationItem(sec), this));
				}

				if (action.equalsIgnoreCase("addwebtext")) {
					con.add(new LayoutWebString(new WebStringCustomizationItem(sec), this));
				}

				if (action.equalsIgnoreCase("addtexture")) {
					LayoutTexture o = new LayoutTexture(new TextureCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}

				if (action.equalsIgnoreCase("addwebtexture")) {
					LayoutWebTexture o = new LayoutWebTexture(new WebTextureCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}

//				if (action.equalsIgnoreCase("addentity")) {
//					LayoutPlayerEntity o = new LayoutPlayerEntity(new PlayerEntityCustomizationItem(sec), this);
//
//					String playername = sec.getEntryValue("playername");
//					if ((playername != null) && (playername.replace(" ", "").equals("%playername%"))) {
//						o.isCLientPlayerName = true;
//					}
//
//					String capePath = sec.getEntryValue("capepath");
//					if (capePath != null) {
//						o.capePath = capePath;
//					}
//
//					String capeUrl = sec.getEntryValue("capeurl");
//					if (capeUrl != null) {
//						o.capeUrl = capeUrl;
//					}
//
//					String skinPath = sec.getEntryValue("skinpath");
//					if (skinPath != null) {
//						o.skinPath = skinPath;
//					}
//
//					String skinUrl = sec.getEntryValue("skinurl");
//					if (skinUrl != null) {
//						o.skinUrl = skinUrl;
//					}
//
//					con.add(o);
//				}

				if (action.equalsIgnoreCase("addslideshow")) {
					String name = sec.getEntryValue("name");
					if (name != null) {
						if (SlideshowHandler.slideshowExists(name)) {
							LayoutSlideshow ls = new LayoutSlideshow(new SlideshowCustomizationItem(sec), this);
							int i = isObjectStretched(sec);
							if (i == 3) {
								ls.setStretchedX(true, false);
								ls.setStretchedY(true, false);
							}
							if (i == 2) {
								ls.setStretchedY(true, false);
							}
							if (i == 1) {
								ls.setStretchedX(true, false);
							}
							con.add(ls);
						}
					}
				}

				if (action.equalsIgnoreCase("addshape")) {
					String shape = sec.getEntryValue("shape");
					if (shape != null) {
						Shape sh = Shape.byName(shape);
						if (sh != null) {
							LayoutShape ls = new LayoutShape(new ShapeCustomizationItem(sec), this);
							int i = isObjectStretched(sec);
							if (i == 3) {
								ls.setStretchedX(true, false);
								ls.setStretchedY(true, false);
							}
							if (i == 2) {
								ls.setStretchedY(true, false);
							}
							if (i == 1) {
								ls.setStretchedX(true, false);
							}
							con.add(ls);
						}
					}
				}

				if (action.equalsIgnoreCase("additemstack")) {
					con.add(new LayoutItemStack(new ItemStackCustomizationItem(sec), this));
				}

				if (action.equalsIgnoreCase("addmirroredplayer")) {
					con.add(new LayoutMirroredPlayer(new MirroredPlayerCustomizationItem(sec), this));
				}

				if (action.equalsIgnoreCase("addsplash")) {
					con.add(new LayoutSplashText(new SplashTextCustomizationItem(sec), this));
				}
				
				if (action.equalsIgnoreCase("addcustomhealthbar")) {
					LayoutCustomHealthBar o = new LayoutCustomHealthBar(new CustomHealthBarCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}
				
				if (action.equalsIgnoreCase("addcustomfoodbar")) {
					LayoutCustomFoodBar o = new LayoutCustomFoodBar(new CustomFoodBarCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}
				
				if (action.equalsIgnoreCase("addcustomarmorbar")) {
					LayoutCustomArmorBar o = new LayoutCustomArmorBar(new CustomArmorBarCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}
				
				if (action.equalsIgnoreCase("addcustomairbar")) {
					LayoutCustomAirBar o = new LayoutCustomAirBar(new CustomAirBarCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}
				
				if (action.equalsIgnoreCase("addcustomexpbar")) {
					LayoutCustomExpBar o = new LayoutCustomExpBar(new CustomExpBarCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}

				if (action.equalsIgnoreCase("addcustommounthealthbar")) {
					LayoutCustomMountHealthBar o = new LayoutCustomMountHealthBar(new CustomMountHealthBarCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}

				if (action.equalsIgnoreCase("addcustommountjumpbar")) {
					LayoutCustomMountJumpBar o = new LayoutCustomMountJumpBar(new CustomMountJumpBarCustomizationItem(sec), this);
					int i = isObjectStretched(sec);
					if (i == 3) {
						o.setStretchedX(true, false);
						o.setStretchedY(true, false);
					}
					if (i == 2) {
						o.setStretchedY(true, false);
					}
					if (i == 1) {
						o.setStretchedX(true, false);
					}
					con.add(o);
				}
				
				/** ########################### CUSTOM ITEM HANDLING ########################### **/

				//DEPRECATED (OLD API)
				if (action.startsWith("add_")) {
					String id = action.split("[_]", 2)[1];
					CustomizationItemContainer c = CustomizationItemRegistry.getInstance().getElement(id);

					if (c != null) {

						CustomizationItem i = c.constructWithProperties(sec);
						con.add(new CustomizationItemLayoutElement(c, i, this));

					}
				}

				//NEW API
				/** CUSTOM ITEMS (API) **/
				if (action.startsWith("custom_layout_element:")) {
					String cusId = action.split("[:]", 2)[1];
					de.keksuccino.spiffyhud.api.item.v2.CustomizationItemContainer cusItem = de.keksuccino.spiffyhud.api.item.v2.CustomizationItemRegistry.getItem(cusId);
					if (cusItem != null) {
						de.keksuccino.spiffyhud.api.item.v2.CustomizationItem cusItemInstance = cusItem.constructCustomizedItemInstance(sec);
						con.add(cusItem.constructEditorElementInstance(cusItemInstance, this));
					}
				}

			}
		}
		
		PropertiesSection dummySec = new PropertiesSection("customization");
		if (!crossSet) {
			this.crosshairLayoutElement = new CrosshairLayoutElement(new CrosshairCustomizationItem(this.ingameHud.crosshairElement, dummySec, false), this);
			vanillaCon.add(this.crosshairLayoutElement);
		}
		if (!bossSet) {
			this.bossBarLayoutElement = new BossBarLayoutElement(new BossBarCustomizationItem(this.ingameHud.bossBarElement, dummySec, false), this);
			vanillaCon.add(this.bossBarLayoutElement);
		}
		if (!hotbarSet) {
			this.hotbarLayoutElement = new HotbarLayoutElement(new HotbarCustomizationItem(this.ingameHud.hotbarElement, dummySec, false), this);
			vanillaCon.add(this.hotbarLayoutElement);
		}
		if (!playerHealthSet) {
			this.playerHealthBarLayoutElement = new PlayerHealthBarLayoutElement(new PlayerHealthBarCustomizationItem(this.ingameHud.healthElement, dummySec, false), this);
			vanillaCon.add(this.playerHealthBarLayoutElement);
		}
		if (!foodSet) {
			this.foodBarLayoutElement = new FoodMountHealthLayoutElement(new FoodMountHealthCustomizationItem(this.ingameHud.foodMountHealthElement, dummySec, false), this);
			vanillaCon.add(this.foodBarLayoutElement);
		}
		if (!experienceSet) {
			this.experienceBarLayoutElement = new ExperienceJumpBarLayoutElement(new ExperienceJumpBarCustomizationItem(this.ingameHud.experienceJumpBarElement, dummySec, false), this);
			vanillaCon.add(this.experienceBarLayoutElement);
		}
		if (!titleSet) {
			this.titleLayoutElement = new TitleLayoutElement(new TitleCustomizationItem(this.ingameHud.titleElement, dummySec, false), this);
			vanillaCon.add(this.titleLayoutElement);
		}
		if (!subtitleSet) {
			this.subtitleLayoutElement = new TitleLayoutElement(new TitleCustomizationItem(this.ingameHud.subtitleElement, dummySec, false), this);
			vanillaCon.add(this.subtitleLayoutElement);
		}
		if (!armorSet) {
			this.armorLayoutElement = new ArmorBarLayoutElement(new ArmorBarCustomizationItem(this.ingameHud.armorBarElement, dummySec, false), this);
			vanillaCon.add(this.armorLayoutElement);
		}
		if (!airSet) {
			this.airLayoutElement = new AirBarLayoutElement(new AirBarCustomizationItem(this.ingameHud.airBarElement, dummySec, false), this);
			vanillaCon.add(this.airLayoutElement);
		}
		if (!selectedItemNameSet) {
			this.selectedItemNameLayoutElement = new SelectedItemNameLayoutElement(new SelectedItemNameCustomizationItem(this.ingameHud.selectedItemNameElement, dummySec, false), this);
			vanillaCon.add(this.selectedItemNameLayoutElement);
		}
		if (!overlayMessageSet) {
			this.overlayMessageLayoutElement = new OverlayMessageLayoutElement(new OverlayMessageCustomizationItem(this.ingameHud.overlayMessageElement, dummySec, false), this);
			vanillaCon.add(this.overlayMessageLayoutElement);
		}
		if (!sidebarSet) {
			this.sidebarLayoutElement = new SidebarLayoutElement(new SidebarCustomizationItem(this.ingameHud.sidebarElement, dummySec, false), this);
			vanillaCon.add(this.sidebarLayoutElement);
		}

		//Deprecated (old API)
		for (Map.Entry<String, HudElementContainer> m : customElementContainers.entrySet()) {
			HudElementContainer c = m.getValue();
			String id = m.getKey();
			if (!customElementsSet.contains(c.elementIdentifier)) {

				CustomVanillaLayoutElement e = new CustomVanillaLayoutElement(c, new CustomVanillaCustomizationItem(c, dummySec, false), this);
				this.customElements.put(id, e);
				vanillaCon.add(e);

			}
		}

		//Custom vanilla HUD element handling (new API)
		for (VanillaHudElementContainer c : VanillaHudElementRegistry.getElements()) {
			if (!customElementsSet.contains(c.getIdentifier())) {

				VanillaLayoutEditorElement e = new VanillaLayoutEditorElement(c, new SimpleVanillaCustomizationItem(c, dummySec, false), this);
				vanillaCon.add(e);

			}
		}

		this.content.clear();
		this.content.addAll(vanillaCon);
		this.content.addAll(con);
		
	}

	/**
	 * Returns:<br>
	 * 0 for FALSE<br>
	 * 1 for HORIZONTALLY STRETCHED<br>
	 * 2 for VERTICALLY STRETCHED<br>
	 * 3 for BOTH
	 */
	public static int isObjectStretched(PropertiesSection sec) {
		String w = sec.getEntryValue("width");
		String h = sec.getEntryValue("height");
		String x = sec.getEntryValue("x");
		String y = sec.getEntryValue("y");
		
		boolean stretchX = false;
		if ((w != null) && (x != null)) {
			if (w.equals("%guiwidth%") && x.equals("0")) {
				stretchX = true;
			}
		}
		boolean stretchY = false;
		if ((h != null) && (y != null)) {
			if (h.equals("%guiheight%") && y.equals("0")) {
				stretchY = true;
			}
		}
		
		if (stretchX && stretchY) {
			return 3;
		}
		if (stretchY) {
			return 2;
		}
		if (stretchX) {
			return 1;
		}
		
		return 0;
	}

}
