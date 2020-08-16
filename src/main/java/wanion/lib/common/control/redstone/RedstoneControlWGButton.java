package wanion.lib.common.control.redstone;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.Reference;
import wanion.lib.client.gui.interaction.WGMouseInteraction;
import wanion.lib.client.gui.button.ControlWGButton;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public final class RedstoneControlWGButton extends ControlWGButton<RedstoneControl, RedstoneControl.RedstoneState>
{
	public RedstoneControlWGButton(@Nonnull final GuiContainer guiContainer, @Nonnull EntityPlayer entityPlayer, final int x, final int y, @Nonnull final RedstoneControl control, int buttonId)
	{
		super(guiContainer, entityPlayer, control, Reference.GUI_TEXTURES, x, y, 18, 18);
	}

	@Override
	public int getTooltipX(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		return mouseInteraction.getMouseX() - (lineWidth / 2) - 12 - mouseInteraction.guiContainer.getGuiLeft();
	}

	@Override
	public int getTooltipY(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		return mouseInteraction.getMouseY() - 20 - mouseInteraction.guiContainer.getGuiTop();
	}
}