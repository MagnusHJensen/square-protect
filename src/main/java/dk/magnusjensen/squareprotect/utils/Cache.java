package dk.magnusjensen.squareprotect.utils;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    public static final Map<ResourceLocation, Integer> DIMENSIONS = new HashMap<>(); // ResourceLocation -> World row id in Database.
}
