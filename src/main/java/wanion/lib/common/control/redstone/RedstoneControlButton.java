package wanion.lib.common.control.redstone;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.Reference;
import wanion.lib.client.button.ControlButton;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public final class RedstoneControlButton extends ControlButton<RedstoneControl, RedstoneControl.RedstoneState>
{
	public RedstoneControlButton(@Nonnull final GuiContainer guiContainer, final int x, final int y, @Nonnull final RedstoneControl control, int buttonId)
	{
		super(guiContainer, control, Reference.GUI_TEXTURES, buttonId, x, y, 18, 18);
	}

	@Override
	public int getTooltipX(@Nonnull final GuiContainer guiContainer, final int mouseX)
	{
		return mouseX - (lineWidth / 2) - 12 - guiContainer.getGuiLeft();
	}

	@Override
	public int getTooltipY(@Nonnull final GuiContainer guiContainer, int mouseY)
	{
		return mouseY - 20 - guiContainer.getGuiTop();
	}
}