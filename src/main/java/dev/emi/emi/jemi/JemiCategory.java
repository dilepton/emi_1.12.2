package dev.emi.emi.jemi;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.runtime.EmiDrawContext;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import shim.net.minecraft.text.Text;

public class JemiCategory extends EmiRecipeCategory {
	public final IRecipeCategory<?> category;

	public JemiCategory(IRecipeCategory<?> category) {
		super(EmiPort.id("jei", "/" + sanitize(category.getUid())), (raw, x, y, delta) -> {});
		this.category = category;
		this.icon = (raw, x, y, delta) -> {
			EmiDrawContext context = EmiDrawContext.wrap(raw);
			IDrawable icon = category.getIcon();
			if (icon != null) {
				icon.draw(Minecraft.getMinecraft(), x + (16 - icon.getWidth()) / 2, y + (16 - icon.getHeight()) / 2);
			} else {
				var workstations = EmiApi.getRecipeManager().getWorkstations(this);
				if (!workstations.isEmpty()) {
					workstations.get(0).render(context.raw(), x, y, delta, EmiIngredient.RENDER_ICON);
				} else {
					String title = category.getTitle();
					String abbr = title.length() > 2 ? title.substring(0, 2) : title;
					context.drawCenteredTextWithShadow(EmiPort.literal(abbr), x + 8, y + 2);
				}
			}
		};
		this.simplified = this.icon;
	}

	@Override
	public Text getName() {
		return Text.literal(category.getTitle());
	}

	private static String sanitize(String uid) {
		return uid.replace(':', '/');
	}
}
