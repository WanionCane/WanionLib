package wanion.lib.client.Button;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import com.google.common.collect.Lists;
import joptsimple.internal.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import wanion.lib.Reference;
import wanion.lib.common.redstone.IRedstoneControllable;
import wanion.lib.common.redstone.RedstoneControlState;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class RedstoneControlButton extends GuiButton
{
	private final GuiContainer guiContainer;
	private final IRedstoneControllable redstoneControllable;
	private final ResourceLocation resourceLocation;

	public RedstoneControlButton(@Nonnull final GuiContainer guiContainer, final int x, final int y, @Nonnull final IRedstoneControllable redstoneControllable, int buttonId)
	{
		this(guiContainer, x, y, redstoneControllable, buttonId, Reference.GUI_TEXTURES);
	}

	public RedstoneControlButton(@Nonnull final GuiContainer guiContainer, final int x, final int y, @Nonnull final IRedstoneControllable redstoneControllable, int buttonId, @Nonnull ResourceLocation resourceLocation)
	{
		super(buttonId, x, y, 18, 18, Strings.EMPTY);
		this.guiContainer = guiContainer;
		this.redstoneControllable = redstoneControllable;
		this.resourceLocation = resourceLocation;
	}

	public void drawButton(@Nonnull final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks)
	{
		if (!this.visible)
			return;
		final RedstoneControlState redstoneControlState = redstoneControllable.getRedstoneControlState();
		mc.getTextureManager().bindTexture(resourceLocation);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		final Pair<Integer, Integer> texturePos = RedstoneControlState.getTexturePos(redstoneControlState, this.hovered);
		drawModalRectWithCustomSizedTexture(x, y, texturePos.getLeft(), texturePos.getRight(), 18, 18, 128, 128);
	}

	public void drawButtonForegroundLayer(final int mouseX, final int mouseY)
	{
		final RedstoneControlState redstoneControlState = redstoneControllable.getRedstoneControlState();
		int width = 0;
		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		final List<String> description = Lists.newArrayList(TextFormatting.RED + I18n.format("wanionlib.redstone.control") + ": " + TextFormatting.WHITE + I18n.format(RedstoneControlState.getStateName(redstoneControlState)), I18n.format(RedstoneControlState.getStateDescription(redstoneControlState)));
		for (final String line : description) {
			final int lineWidth = fontRenderer.getStringWidth(line);
			if (lineWidth > width)
				width = lineWidth;
		}
		guiContainer.drawHoveringText(description, mouseX - (width / 2) - 12 - guiContainer.getGuiLeft(), mouseY - 20 - guiContainer.getGuiTop());
	}
}