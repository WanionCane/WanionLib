package wanion.lib.common.control.redstone;

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
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import wanion.lib.Reference;
import wanion.lib.WanionLib;
import wanion.lib.client.button.ControlButton;
import wanion.lib.network.RedstoneControlButtonClick;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class RedstoneControlButton extends ControlButton
{
	private final GuiContainer guiContainer;
	private final RedstoneControl redstoneControl;
	private final ResourceLocation resourceLocation;

	public RedstoneControlButton(@Nonnull final GuiContainer guiContainer, final int x, final int y, @Nonnull final RedstoneControl redstoneControl, int buttonId)
	{
		this(guiContainer, x, y, redstoneControl, buttonId, Reference.GUI_TEXTURES);
	}

	public RedstoneControlButton(@Nonnull final GuiContainer guiContainer, final int x, final int y, @Nonnull final RedstoneControl redstoneControl, int buttonId, @Nonnull ResourceLocation resourceLocation)
	{
		super(buttonId, x, y, 18, 18, Strings.EMPTY);
		this.guiContainer = guiContainer;
		this.redstoneControl = redstoneControl;
		this.resourceLocation = resourceLocation;
	}

	public void drawButton(@Nonnull final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks)
	{
		if (!this.visible)
			return;
		final RedstoneControl.State redstoneControlState = redstoneControl.getRedstoneControlState();
		mc.getTextureManager().bindTexture(resourceLocation);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		final Pair<Integer, Integer> texturePos = RedstoneControl.State.getTexturePos(redstoneControlState, this.hovered);
		drawModalRectWithCustomSizedTexture(x, y, texturePos.getLeft(), texturePos.getRight(), 18, 18, 128, 128);
	}

	public void drawButtonForegroundLayer(final int mouseX, final int mouseY)
	{
		final RedstoneControl.State redstoneControlState = redstoneControl.getRedstoneControlState();
		int width = 0;
		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		final List<String> description = Lists.newArrayList(TextFormatting.RED + I18n.format("wanionlib.redstone.control") + ": " + TextFormatting.WHITE + I18n.format(RedstoneControl.State.getStateName(redstoneControlState)), I18n.format(RedstoneControl.State.getStateDescription(redstoneControlState)));
		for (final String line : description) {
			final int lineWidth = fontRenderer.getStringWidth(line);
			if (lineWidth > width)
				width = lineWidth;
		}
		guiContainer.drawHoveringText(description, mouseX - (width / 2) - 12 - guiContainer.getGuiLeft(), mouseY - 20 - guiContainer.getGuiTop());
	}

	@Override
	public void controlAction(final boolean leftClick)
	{
		playPressSound(guiContainer.mc.getSoundHandler());
		WanionLib.networkWrapper.sendToServer(new RedstoneControlButtonClick(leftClick));
	}
}