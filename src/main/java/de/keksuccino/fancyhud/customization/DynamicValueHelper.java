package de.keksuccino.fancyhud.customization;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import de.keksuccino.fancyhud.api.DynamicValueRegistry;
import de.keksuccino.fancyhud.api.DynamicValueRegistry.DynamicValue;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.ExtendedServerListData;
import net.minecraftforge.versions.mcp.MCPVersion;

public class DynamicValueHelper {
	
	public static String convertFromRaw(String in) {
		String playername = Minecraft.getInstance().getSession().getUsername();
		String playeruuid = Minecraft.getInstance().getSession().getPlayerID();
		String mcversion = MCPVersion.getMCVersion();
		
		//Convert &-formatcodes to real ones
		in = StringUtils.convertFormatCodes(in, "&", "§");
		
		//Replace player name and uuid placeholders
		in = in.replace("%playername%", playername);
		in = in.replace("%playeruuid%", playeruuid);
		
		//Replace mc version placeholder
		in = in.replace("%mcversion%", mcversion);

		//Replace mod version placeholder
		in = replaceModVersionPlaceolder(in);

		//Replace loaded mods placeholder
		in = in.replace("%loadedmods%", "" + getLoadedMods());

		//Replace total mods placeholder
		in = in.replace("%totalmods%", "" + getTotalMods());
		
		//Current player health
		in = in.replace("%currenthealth%", "" + getCurrentHP());
		
		//Max player health
		in = in.replace("%maxhealth%", "" + getMaxHP());
		
		//Current player food
		in = in.replace("%currentfood%", "" + getCurrentFood());
		
		//Current game day time hours
		in = in.replace("%daytimehours%", getDayTimeHours());
		
		//Current game day time minutes
		in = in.replace("%daytimeminutes%", getDayTimeMinutes());
		
		ClientPlayerEntity p = Minecraft.getInstance().player;
		
		if (p != null) {
			
			BlockPos pp = p.getPosition();
			
			if (pp != null) {
				
				in = in.replace("%posx%", "" + pp.getX());
				
				in = in.replace("%posy%", "" + pp.getY());
				
				in = in.replace("%posz%", "" + pp.getZ());
				
			}
			
			in = in.replace("%currentair%", "" + (int)(p.getAir() / 27.27));
			
			in = in.replace("%maxair%", "" + p.getMaxAir());
			
			in = in.replace("%currentscore%", "" + p.getScore());
			
			in = in.replace("%experience%", "" + p.experienceTotal);
			
			in = in.replace("%level%", "" + p.experienceLevel);
			
			in = in.replace("%currentarmor%", "" + p.getTotalArmorValue());
			
			ItemStack mh = p.getHeldItemMainhand();
			ItemStack oh = p.getHeldItemOffhand();
			
			if (mh != null) {
				
				in = in.replace("%mhitemname%", "" + mh.getDisplayName().getString());
				
				in = in.replace("%mhitemcurdurab%", "" + (mh.getMaxDamage() - mh.getDamage()));
				
				in = in.replace("%mhitemmaxdurab%", "" + mh.getMaxDamage());
				
			}
			
			if (oh != null) {
				
				in = in.replace("%ohitemname%", "" + oh.getDisplayName().getString());
				
				in = in.replace("%ohitemcurdurab%", "" + (oh.getMaxDamage() - oh.getDamage()));
				
				in = in.replace("%ohitemmaxdurab%", "" + oh.getMaxDamage());
				
			}
			
		}
		
		ServerData sd = Minecraft.getInstance().getCurrentServerData();
			
		//Current IP
		if (sd != null) {
			in = in.replace("%serverip%", "" + sd.serverIP);
		} else {
			in = in.replace("%serverip%", "none");
		}

		//Current MOTD
		if (sd != null) {
			in = in.replace("%servermotd%", "" + sd.serverMOTD.getString());
		} else {
			in = in.replace("%servermotd%", "none");
		}

		//Current Ping
		if (sd != null) {
			in = in.replace("%serverping%", "" + sd.pingToServer);
		} else {
			in = in.replace("%serverping%", "0");
		}

		//Current Player Count
		if (sd != null) {
			if (sd.playerList != null) {
				in = in.replace("%serverplayers%", "" + sd.playerList.size());
			} else {
				in = in.replace("%serverplayers%", "1");
			}
		} else {
			in = in.replace("%serverplayers%", "1");
		}

		//Current Server Mods Count
		if (sd != null) {
			ExtendedServerListData fd = sd.forgeData;
			if (fd != null) {
				in = in.replace("%servermods%", "" + fd.numberOfMods);
			} else {
				in = in.replace("%servermods%", "0");
			}
		} else {
			in = in.replace("%servermods%", "0");
		}
		
		Calendar c = Calendar.getInstance();
		
		in = in.replace("%realtimeyear%", "" + c.get(Calendar.YEAR));
		
		in = in.replace("%realtimemonth%", formatToFancyDateTime(c.get(Calendar.MONTH) + 1));
		
		in = in.replace("%realtimeday%", formatToFancyDateTime(c.get(Calendar.DAY_OF_MONTH)));
		
		in = in.replace("%realtimehour%", formatToFancyDateTime(c.get(Calendar.HOUR_OF_DAY)));
		
		in = in.replace("%realtimeminute%", formatToFancyDateTime(c.get(Calendar.MINUTE)));
		
		in = in.replace("%realtimesecond%", formatToFancyDateTime(c.get(Calendar.SECOND)));
		
		//Apply all custom value
		for (DynamicValue v : DynamicValueRegistry.getInstance().getValuesAsList()) {
			in = in.replace(v.getPlaceholder(), v.get());
		}
		
		return in;
	}
	
	public static boolean containsDynamicValues(String in) {
		String s = convertFromRaw(in);
		return !s.equals(in);
	}
	
	private static String replaceModVersionPlaceolder(String in) {
		try {
			if (in.contains("%version:")) {
				List<String> l = new ArrayList<String>();
				int index = -1;
				int i = 0;
				while (i < in.length()) {
					String s = "" + in.charAt(i);
					if (s.equals("%")) {
						if (index == -1) {
							index = i;
						} else {
							String sub = in.substring(index, i+1);
							if (sub.startsWith("%version:") && sub.endsWith("%")) {
								l.add(sub);
							}
							index = -1;
						}
					}
					i++;
				}
				for (String s : l) {
					if (s.contains(":")) {
						String blank = s.substring(1, s.length()-1);
						String mod = blank.split(":", 2)[1];
						if (ModList.get().isLoaded(mod)) {
							Optional<? extends ModContainer> o = ModList.get().getModContainerById(mod);
							if (o.isPresent()) {
								ModContainer c = o.get();
								String version = c.getModInfo().getVersion().toString();
								in = in.replace(s, version);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

	private static int getTotalMods() {
		File modDir = new File("mods");
		if (modDir.exists()) {
			int i = 0;
			for (File f : modDir.listFiles()) {
				if (f.isFile() && f.getName().toLowerCase().endsWith(".jar")) {
					i++;
				}
			}
			return i+2;
		}
		return -1;
	}

	private static int getLoadedMods() {
		try {
			return ModList.get().getMods().size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private static int getCurrentHP() {
		ClientPlayerEntity p = Minecraft.getInstance().player;
		if (p != null) {
			float raw = p.getHealth();
			String rawString = raw + "";
			String preDecimalString = rawString.split("[.]", 2)[0];
			String postDecimalString = rawString.split("[.]", 2)[1];
			if (postDecimalString.length() > 3) {
				postDecimalString = postDecimalString.substring(0, 3);
			}
			if (MathUtils.isInteger(preDecimalString) && MathUtils.isInteger(postDecimalString)) {
				int pre = Integer.parseInt(preDecimalString);
				int post = Integer.parseInt(postDecimalString);
				if (post > 0) {
					pre++;
				}
				return pre;
			}
		}
		return -1;
	}
	
	private static int getMaxHP() {
		ClientPlayerEntity p = Minecraft.getInstance().player;
		if (p != null) {
			return (int) p.getMaxHealth();
		}
		return -1;
	}
	
	private static int getCurrentFood() {
		ClientPlayerEntity p = Minecraft.getInstance().player;
		if (p != null) {
			return p.getFoodStats().getFoodLevel();
		}
		return -1;
	}
	
	private static long getDayTime() {
		ClientWorld w = Minecraft.getInstance().world;
		if (w != null) {
			return w.getDayTime();
		}
		return 1L;
	}
	
	private static String getDayTimeHours() {
		String hString = "00";
		long dt = getDayTime();
		while (dt >= 24000) {
			dt -= 24000;
		}
		long h = 0;
		if (dt < 18000) {
			h = (dt / 1000) + 6;
		} else {
			h = (dt / 1000) - 18;
		}
		hString = "" + h;
		if (hString.length() < 2) {
			hString = "0" + hString;
		}
		return hString;
	}
	
	private static String getDayTimeMinutes() {
		String minString = "00";
		long i = getDayTime() / 1000;
		long i2 = getDayTime() - (i * 1000);
		if (i2 <= 0) {
			return minString;
		}
		long min = (long)((float)i2 / 16.6F);
		if (min > 59) {
			min = 0;
		}
		minString = "" + min;
		if (minString.length() < 2) {
			minString = "0" + minString;
		}
		return minString;
	}
	
	private static String formatToFancyDateTime(int in) {
		String s = "" + in;
		if (s.length() < 2) {
			s = "0" + s;
		}
		return s;
	}

}
