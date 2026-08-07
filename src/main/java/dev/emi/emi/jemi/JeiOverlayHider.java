package dev.emi.emi.jemi;

import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.platform.EmiAgnos;
import dev.emi.emi.runtime.EmiLog;

/**
 * Hides JEI's on-screen overlays while keeping JEI loaded for JEMI recipe import.
 */
public final class JeiOverlayHider {
	private JeiOverlayHider() {
	}

	public static void apply() {
		if (!EmiConfig.hideJeiOverlay || !EmiAgnos.isModLoaded("jei")) {
			return;
		}
		try {
			Class<?> config = Class.forName("mezz.jei.config.Config");
			if ((boolean) config.getMethod("isOverlayEnabled").invoke(null)) {
				config.getMethod("toggleOverlayEnabled").invoke(null);
				EmiLog.info("[JEMI] Disabled JEI item overlay (EMI is the UI)");
			}
			if ((boolean) config.getMethod("isBookmarkOverlayEnabled").invoke(null)) {
				config.getMethod("toggleBookmarkEnabled").invoke(null);
				EmiLog.info("[JEMI] Disabled JEI bookmark overlay");
			}
		} catch (Throwable t) {
			EmiLog.error("[JEMI] Failed to hide JEI overlay", t);
		}
	}
}
