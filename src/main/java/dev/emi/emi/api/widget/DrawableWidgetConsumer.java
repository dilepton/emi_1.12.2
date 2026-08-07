package dev.emi.emi.api.widget;

import shim.net.minecraft.client.gui.DrawContext;

public interface DrawableWidgetConsumer {

	void render(DrawContext draw, int mouseX, int mouseY, float delta);
}
