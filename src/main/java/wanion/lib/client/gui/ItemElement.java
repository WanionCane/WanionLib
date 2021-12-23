package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.interaction.WInteraction;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class ItemElement extends WElement<ItemElement>
{
	public static final ITooltipSupplier DEFAULT_ITEMSTACK_TOOLTIP_SUPPLIER = ((interaction, stackSupplier) -> interaction.getWGuiContainer().getItemToolTip(stackSupplier.get()));
	protected final Supplier<ItemStack> stackSupplier;

	public ItemElement(@Nonnull final Supplier<ItemStack> stackSupplier, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		this(stackSupplier, wGuiContainer, x, y, 16, 16);
	}

	public ItemElement(@Nonnull final Supplier<ItemStack> getStack, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height)
	{
		super(wGuiContainer, x, y, width, height);
		this.stackSupplier = getStack;
		setTooltipSupplier(DEFAULT_ITEMSTACK_TOOLTIP_SUPPLIER);
		setForegroundCheck(interaction -> interaction.isHovering(this) && !stackSupplier.get().isEmpty());
		setItemStackSupplier(stackSupplier);
	}

	@Override
	public void draw(@Nonnull final WInteraction wInteraction)
	{
		final ItemStack stack = this.stackSupplier.get();
		if (stack.isEmpty())
			return;
		try {
			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.pushMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			GlStateManager.enableRescaleNormal();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			wGuiContainer.mc.getRenderItem().renderItemIntoGUI(stack, getUsableX(), getUsableY());
			GlStateManager.popMatrix();
		} catch (Exception ignored) {}
	}
}