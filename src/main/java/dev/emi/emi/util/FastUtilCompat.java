package dev.emi.emi.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;

/**
 * Compatibility helpers for fastutil versions bundled with other mods (e.g. JEI).
 */
public final class FastUtilCompat {
	private FastUtilCompat() {
	}

	public static int getOrDefault(Object2IntMap<?> map, Object key, int defaultValue) {
		return map.containsKey(key) ? map.getInt(key) : defaultValue;
	}

	public static long getOrDefault(Object2LongMap<?> map, Object key, long defaultValue) {
		return map.containsKey(key) ? map.getLong(key) : defaultValue;
	}

	public static <K> int incrementDuplicate(Object2IntMap<K> map, K key) {
		int next = getOrDefault(map, key, 1) + 1;
		map.put(key, next);
		return next;
	}
}
