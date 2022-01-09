package de.keksuccino.spiffyhud.utils;

import java.util.stream.Stream;

import com.google.common.collect.Streams;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;

public class TextComponentUtils {

	public static String getFormattedStringFromComponent(Component component) {
		StringBuilder stringbuilder = new StringBuilder();
		String s = "";

		String s1 = component.getString();
		if (!s1.isEmpty()) {
			String s2 = getFormattingCodesFromStyle(component.getStyle());
			if (!s2.equals(s)) {
				if (!s.isEmpty()) {
					stringbuilder.append(ChatFormatting.RESET);
				}

				stringbuilder.append(s2);
				s = s2;
			}

			stringbuilder.append(s1);
		}

		for (Component c : component.getSiblings()) {
			String s3 = c.getString();
			if (!s3.isEmpty()) {
				String s2 = getFormattingCodesFromStyle(c.getStyle());
				if (!s2.equals(s)) {
					if (!s.isEmpty()) {
						stringbuilder.append(ChatFormatting.RESET);
					}

					stringbuilder.append(s2);
					s = s2;
				}

				stringbuilder.append(s3);
			}
		}

		if (!s.isEmpty()) {
			stringbuilder.append(ChatFormatting.RESET);
		}

		return stringbuilder.toString();
	}

	public static Stream<Component> getStreamOfComponent(Component component) {
		return Streams.concat(Stream.of(component));
	}

	public static String getFormattingCodesFromStyle(Style style) {
		String s = "";

		TextColor c = style.getColor();
		if (c != null) {
			if (c.getValue() == ChatFormatting.AQUA.getColor()) {
				s += "§b";
			}
			if (c.getValue() == ChatFormatting.BLACK.getColor()) {
				s += "§0";
			}
			if (c.getValue() == ChatFormatting.BLUE.getColor()) {
				s += "§9";
			}
			if (c.getValue() == ChatFormatting.DARK_AQUA.getColor()) {
				s += "§3";
			}
			if (c.getValue() == ChatFormatting.DARK_BLUE.getColor()) {
				s += "§1";
			}
			if (c.getValue() == ChatFormatting.DARK_GRAY.getColor()) {
				s += "§8";
			}
			if (c.getValue() == ChatFormatting.DARK_GREEN.getColor()) {
				s += "§2";
			}
			if (c.getValue() == ChatFormatting.DARK_PURPLE.getColor()) {
				s += "§5";
			}
			if (c.getValue() == ChatFormatting.DARK_RED.getColor()) {
				s += "§4";
			}
			if (c.getValue() == ChatFormatting.GOLD.getColor()) {
				s += "§6";
			}
			if (c.getValue() == ChatFormatting.GRAY.getColor()) {
				s += "§7";
			}
			if (c.getValue() == ChatFormatting.GREEN.getColor()) {
				s += "§a";
			}
			if (c.getValue() == ChatFormatting.LIGHT_PURPLE.getColor()) {
				s += "§d";
			}
			if (c.getValue() == ChatFormatting.RED.getColor()) {
				s += "§c";
			}
			if (c.getValue() == ChatFormatting.WHITE.getColor()) {
				s += "§f";
			}
			if (c.getValue() == ChatFormatting.YELLOW.getColor()) {
				s += "§e";
			}
		}

		if (style.isBold()) {
			s += "§l";
		}
		if (style.isItalic()) {
			s += "§o";
		}
		if (style.isObfuscated()) {
			s += "§k";
		}
		if (style.isStrikethrough()) {
			s += "§m";
		}
		if (style.isUnderlined()) {
			s += "§n";
		}

		return s;
	}

	public static String toHexString(int red, int green, int blue) {
		String hex = String. format("#%02X%02X%02X", red, green, blue);
		return hex;
	}
	
	public static TextComponent getWithGradient(java.awt.Color start, String text, java.awt.Color end) {

		TextComponent sc = new TextComponent("");

		int length = text.length();
		int i = 0;
		for (char c : text.toCharArray()) {

			int red = (int)(start.getRed() + ((float)(end.getRed() - start.getRed())) / (length - 1) * i);
			int green = (int)(start.getGreen() + ((float)(end.getGreen() - start.getGreen())) / (length - 1) * i);
			int blue = (int)(start.getBlue() + ((float)(end.getBlue() - start.getBlue())) / (length - 1) * i);

			TextComponent charComp = new TextComponent("" + c);

			Style s = charComp.getStyle();
			TextColor textColor = TextColor.parseColor(toHexString(red, green, blue));
			Style colored = s.withColor(textColor);

			charComp.setStyle(colored);

			sc.append(charComp);
			
			i++;

		}

		return sc;

	}

}
