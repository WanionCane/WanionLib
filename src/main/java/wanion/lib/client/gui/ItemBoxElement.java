package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.Reference;
import wanion.lib.client.gui.interaction.WInteraction;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class ItemBoxElement extends ItemElement
{
	protected static final ResourceLocation DEFAULT_RESOURCE_LOCATION = Reference.GUI_TEXTURES;

	public ItemBoxElement(@Nonnull final Supplier<ItemStack> stackSupplier, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		super(stackSupplier, wGuiContainer, x, y, 18, 18);
	}

	@Override
	public void draw(@Nonnull final WInteraction wInteraction)
	{
		final ItemStack stack = this.stackSupplier.get();
		bindTexture(DEFAULT_RESOURCE_LOCATION);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		Gui.drawModalRectWithCustomSizedTexture(getUsableX(), getUsableY(), 90, 0, width, height, 128, 128);
		if (!stack.isEmpty()) {
			try {
				RenderHelper.enableGUIStandardItemLighting();
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0F, 1.0F, 1.0F);
				GlStateManager.enableRescaleNormal();
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
				wGuiContainer.mc.getRenderItem().renderItemIntoGUI(stack, getUsableX() + 1, getUsableY()  + 1);
				GlStateManager.popMatrix();
				RenderHelper.disableStandardItemLighting();
			} catch (Exception ignored) {}
		}
		if (wInteraction.isHovering(this)) {
			GlStateManager.disableDepth();
			GlStateManager.colorMask(true, true, true, false);
			GuiContainer.drawRect(getUsableX() + 1, getUsableY() + 1, getUsableX() + 17, getUsableY() + 17, 0x80FFFFFF);
			GlStateManager.colorMask(true, true, true, true);
			GlStateManager.enableDepth();
		}
	}
}