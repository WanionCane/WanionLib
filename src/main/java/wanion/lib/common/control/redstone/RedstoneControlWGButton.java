package wanion.lib.common.control.redstone;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.WGContainer;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.button.ControlWGButton;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public final class RedstoneControlWGButton extends ControlWGButton<RedstoneControl, RedstoneControl.RedstoneState>
{
	public RedstoneControlWGButton(@Nonnull final RedstoneControl redStoneControl, @Nonnull final WGContainer<?> wgContainer, final int x, final int y)
	{
		super(redStoneControl, wgContainer, x, y);
	}

	@Override
	public int getTooltipX(@Nonnull final WGInteraction wgInteraction)
	{
		return wgInteraction.getMouseX() - (lineWidth / 2) - 12 - wgInteraction.WGContainer.getGuiLeft();
	}

	@Override
	public int getTooltipY(@Nonnull final WGInteraction wgInteraction)
	{
		return wgInteraction.getMouseY() - 20 - wgInteraction.WGContainer.getGuiTop();
	}
}