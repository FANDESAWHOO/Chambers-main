package me.hulipvp.chambers.util;

import org.bukkit.ChatColor;

public class Color {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static java.awt.Color toAwtColor(ChatColor chatColor) {
        switch (chatColor) {
            case BLACK:
                return new java.awt.Color(0, 0, 0);
            case DARK_BLUE:
                return new java.awt.Color(0, 0, 170);
            case DARK_GREEN:
                return new java.awt.Color(0, 170, 0);
            case DARK_AQUA:
                return new java.awt.Color(0, 170, 170);
            case DARK_RED:
                return new java.awt.Color(170, 0, 0);
            case DARK_PURPLE:
                return new java.awt.Color(170, 0, 170);
            case GOLD:
                return new java.awt.Color(255, 170, 0);
            case GRAY:
                return new java.awt.Color(170, 170, 170);
            case DARK_GRAY:
                return new java.awt.Color(85, 85, 85);
            case BLUE:
                return new java.awt.Color(85, 85, 255);
            case GREEN:
                return new java.awt.Color(85, 255, 85);
            case AQUA:
                return new java.awt.Color(85, 255, 255);
            case RED:
                return new java.awt.Color(255, 85, 85);
            case LIGHT_PURPLE:
                return new java.awt.Color(255, 85, 255);
            case YELLOW:
                return new java.awt.Color(255, 255, 85);
            case WHITE:
                return new java.awt.Color(255, 255, 255);
            default:
                return java.awt.Color.WHITE;
        }
    }
}
