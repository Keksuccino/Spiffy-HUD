package de.keksuccino.spiffyhud.customization.rendering.ingamehud;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.keksuccino.konkrete.reflection.ReflectionHelper;
import de.keksuccino.spiffyhud.events.hud.*;
import de.keksuccino.spiffyhud.mixin.client.IMixinInGameHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.events.client.ClientTickEvent;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.api.hud.CustomVanillaCustomizationItem;
import de.keksuccino.spiffyhud.api.hud.HudElementContainer;
import de.keksuccino.spiffyhud.api.hud.HudElementRegistry;
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

public class CustomizableIngameGui extends InGameHud {

	private boolean isEditor;

	public IMixinInGameHud hud = (IMixinInGameHud) this;

	public CrosshairHudElement crosshairElement = new CrosshairHudElement(this);
	public BossBarHudElement bossBarElement = new BossBarHudElement(this);
	public HotbarHudElement hotbarElement = new HotbarHudElement(this);
	public PlayerHealthHudElement healthElement = new PlayerHealthHudElement(this);
	public FoodMountHealthHudElement foodMountHealthElement = new FoodMountHealthHudElement(this);
	public ExperienceJumpBarHudElement experienceJumpBarElement = new ExperienceJumpBarHudElement(this);
	public ArmorBarHudElement armorBarElement = new ArmorBarHudElement(this);
	public AirBarHudElement airBarElement = new AirBarHudElement(this);
	public TitleHudElement titleElement = new TitleHudElement(this, false);
	public TitleHudElement subtitleElement = new TitleHudElement(this, true);
	public SelectedItemNameHudElement selectedItemNameElement = new SelectedItemNameHudElement(this);
	public OverlayMessageHudElement overlayMessageElement = new OverlayMessageHudElement(this);
	public SidebarHudElement sidebarElement = new SidebarHudElement(this);

	public Map<String, HudElementContainer> customElements;

	protected CustomizableBossHealthOverlay bossGui;

	protected List<CustomizationItemBase> backgroundElements = new ArrayList<>();
	protected List<CustomizationItemBase> foregroundElements = new ArrayList<>();

	protected boolean prevTickIsSingleplayer = true;
	protected boolean prevTickWorldNull = true;

	public boolean showVignette = true;

	public CustomizableIngameGui(MinecraftClient mc, boolean isEditor) {
		super(mc);

		this.isEditor = isEditor;

		this.bossGui = new CustomizableBossHealthOverlay(mc, this);
		setBossBarHudInstance(this, bossGui);

		this.customElements = HudElementRegistry.getInstance().getElements();

		Konkrete.getEventHandler().registerEventsFrom(this);

		if (!this.isEditor()) {
			this.reloadHud();
		}
	}

	protected static void setBossBarHudInstance(InGameHud hudInstance, BossBarHud bossHud) {
		try {
			Field f = ReflectionHelper.findField(InGameHud.class, "bossBarHud", "field_2030");
			f.set(hudInstance, bossHud);
//			SpiffyHud.LOGGER.info("[SPIFFY HUD] Custom boss bar HUD instance set!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onWindowResized(WindowResizedEvent e) {
		if (!this.isEditor()) {
			this.reloadHud();
		}
	}

	@SubscribeEvent
	public void onSystemReloaded(CustomizationSystemReloadedEvent e) {
		if (!this.isEditor()) {
			this.reloadHud();
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent.Pre e) {
		if (!this.isEditor()) {
			if ((MinecraftClient.getInstance() != null)) {

				if ((MinecraftClient.getInstance().world == null) != this.prevTickWorldNull) {
					this.reloadHud();
				}
				this.prevTickWorldNull = (MinecraftClient.getInstance().world == null);

				if (MinecraftClient.getInstance().isIntegratedServerRunning() != this.prevTickIsSingleplayer) {
					this.reloadHud();
				}
				this.prevTickIsSingleplayer = MinecraftClient.getInstance().isIntegratedServerRunning();

			}
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
						if (!MinecraftClient.getInstance().isIntegratedServerRunning()) {
							continue;
						}
					}
					if (showIn.equalsIgnoreCase("multiplayer")) {
						if (MinecraftClient.getInstance().isIntegratedServerRunning()) {
							continue;
						}
					}
				}

				for (PropertiesSection sec : s.getPropertiesOfType("customization")) {
					String action = sec.getEntryValue("action");

					if (action != null) {

						if (!CustomizationHandler.isLightModeEnabled()) {

							/** ################## VANILLA ELEMENTS / CUSTOMIZATIONS ################## **/

							/** CROSSHAIR **/
							if (action.equalsIgnoreCase("editcrosshair")) {
								this.backgroundElements.add(new CrosshairCustomizationItem(this.crosshairElement, sec, crossSet));
								crossSet = true;
							}

							/** BOSS HEALTH **/
							if (action.equalsIgnoreCase("editbosshealth")) {
								this.backgroundElements.add(new BossBarCustomizationItem(this.bossBarElement, sec, bossSet));
								bossSet = true;
							}

							/** HOTBAR **/
							if (action.equalsIgnoreCase("edithotbar")) {
								this.backgroundElements.add(new HotbarCustomizationItem(this.hotbarElement, sec, hotbarSet));
								hotbarSet = true;
							}

							/** PLAYER HEALTH BAR **/
							if (action.equalsIgnoreCase("editplayerhealthbar")) {
								this.backgroundElements.add(new PlayerHealthBarCustomizationItem(this.healthElement, sec, playerHealthSet));
								playerHealthSet = true;
							}

							/** PLAYER FOOD BAR | MOUNT HEALTH BAR **/
							if (action.equalsIgnoreCase("editplayerfoodbar")) {
								this.backgroundElements.add(new FoodMountHealthCustomizationItem(this.foodMountHealthElement, sec, foodSet));
								foodSet = true;
							}

							/** EXPERIENCE BAR | JUMP BAR **/
							if (action.equalsIgnoreCase("editexperiencebar")) {
								this.backgroundElements.add(new ExperienceJumpBarCustomizationItem(this.experienceJumpBarElement, sec, experienceSet));
								experienceSet = true;
							}

							/** TITLE **/
							if (action.equalsIgnoreCase("edittitle")) {
								this.backgroundElements.add(new TitleCustomizationItem(this.titleElement, sec, titleSet));
								titleSet = true;
							}

							/** SUBTITLE **/
							if (action.equalsIgnoreCase("editsubtitle")) {
								this.backgroundElements.add(new TitleCustomizationItem(this.subtitleElement, sec, subtitleSet));
								subtitleSet = true;
							}

							/** ARMOR BAR **/
							if (action.equalsIgnoreCase("editarmorbar")) {
								this.backgroundElements.add(new ArmorBarCustomizationItem(this.armorBarElement, sec, armorSet));
								armorSet = true;
							}

							/** AIR BAR **/
							if (action.equalsIgnoreCase("editairbar")) {
								this.backgroundElements.add(new AirBarCustomizationItem(this.airBarElement, sec, airSet));
								airSet = true;
							}

							/** SELECTED ITEM NAME **/
							if (action.equalsIgnoreCase("editselecteditemname")) {
								this.backgroundElements.add(new SelectedItemNameCustomizationItem(this.selectedItemNameElement, sec, selectedItemNameSet));
								selectedItemNameSet = true;
							}

							/** OVERLAY MESSAGE **/
							if (action.equalsIgnoreCase("editoverlaymessage")) {
								this.backgroundElements.add(new OverlayMessageCustomizationItem(this.overlayMessageElement, sec, overlayMessageSet));
								overlayMessageSet = true;
							}
							
							/** SIDEBAR **/
							if (action.equalsIgnoreCase("editsidebar")) {
								this.backgroundElements.add(new SidebarCustomizationItem(this.sidebarElement, sec, sidebarSet));
								sidebarSet = true;
							}

						}

						/** ################## CUSTOM VANILLA ELEMENTS ################## **/

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

					}

				}

			}

			//Add dummy customization items to handle positions of all non-customized elements
			PropertiesSection dummySec = new PropertiesSection("customization");
			if (!crossSet) {
				this.backgroundElements.add(new CrosshairCustomizationItem(this.crosshairElement, dummySec, false));
			}
			if (!bossSet) {
				this.backgroundElements.add(new BossBarCustomizationItem(this.bossBarElement, dummySec, false));
			}
			if (!hotbarSet) {
				this.backgroundElements.add(new HotbarCustomizationItem(this.hotbarElement, dummySec, false));
			}
			if (!playerHealthSet) {
				this.backgroundElements.add(new PlayerHealthBarCustomizationItem(this.healthElement, dummySec, false));
			}
			if (!foodSet) {
				this.backgroundElements.add(new FoodMountHealthCustomizationItem(this.foodMountHealthElement, dummySec, false));
			}
			if (!experienceSet) {
				this.backgroundElements.add(new ExperienceJumpBarCustomizationItem(this.experienceJumpBarElement, dummySec, false));
			}
			if (!titleSet) {
				this.backgroundElements.add(new TitleCustomizationItem(this.titleElement, dummySec, false));
			}
			if (!subtitleSet) {
				this.backgroundElements.add(new TitleCustomizationItem(this.subtitleElement, dummySec, false));
			}
			if (!armorSet) {
				this.backgroundElements.add(new ArmorBarCustomizationItem(this.armorBarElement, dummySec, false));
			}
			if (!airSet) {
				this.backgroundElements.add(new AirBarCustomizationItem(this.airBarElement, dummySec, false));
			}
			if (!selectedItemNameSet) {
				this.backgroundElements.add(new SelectedItemNameCustomizationItem(this.selectedItemNameElement, dummySec, false));
			}
			if (!overlayMessageSet) {
				this.backgroundElements.add(new OverlayMessageCustomizationItem(this.overlayMessageElement, dummySec, false));
			}
			if (!sidebarSet) {
				this.backgroundElements.add(new SidebarCustomizationItem(this.sidebarElement, dummySec, false));
			}

			for (Map.Entry<String, HudElementContainer> m : this.customElements.entrySet()) {
				HudElementContainer c = m.getValue();
				if (!customElementsSet.contains(c.elementIdentifier)) {
					this.backgroundElements.add(new CustomVanillaCustomizationItem(c, dummySec, false));
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void render(MatrixStack matrix, float partialTicks) {

		try {

			if (!this.isEditor()) {
				this.renderBackgroundItems(matrix);
			}

			Text cachedOverlayMessage = hud.getOverlayMessage();
			Text cachedTitle = hud.getTitle();
			Text cachedSubtitle = hud.getSubtitle();

			//Set overlay and title + subtitle to NULL to cancel its rendering
			hud.setOverlayMessage(null);
			this.setTitle(null);
			this.setSubtitle(null);

			//Call original render method
			super.render(matrix, partialTicks);

			//Set overlay and title + subtitle back to their original values
			hud.setOverlayMessage(cachedOverlayMessage);
			this.setTitle(cachedTitle);
			this.setSubtitle(cachedSubtitle);

			//Render customized overlay and title + subtitle elements
			this.renderOverlayMessage(matrix, hud.getScaledWidth(), hud.getScaledHeight(), partialTicks);
			this.renderTitleAndSubtitle(matrix, hud.getScaledWidth(), hud.getScaledHeight(), partialTicks);

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
	
	public void renderBackgroundItems(MatrixStack matrix) {
		for (CustomizationItemBase c : this.backgroundElements) {
			c.render(matrix);
		}
	}

	public void renderForegroundItems(MatrixStack matrix) {
		for (CustomizationItemBase c : this.foregroundElements) {
			c.render(matrix);
		}
	}

	public void renderCustomVanillaElements(MatrixStack matrix, float partialTicks) {
		for (Map.Entry<String, HudElementContainer> m : this.customElements.entrySet()) {
			m.getValue().element.render(matrix, hud.getScaledWidth(), hud.getScaledHeight(), partialTicks);
		}
	}

	@SubscribeEvent
	public void onRenderVignette(RenderVignetteEvent.Pre e) {
		if (e.hud == this) {
			if (!this.showVignette) {
				e.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onRenderCrosshair(RenderCrosshairEvent.Pre e) {
		if (e.hud == this) {
			e.setCanceled(true);
			this.crosshairElement.render(e.matrixStack, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());
		}
	}

	@SubscribeEvent
	public void onRenderBossBars(RenderBossBarsEvent.Pre e) {
		if (e.hud == this) {
			e.setCanceled(true);
			this.bossBarElement.render(e.matrixStack, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());
		}
	}

	@SubscribeEvent
	public void onRenderHotbar(RenderHotbarEvent.Pre e) {
		if (e.hud == this) {
			e.setCanceled(true);
			this.hotbarElement.render(e.matrixStack, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());
		}
	}

	@SubscribeEvent
	public void onRenderStatusBars(RenderStatusBarsEvent.Pre e) {
		if (e.hud == this) {
			e.setCanceled(true);

			this.healthElement.render(e.matrixStack, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());

			this.armorBarElement.render(e.matrixStack, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());

			this.foodMountHealthElement.render(e.matrixStack, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());

			this.airBarElement.render(e.matrixStack, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());

		}
	}

	@Override
	public void renderHeldItemTooltip(MatrixStack matrix) {

		this.selectedItemNameElement.render(matrix, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());

	}

	protected void renderOverlayMessage(MatrixStack matrix, int width, int height, float partialTicks) {

		this.overlayMessageElement.render(matrix, width, height, partialTicks);

	}

	@SubscribeEvent
	public void onRenderMountHealth(RenderMountHealthEvent.Pre e) {
		if (e.hud == this) {
			//cancel, because mount health is rendered with food
			e.setCanceled(true);
		}
	}

	@Override
	public void renderMountJumpBar(MatrixStack matrix, int posX) {
		//empty, because jump bar is rendered with exp bar
	}

	@Override
	public void renderExperienceBar(MatrixStack matrix, int x) {

		this.experienceJumpBarElement.render(matrix, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());

	}

	protected void renderTitleAndSubtitle(MatrixStack matrix, int width, int height, float partialTicks) {

		if (this.isEditor()) {
			hud.setTitleTotalTicks(Integer.MAX_VALUE);
			this.setTitle(new LiteralText("Title"));
			this.setSubtitle(new LiteralText("Subtitle"));
		}

		this.titleElement.render(matrix, width, height, partialTicks);

		this.subtitleElement.render(matrix, width, height, partialTicks);

	}

	@SubscribeEvent
	public void onRenderSidebar(RenderSidebarEvent.Pre e) {
		if (e.hud == this) {
			e.setCanceled(true);
			this.sidebarElement.render(e.matrixStack, hud.getScaledWidth(), hud.getScaledHeight(), MinecraftClient.getInstance().getTickDelta());
		}
	}

//	@Override
//	public BossBarHud getBossBarHud() {
//		return this.bossGui;
//	}

	public void bind(Identifier res) {
		RenderUtils.bindTexture(res);
	}

	public boolean isEditor() {
		return this.isEditor;
	}

	public Text getCurrentTitle() {
		return hud.getTitle();
	}

	public Text getCurrentSubTitle() {
		return hud.getSubtitle();
	}

	public int getTitlesTimer() {
		return hud.getTitleTotalTicks();
	}

	public int getTitleFadeOut() {
		return hud.getTitleFadeOutTicks();
	}

	public int getTitleFadeIn() {
		return hud.getTitleFadeInTicks();
	}

	public int getTitleDisplayTime() {
		return hud.getTitleRemainTicks();
	}

	public int getRemainingHighlightingTicks() {
		return hud.getHeldItemTooltipFade();
	}

	public ItemStack getHighlightingItemStack() {
		return hud.getCurrentStack();
	}

	public int getOverlayMessageTime() {
		return hud.getOverlayRemaining();
	}

	public Text getOverlayMessageText() {
		return hud.getOverlayMessage();
	}

	public boolean getAnimateOverlayMessageColor() {
		return hud.getOverlayTinted();
	}

	public boolean shouldDrawSurvivalElements() {
		return MinecraftClient.getInstance().interactionManager.hasStatusBars();
	}

}
