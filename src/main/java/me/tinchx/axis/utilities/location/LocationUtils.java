package me.tinchx.axis.utilities.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {

    public static String getString(Location loc) {
        if (loc == null) {
            return "Location Not Found";
        }
        return loc.getX() + '|' +
                loc.getY() + '|' +
                loc.getZ() + '|' +
                loc.getWorld().getName() + '|' +
                loc.getYaw() + '|' +
                loc.getPitch();
    }

    public static Location getLocation(String s) {
        if (s == null || s.equals("Location Not Found") || s.equals("")) {
            return null;
        }
        String[] data = s.split("\\|");
        return new Location(Bukkit.getWorld(data[3]), Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
    }

    public static boolean isSameLocation(Location loc1, Location loc2) {
        return loc1 != null && loc1.equals(loc2);
    }
}