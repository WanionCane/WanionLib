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
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.client.gui.button.ControlWButton;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public final class RedstoneControlWButton extends ControlWButton<RedstoneControl, RedstoneControl.RedstoneState>
{
	public RedstoneControlWButton(@Nonnull final RedstoneControl redStoneControl, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		super(redStoneControl, wGuiContainer, x, y);
	}

	@Override
	public int getTooltipX(@Nonnull final WInteraction wInteraction)
	{
		return wInteraction.getMouseX() - (lineWidth / 2) - 12 - wGuiContainer.getGuiLeft();
	}

	@Override
	public int getTooltipY(@Nonnull final WInteraction wInteraction)
	{
		return wInteraction.getMouseY() - 20 - wGuiContainer.getGuiTop();
	}
}