package de.keksuccino.spiffyhud.customization.rendering.ingamehud;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.api.hud.CustomVanillaCustomizationItem;
import de.keksuccino.spiffyhud.api.hud.HudElementContainer;
import de.keksuccino.spiffyhud.api.hud.HudElementRegistry;
import de.keksuccino.spiffyhud.api.hud.v2.SimpleVanillaCustomizationItem;
import de.keksuccino.spiffyhud.api.hud.v2.VanillaHudElementContainer;
import de.keksuccino.spiffyhud.api.hud.v2.VanillaHudElementRegistry;
import de.keksuccino.spiffyhud.api.item.CustomizationItem;
import de.keksuccino.spiffyhud.api.item.CustomizationItemContainer;
import de.keksuccino.spiffyhud.api.item.CustomizationItemRegistry;
import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.CustomizationPropertiesHandler;
import de.keksuccino.spiffyhud.customization.items.*;
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
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.AirBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.ArmorBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.BossBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.CrosshairHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.ExperienceJumpBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.HotbarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.OverlayMessageHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.FoodMountHealthHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.PlayerHealthHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.SidebarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.SelectedItemNameHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.TitleHudElement;
import de.keksuccino.spiffyhud.events.CustomizationSystemReloadedEvent;
import de.keksuccino.spiffyhud.events.WindowResizedEvent;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.properties.PropertiesSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.ALL;

public class CustomizableIngameGui extends ForgeIngameGui {

	private boolean isEditor;

	public CrosshairHudElement crosshairElement = new CrosshairHudElement(this);
	public BossBarHudElement bossBarElement = new BossBarHudElement(this);
	public HotbarHudElement hotbarElement = new HotbarHudElement(this);
	public PlayerHealthHudElement healthElement = new PlayerHealthHudElement(this);
	public FoodMountHealthHudElement foodMountHealthElement = new FoodMountHealthHudElement(this);
	public ExperienceJumpBarHudElement experienceJumpBarElement = new ExperienceJumpBarHudElement(this);
//	public PotionIconsHudElement potionIconsElement = new PotionIconsHudElement(this);
	public ArmorBarHudElement armorBarElement = new ArmorBarHudElement(this);
	public AirBarHudElement airBarElement = new AirBarHudElement(this);
	public TitleHudElement titleElement = new TitleHudElement(this, false);
	public TitleHudElement subtitleElement = new TitleHudElement(this, true);
	public SelectedItemNameHudElement selectedItemNameElement = new SelectedItemNameHudElement(this);
	public OverlayMessageHudElement overlayMessageElement = new OverlayMessageHudElement(this);
	public SidebarHudElement sidebarElement = new SidebarHudElement(this);

	//Deprecated
	public Map<String, HudElementContainer> customElements;

	protected CustomizableBossHealthOverlay bossGui;

	protected List<CustomizationItemBase> backgroundElements = new ArrayList<>();
	protected List<CustomizationItemBase> foregroundElements = new ArrayList<>();

	protected boolean prevTickIsSingleplayer = true;
	protected boolean prevTickWorldNull = true;

	public boolean showVignette = true;

	public CustomizableIngameGui(Minecraft mc, boolean isEditor) {
		super(mc);

		this.isEditor = isEditor;

		this.bossGui = new CustomizableBossHealthOverlay(mc, this);

		this.customElements = HudElementRegistry.getInstance().getElements();

		if (!this.isEditor()) {
			MinecraftForge.EVENT_BUS.register(this);
			this.reloadHud();
		}
	}

	@SubscribeEvent
	public void onWindowResized(WindowResizedEvent e) {
		this.reloadHud();
	}

	@SubscribeEvent
	public void onSystemReloaded(CustomizationSystemReloadedEvent e) {
		this.reloadHud();
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) {
		if ((Minecraft.getInstance() != null)) {

			if ((Minecraft.getInstance().level == null) != this.prevTickWorldNull) {
				this.reloadHud();
			}
			this.prevTickWorldNull = (Minecraft.getInstance().level == null);

			if (Minecraft.getInstance().hasSingleplayerServer() != this.prevTickIsSingleplayer) {
				this.reloadHud();
			}
			this.prevTickIsSingleplayer = Minecraft.getInstance().hasSingleplayerServer();

		}
	}

	public void reloadHud() {

		try {

			if (this.isEditor()) {
				return;
			}

			this.foregroundElements.clear();
			this.backgroundElements.clear();

			this.customElements = HudElementRegistry.getInstance().getElements();

			for (HudElementContainer c : this.customElements.values()) {
				c.onResetElement();
				c.element.setHandler(this);
			}

			List<PropertiesSet> props = CustomizationPropertiesHandler.getProperties();

			boolean crossSet = false;
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

			List<String> customElementsSet = new ArrayList<>();

			for (PropertiesSet s : props) {

				boolean renderInBackground = false;

				List<PropertiesSection> metas = s.getPropertiesOfType("customization-meta");

				if (metas.isEmpty()) {
					continue;
				}

				String roString = metas.get(0).getEntryValue("renderorder");
				if ((roString != null) && roString.equalsIgnoreCase("background")) {
					renderInBackground = true;
				}

				String vignetteString = metas.get(0).getEntryValue("showvignette");
				if ((vignetteString != null) && vignetteString.equalsIgnoreCase("false")) {
					this.showVignette = false;
				}

				String showIn = metas.get(0).getEntryValue("showin");
				if (showIn != null) {
					if (showIn.equalsIgnoreCase("singleplayer")) {
						if (!Minecraft.getInstance().hasSingleplayerServer()) {
							continue;
						}
					}
					if (showIn.equalsIgnoreCase("multiplayer")) {
						if (Minecraft.getInstance().hasSingleplayerServer()) {
							continue;
						}
					}
				}

				//TODO reimplement biggerthan, smallerthan, etc ----> AT THIS LINE <----

				for (PropertiesSection sec : s.getPropertiesOfType("customization")) {
					String action = sec.getEntryValue("action");

					if (action != null) {

						if (!CustomizationHandler.isLightModeEnabled()) {

							/** ################## VANILLA ELEMENTS / CUSTOMIZATIONS ################## **/

							/** CROSSHAIR **/
							if (action.equalsIgnoreCase("editcrosshair")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("crosshair")) {
									this.backgroundElements.add(new CrosshairCustomizationItem(this.crosshairElement, sec, crossSet));
									crossSet = true;
								}
							}

							/** BOSS HEALTH **/
							if (action.equalsIgnoreCase("editbosshealth")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("boss")) {
									this.backgroundElements.add(new BossBarCustomizationItem(this.bossBarElement, sec, bossSet));
									bossSet = true;
								}
							}

							/** HOTBAR **/
							if (action.equalsIgnoreCase("edithotbar")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("hotbar")) {
									this.backgroundElements.add(new HotbarCustomizationItem(this.hotbarElement, sec, hotbarSet));
									hotbarSet = true;
								}
							}

							/** PLAYER HEALTH BAR **/
							if (action.equalsIgnoreCase("editplayerhealthbar")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("playerhealth")) {
									this.backgroundElements.add(new PlayerHealthBarCustomizationItem(this.healthElement, sec, playerHealthSet));
									playerHealthSet = true;
								}
							}

							/** PLAYER FOOD BAR | MOUNT HEALTH BAR **/
							if (action.equalsIgnoreCase("editplayerfoodbar")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("food")) {
									this.backgroundElements.add(new FoodMountHealthCustomizationItem(this.foodMountHealthElement, sec, foodSet));
									foodSet = true;
								}
							}

							/** EXPERIENCE BAR | JUMP BAR **/
							if (action.equalsIgnoreCase("editexperiencebar")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("experience")) {
									this.backgroundElements.add(new ExperienceJumpBarCustomizationItem(this.experienceJumpBarElement, sec, experienceSet));
									experienceSet = true;
								}
							}

							/** TITLE **/
							if (action.equalsIgnoreCase("edittitle")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("title")) {
									this.backgroundElements.add(new TitleCustomizationItem(this.titleElement, sec, titleSet));
									titleSet = true;
								}
							}

							/** SUBTITLE **/
							if (action.equalsIgnoreCase("editsubtitle")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("subtitle")) {
									this.backgroundElements.add(new TitleCustomizationItem(this.subtitleElement, sec, subtitleSet));
									subtitleSet = true;
								}
							}

							/** ARMOR BAR **/
							if (action.equalsIgnoreCase("editarmorbar")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("armor")) {
									this.backgroundElements.add(new ArmorBarCustomizationItem(this.armorBarElement, sec, armorSet));
									armorSet = true;
								}
							}

							/** AIR BAR **/
							if (action.equalsIgnoreCase("editairbar")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("air")) {
									this.backgroundElements.add(new AirBarCustomizationItem(this.airBarElement, sec, airSet));
									airSet = true;
								}
							}

							/** SELECTED ITEM NAME **/
							if (action.equalsIgnoreCase("editselecteditemname")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("selecteditem")) {
									this.backgroundElements.add(new SelectedItemNameCustomizationItem(this.selectedItemNameElement, sec, selectedItemNameSet));
									selectedItemNameSet = true;
								}
							}

							/** OVERLAY MESSAGE **/
							if (action.equalsIgnoreCase("editoverlaymessage")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("overlaymessage")) {
									this.backgroundElements.add(new OverlayMessageCustomizationItem(this.overlayMessageElement, sec, overlayMessageSet));
									overlayMessageSet = true;
								}
							}
							
							/** SIDEBAR **/
							if (action.equalsIgnoreCase("editsidebar")) {
								//TODO übernehmen
								if (InGameHudOverlay.isElementActive("sidebar")) {
									this.backgroundElements.add(new SidebarCustomizationItem(this.sidebarElement, sec, sidebarSet));
									sidebarSet = true;
								}
							}

						}

						/** ################## CUSTOM VANILLA ELEMENTS ################## **/

						//Deprecated (old API)
						if (action.startsWith("edit_")) {

							String id = action.split("[_]", 2)[1];
							HudElementContainer c = this.customElements.get(id);
							boolean isSecond = customElementsSet.contains(id);

							if (c != null) {

								this.backgroundElements.add(new CustomVanillaCustomizationItem(c, sec, isSecond));
								if (!customElementsSet.contains(id)) {
									customElementsSet.add(id);
								}

							}

						}

						//Custom vanilla HUD element handling (new API)
						if (action.startsWith("custom_vanilla_layout_element:")) {

							String identifier = action.split("[:]", 2)[1];
							VanillaHudElementContainer c = VanillaHudElementRegistry.getElement(identifier);
							boolean isSecond = customElementsSet.contains(identifier);

							if (c != null) {

								this.backgroundElements.add(new SimpleVanillaCustomizationItem(c, sec, isSecond));
								if (!customElementsSet.contains(identifier)) {
									customElementsSet.add(identifier);
								}

							}

						}

						/** ################## ITEMS ################## **/

						/** TEXT ELEMENT **/
						if (action.equalsIgnoreCase("addtext")) {
							if (renderInBackground) {
								backgroundElements.add(new StringCustomizationItem(sec));
							} else {
								foregroundElements.add(new StringCustomizationItem(sec));
							}
						}

						/** WEB TEXT ELEMENT **/
						if (action.equalsIgnoreCase("addwebtext")) {
							if (renderInBackground) {
								backgroundElements.add(new WebStringCustomizationItem(sec));
							} else {
								foregroundElements.add(new WebStringCustomizationItem(sec));
							}
						}

						/** TEXTURE ELEMENT **/
						if (action.equalsIgnoreCase("addtexture")) {
							if (renderInBackground) {
								backgroundElements.add(new TextureCustomizationItem(sec));
							} else {
								foregroundElements.add(new TextureCustomizationItem(sec));
							}
						}

						/** WEB TEXTURE ELEMENT **/
						if (action.equalsIgnoreCase("addwebtexture")) {
							if (renderInBackground) {
								backgroundElements.add(new WebTextureCustomizationItem(sec));
							} else {
								foregroundElements.add(new WebTextureCustomizationItem(sec));
							}
						}

						/** SHAPE ELEMENT **/
						if (action.equalsIgnoreCase("addshape")) {
							if (renderInBackground) {
								backgroundElements.add(new ShapeCustomizationItem(sec));
							} else {
								foregroundElements.add(new ShapeCustomizationItem(sec));
							}
						}

						/** SLIDESHOW ELEMENT **/
						if (action.equalsIgnoreCase("addslideshow")) {
							if (renderInBackground) {
								backgroundElements.add(new SlideshowCustomizationItem(sec));
							} else {
								foregroundElements.add(new SlideshowCustomizationItem(sec));
							}
						}

						/** SPLASH TEXT ELEMENT **/
						if (action.equalsIgnoreCase("addsplash")) {
							String file = sec.getEntryValue("splashfilepath");
							String text = sec.getEntryValue("text");
							if ((file != null) || (text != null)) {

								SplashTextCustomizationItem i = new SplashTextCustomizationItem(sec);

								if (renderInBackground) {
									backgroundElements.add(i);
								} else {
									foregroundElements.add(i);
								}

							}
						}

						/** ITEM STACK **/
						if (action.equalsIgnoreCase("additemstack")) {
							if (renderInBackground) {
								backgroundElements.add(new ItemStackCustomizationItem(sec));
							} else {
								foregroundElements.add(new ItemStackCustomizationItem(sec));
							}
						}

						/** MIRRORED PLAYER **/
						if (action.equalsIgnoreCase("addmirroredplayer")) {
							if (renderInBackground) {
								backgroundElements.add(new MirroredPlayerCustomizationItem(sec));
							} else {
								foregroundElements.add(new MirroredPlayerCustomizationItem(sec));
							}
						}
						
						/** CUSTOM HEALTH BAR **/
						if (action.equalsIgnoreCase("addcustomhealthbar")) {
							if (renderInBackground) {
								backgroundElements.add(new CustomHealthBarCustomizationItem(sec));
							} else {
								foregroundElements.add(new CustomHealthBarCustomizationItem(sec));
							}
						}
						
						/** CUSTOM FOOD BAR **/
						if (action.equalsIgnoreCase("addcustomfoodbar")) {
							if (renderInBackground) {
								backgroundElements.add(new CustomFoodBarCustomizationItem(sec));
							} else {
								foregroundElements.add(new CustomFoodBarCustomizationItem(sec));
							}
						}
						
						/** CUSTOM ARMOR BAR **/
						if (action.equalsIgnoreCase("addcustomarmorbar")) {
							if (renderInBackground) {
								backgroundElements.add(new CustomArmorBarCustomizationItem(sec));
							} else {
								foregroundElements.add(new CustomArmorBarCustomizationItem(sec));
							}
						}
						
						/** CUSTOM AIR BAR **/
						if (action.equalsIgnoreCase("addcustomairbar")) {
							if (renderInBackground) {
								backgroundElements.add(new CustomAirBarCustomizationItem(sec));
							} else {
								foregroundElements.add(new CustomAirBarCustomizationItem(sec));
							}
						}
						
						/** CUSTOM EXP BAR **/
						if (action.equalsIgnoreCase("addcustomexpbar")) {
							if (renderInBackground) {
								backgroundElements.add(new CustomExpBarCustomizationItem(sec));
							} else {
								foregroundElements.add(new CustomExpBarCustomizationItem(sec));
							}
						}

						/** CUSTOM MOUNT HEALTH BAR **/
						if (action.equalsIgnoreCase("addcustommounthealthbar")) {
							if (renderInBackground) {
								backgroundElements.add(new CustomMountHealthBarCustomizationItem(sec));
							} else {
								foregroundElements.add(new CustomMountHealthBarCustomizationItem(sec));
							}
						}

						/** CUSTOM MOUNT JUMP BAR **/
						if (action.equalsIgnoreCase("addcustommountjumpbar")) {
							if (renderInBackground) {
								backgroundElements.add(new CustomMountJumpBarCustomizationItem(sec));
							} else {
								foregroundElements.add(new CustomMountJumpBarCustomizationItem(sec));
							}
						}

						/** ################## CUSTOM ITEMS ################## **/

						//DEPRECATED (OLD API)
						if (action.startsWith("add_")) {
							String id = action.split("[_]", 2)[1];
							CustomizationItemContainer c = CustomizationItemRegistry.getInstance().getElement(id);
							if (c != null) {

								CustomizationItem i = c.constructWithProperties(sec);
								if (renderInBackground) {
									backgroundElements.add(i);
								} else {
									foregroundElements.add(i);
								}

							}
						}

						//NEW API
						/** CUSTOM ITEMS (API) **/
						if (action.startsWith("custom_layout_element:")) {
							String cusId = action.split("[:]", 2)[1];
							de.keksuccino.spiffyhud.api.item.v2.CustomizationItemContainer cusItem = de.keksuccino.spiffyhud.api.item.v2.CustomizationItemRegistry.getItem(cusId);
							if (cusItem != null) {
								de.keksuccino.spiffyhud.api.item.v2.CustomizationItem cusItemInstance = cusItem.constructCustomizedItemInstance(sec);
								if (renderInBackground) {
									backgroundElements.add(cusItemInstance);
								} else {
									foregroundElements.add(cusItemInstance);
								}
							}
						}

					}

				}

			}

			//Add dummy customization items to handle positions of all non-customized elements
			PropertiesSection dummySec = new PropertiesSection("customization");
			if (!crossSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("crosshair")) {
					this.backgroundElements.add(new CrosshairCustomizationItem(this.crosshairElement, dummySec, false));
				}
			}
			if (!bossSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("boss")) {
					this.backgroundElements.add(new BossBarCustomizationItem(this.bossBarElement, dummySec, false));
				}
			}
			if (!hotbarSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("hotbar")) {
					this.backgroundElements.add(new HotbarCustomizationItem(this.hotbarElement, dummySec, false));
				}
			}
			if (!playerHealthSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("playerhealth")) {
					this.backgroundElements.add(new PlayerHealthBarCustomizationItem(this.healthElement, dummySec, false));
				}
			}
			if (!foodSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("food")) {
					this.backgroundElements.add(new FoodMountHealthCustomizationItem(this.foodMountHealthElement, dummySec, false));
				}
			}
			if (!experienceSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("experience")) {
					this.backgroundElements.add(new ExperienceJumpBarCustomizationItem(this.experienceJumpBarElement, dummySec, false));
				}
			}
			if (!titleSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("title")) {
					this.backgroundElements.add(new TitleCustomizationItem(this.titleElement, dummySec, false));
				}
			}
			if (!subtitleSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("subtitle")) {
					this.backgroundElements.add(new TitleCustomizationItem(this.subtitleElement, dummySec, false));
				}
			}
			if (!armorSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("armor")) {
					this.backgroundElements.add(new ArmorBarCustomizationItem(this.armorBarElement, dummySec, false));
				}
			}
			if (!airSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("air")) {
					this.backgroundElements.add(new AirBarCustomizationItem(this.airBarElement, dummySec, false));
				}
			}
			if (!selectedItemNameSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("selecteditem")) {
					this.backgroundElements.add(new SelectedItemNameCustomizationItem(this.selectedItemNameElement, dummySec, false));
				}
			}
			if (!overlayMessageSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("overlaymessage")) {
					this.backgroundElements.add(new OverlayMessageCustomizationItem(this.overlayMessageElement, dummySec, false));
				}
			}
			if (!sidebarSet) {
				//TODO übernehmen
				if (InGameHudOverlay.isElementActive("sidebar")) {
					this.backgroundElements.add(new SidebarCustomizationItem(this.sidebarElement, dummySec, false));
				}
			}

			//Deprecated (old API)
			for (Map.Entry<String, HudElementContainer> m : this.customElements.entrySet()) {
				HudElementContainer c = m.getValue();
				if (!customElementsSet.contains(c.elementIdentifier)) {
					this.backgroundElements.add(new CustomVanillaCustomizationItem(c, dummySec, false));
				}
			}

			//Custom vanilla HUD element handling (new API)
			for (VanillaHudElementContainer c : VanillaHudElementRegistry.getElements()) {
				if (!customElementsSet.contains(c.getIdentifier())) {
					this.backgroundElements.add(new SimpleVanillaCustomizationItem(c, dummySec, false));
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void render(PoseStack matrix, float partialTicks) {

		try {

			if (!this.isEditor()) {
				this.renderBackgroundItems(matrix);
			}

			renderRaw(matrix, partialTicks);

			//Render custom vanilla elements
			if (!this.isEditor()) {
				this.renderCustomVanillaElements(matrix, partialTicks);
			}

			if (!this.isEditor()) {
				this.renderForegroundItems(matrix);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void renderRaw(PoseStack matrix, float partialTicks) {

		this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
		this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
		setEventParent(new RenderGameOverlayEvent(matrix, partialTicks, this.minecraft.getWindow()));

		right_height = 39;
		left_height = 39;

		if (pre(ALL, matrix)) return;

		setFont(minecraft.font);

		this.random.setSeed(tickCount * 312871L);

		OverlayRegistry.orderedEntries().forEach(entry -> {
			try {

				if (!entry.isEnabled()) return;

				IIngameOverlay overlay = entry.getOverlay();

				if (fireEventsFor(overlay)) {
					if (pre(overlay, matrix)) return;
				}

				overlay.render(this, matrix, partialTicks, screenWidth, screenHeight);

				if (fireEventsFor(overlay)) {
					post(overlay, matrix);
				}

			} catch(Exception e) {
				SpiffyHud.LOGGER.error("Error rendering overlay '{}'", entry.getDisplayName(), e);
			}
		});

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		post(ALL, matrix);

	}

	public boolean fireEventsFor(IIngameOverlay overlay) {
		if (overlay == AIR_LEVEL_ELEMENT) {
			return false;
		}
		if (overlay == ARMOR_LEVEL_ELEMENT) {
			return false;
		}
		if (overlay == BOSS_HEALTH_ELEMENT) {
			return false;
		}
		if (overlay == CROSSHAIR_ELEMENT) {
			return false;
		}
		if (overlay == EXPERIENCE_BAR_ELEMENT) {
			return false;
		}
		if (overlay == FOOD_LEVEL_ELEMENT) {
			return false;
		}
		if (overlay == MOUNT_HEALTH_ELEMENT) {
			return false;
		}
		if (overlay == HOTBAR_ELEMENT) {
			return false;
		}
		if (overlay == PLAYER_HEALTH_ELEMENT) {
			return false;
		}
		if (overlay == ITEM_NAME_ELEMENT) {
			return false;
		}
		if (overlay == SCOREBOARD_ELEMENT) {
			return false;
		}
		if (overlay == TITLE_TEXT_ELEMENT) {
			return false;
		}
		return true;
	}
	
	public void renderBackgroundItems(PoseStack matrix) {
		for (CustomizationItemBase c : this.backgroundElements) {
			c.render(matrix);
		}
	}

	public void renderForegroundItems(PoseStack matrix) {
		for (CustomizationItemBase c : this.foregroundElements) {
			c.render(matrix);
		}
	}

	public void renderCustomVanillaElements(PoseStack matrix, float partialTicks) {
		//Deprecated (old API)
		for (Map.Entry<String, HudElementContainer> m : this.customElements.entrySet()) {
			m.getValue().element.render(matrix, screenWidth, screenHeight, partialTicks);
		}
		//New API
		for (VanillaHudElementContainer c : VanillaHudElementRegistry.getElements()) {
			c.element.render(matrix, screenWidth, screenHeight, partialTicks);
		}
	}

	@Override
	protected void renderVignette(Entity entity) {
		if (this.showVignette) {
			super.renderVignette(entity);
		}
	}

	//Render Crosshair
	@Override
	protected void renderCrosshair(PoseStack matrix) {

		this.crosshairElement.render(matrix, screenWidth, screenHeight, Minecraft.getInstance().getFrameTime());

	}

//		@Override
//	    protected void renderPotionIcons(PoseStack matrix) {
//
//			if (this.potionIconsElement.fireEvents) {
//				if (pre(ElementType.POTION_ICONS, matrix, false)) return;
//			}
//
//			if (this.potionIconsElement.visible) {
//
//				this.renderPotionIconsRaw(matrix);
//
//			}
//
//			if (this.potionIconsElement.fireEvents) {
//				post(ElementType.POTION_ICONS, matrix, false);
//			}
//
//	//		this.potionIconsElement.render(matrix, scaledWidth, scaledHeight, Minecraft.getInstance().getRenderPartialTicks());
//
//	    }
//
//	protected void renderPotionIconsRaw(PoseStack matrix) {
//
//		Collection<MobEffectInstance> collection = this.minecraft.player.getActiveEffects();
//		if (!collection.isEmpty()) {
//			RenderSystem.enableBlend();
//			int i = 0;
//			int j = 0;
//			MobEffectTextureManager potionspriteuploader = this.minecraft.getMobEffectTextures();
//			List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());
//			RenderUtils.bindTexture(ContainerScreen.INVENTORY_LOCATION);
//
//			for(MobEffectInstance effectinstance : Ordering.natural().reverse().sortedCopy(collection)) {
//				MobEffect effect = effectinstance.getEffect();
//				if (!effectinstance.renderHud()) continue;
//				RenderUtils.bindTexture(ContainerScreen.INVENTORY_LOCATION);
//				if (effectinstance.showIcon()) {
//					int k = this.screenWidth;
//					int l = 1;
//					if (this.minecraft.isDemo()) {
//						l += 15;
//					}
//
//					if (effect.isBeneficial()) {
//						++i;
//						k = k - 25 * i;
//					} else {
//						++j;
//						k = k - 25 * j;
//						l += 26;
//					}
//
//					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//					float f = 1.0F;
//					if (effectinstance.isAmbient()) {
//						this.blit(matrix, k, l, 165, 166, 24, 24);
//					} else {
//						this.blit(matrix, k, l, 141, 166, 24, 24);
//						if (effectinstance.getDuration() <= 200) {
//							int i1 = 10 - effectinstance.getDuration() / 20;
//							f = Mth.clamp((float)effectinstance.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((float)effectinstance.getDuration() * (float)Math.PI / 5.0F) * Mth.clamp((float)i1 / 10.0F * 0.25F, 0.0F, 0.25F);
//						}
//					}
//
//					TextureAtlasSprite textureatlassprite = potionspriteuploader.get(effect);
//					int j1 = k;
//					int k1 = l;
//					float f1 = f;
//					list.add(() -> {
//						RenderUtils.bindTexture(textureatlassprite.atlas().location());
//						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f1);
//						blit(matrix, j1 + 3, k1 + 3, this.getBlitOffset(), 18, 18, textureatlassprite);
//					});
//					effectinstance.renderHUDEffect(this, matrix, k, l, this.getBlitOffset(), f);
//				}
//			}
//
//			list.forEach(Runnable::run);
//		}
//
//	}

	@Override
	protected void renderBossHealth(PoseStack matrix) {

		this.bossBarElement.render(matrix, screenWidth, screenHeight, Minecraft.getInstance().getFrameTime());

	}

	@Override
	protected void renderHotbar(float partialTicks, PoseStack matrix) {

		this.hotbarElement.render(matrix, screenWidth, screenHeight, partialTicks);

	}

	@Override
	public void renderHealth(int width, int height, PoseStack matrix) {

		this.healthElement.render(matrix, width, height, Minecraft.getInstance().getFrameTime());

		this.armorBarElement.render(matrix, width, height, Minecraft.getInstance().getFrameTime());

		this.foodMountHealthElement.render(matrix, width, height, Minecraft.getInstance().getFrameTime());

		this.airBarElement.render(matrix, width, height, Minecraft.getInstance().getFrameTime());

		this.experienceJumpBarElement.render(matrix, screenWidth, screenHeight, Minecraft.getInstance().getFrameTime());

	}

	//renderSelectedItemName
	@Override
	public void renderSelectedItemName(PoseStack matrix) {

		this.selectedItemNameElement.render(matrix, screenWidth, screenHeight, Minecraft.getInstance().getFrameTime());

	}

	@Override
	protected void renderRecordOverlay(int width, int height, float partialTicks, PoseStack matrix) {

		this.overlayMessageElement.render(matrix, width, height, partialTicks);

	}

	@Override
	public void renderFood(int width, int height, PoseStack matrix) {
		//empty bc food + mount health are rendered in renderHealth
	}

	@Override
	protected void renderHealthMount(int width, int height, PoseStack matrix) {
		//empty bc food + mount health are rendered in renderHealth
	}

	@Override
	public void renderJumpMeter(PoseStack matrix, int posX) {
		//empty bc exp + jump bar are rendered in renderHealth
	}

	@Override
	protected void renderExperience(int posX, PoseStack matrix) {
		//empty bc exp + jump bar are rendered in renderHealth
	}

	@Override
	protected void renderArmor(PoseStack matrix, int width, int height) {
		//empty bc armor bar is rendered in renderHealth
	}

	@Override
	protected void renderAir(int width, int height, PoseStack matrix) {
		//empty bc air bar is rendered in renderHealth
	}

	@Override
	protected void renderTitle(int width, int height, float partialTicks, PoseStack matrix) {

		this.titleElement.render(matrix, width, height, partialTicks);

		this.subtitleElement.render(matrix, width, height, partialTicks);

	}

	@Override
	protected void displayScoreboardSidebar(PoseStack matrix, Objective objective) {

		this.sidebarElement.render(matrix, screenWidth, screenHeight, Minecraft.getInstance().getFrameTime());

	}

	@Override
	public BossHealthOverlay getBossOverlay() {
		return this.bossGui;
	}

	public boolean pre(ElementType type, PoseStack matrix) {
		try {
			Method m = ForgeIngameGui.class.getDeclaredMethod("pre", ElementType.class, PoseStack.class);
			m.setAccessible(true);
			return (boolean) m.invoke(this, type, matrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void post(ElementType type, PoseStack matrix) {
		try {
			Method m = ForgeIngameGui.class.getDeclaredMethod("post", ElementType.class, PoseStack.class);
			m.setAccessible(true);
			m.invoke(this, type, matrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean pre(IIngameOverlay type, PoseStack matrix) {
		try {
			Method m = ForgeIngameGui.class.getDeclaredMethod("pre", IIngameOverlay.class, PoseStack.class);
			m.setAccessible(true);
			return (boolean) m.invoke(this, type, matrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void post(IIngameOverlay type, PoseStack matrix) {
		try {
			Method m = ForgeIngameGui.class.getDeclaredMethod("post", IIngameOverlay.class, PoseStack.class);
			m.setAccessible(true);
			m.invoke(this, type, matrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RenderGameOverlayEvent getEventParent() {
		try {
			Field f = ForgeIngameGui.class.getDeclaredField("eventParent");
			f.setAccessible(true);
			return (RenderGameOverlayEvent) f.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setEventParent(RenderGameOverlayEvent e) {
		try {
			Field f = ForgeIngameGui.class.getDeclaredField("eventParent");
			f.setAccessible(true);
			f.set(this, e);
		} catch (Exception ex) {
			SpiffyHud.LOGGER.error("[SPIFFY HUD] CustomizableIngameGui: setEventParent: ERROR: UNABLE TO SET EVENT PARENT!");
			ex.printStackTrace();
		}
	}

	public void setFont(Font font) {
		try {
			Field f = ForgeIngameGui.class.getDeclaredField("font");
			f.setAccessible(true);
			f.set(this, font);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void bind(ResourceLocation res) {
		RenderUtils.bindTexture(res);
	}

	public boolean isEditor() {
		return this.isEditor;
	}

	public Component getCurrentTitle() {
		return this.title;
	}

	public Component getCurrentSubTitle() {
		return this.subtitle;
	}

	public int getTitlesTimer() {
		return this.titleTime;
	}

	public int getTitleFadeOut() {
		return this.titleFadeOutTime;
	}

	public int getTitleFadeIn() {
		return this.titleFadeInTime;
	}

	public int getTitleDisplayTime() {
		return this.titleStayTime;
	}

	public int getRemainingHighlightingTicks() {
		return this.toolHighlightTimer;
	}

	public ItemStack getHighlightingItemStack() {
		return this.lastToolHighlight;
	}

	public int getOverlayMessageTime() {
		return this.overlayMessageTime;
	}

	public Component getOverlayMessage() {
		return this.overlayMessageString;
	}

	public boolean getAnimateOverlayMessageColor() {
		return this.animateOverlayMessageColor;
	}

	public void onClose() {
		if (!this.isEditor()) {
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}

}
