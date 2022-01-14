package de.keksuccino.spiffyhud.customization.rendering.slideshow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.events.CustomizationSystemReloadedEvent;

public class SlideshowHandler {
	
	private static Map<String, ExternalTextureSlideshowRenderer> slideshows = new HashMap<String, ExternalTextureSlideshowRenderer>();
	
	public static void init() {
		updateSlideshows();
		
		Konkrete.getEventHandler().registerEventsFrom(new SlideshowHandler());
	}
	
	public static void updateSlideshows() {
		File f = SpiffyHud.SLIDESHOW_DIR;
		
		if (!SpiffyHud.SLIDESHOW_DIR.exists()) {
			SpiffyHud.SLIDESHOW_DIR.mkdirs();
		}
		
		slideshows.clear();
		for (File f2 : f.listFiles()) {
			if (f2.isDirectory()) {
				File f3 = new File(f2.getPath() + "/slideshow.properties");
				File f4 = new File(f2.getPath() + "/images");
				if (f3.exists() && f4.exists()) {
					ExternalTextureSlideshowRenderer render = new ExternalTextureSlideshowRenderer(f2.getPath());
					String name = render.getName();
					if (name != null) {
						render.prepareSlideshow();
						slideshows.put(name, render);
					} else {
						SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: Invalid slideshow found: " + f2.getPath());
					}
				}
			}
		}
	}
	
	public static ExternalTextureSlideshowRenderer getSlideshow(String name) {
		return slideshows.get(name);
	}
	
	public static List<ExternalTextureSlideshowRenderer> getSlideshows() {
		List<ExternalTextureSlideshowRenderer> l = new ArrayList<ExternalTextureSlideshowRenderer>();
		l.addAll(slideshows.values());
		return l;
	}
	
	public static List<String> getSlideshowNames() {
		List<String> l = new ArrayList<String>();
		l.addAll(slideshows.keySet());
		return l;
	}
	
	public static boolean slideshowExists(String name) {
		return slideshows.containsKey(name);
	}
	
	@SubscribeEvent
	public void onSystemReload(CustomizationSystemReloadedEvent e) {
		updateSlideshows();
	}

}
