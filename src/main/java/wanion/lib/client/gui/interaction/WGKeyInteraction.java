package wanion.lib.client.gui.interaction;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

public final class WGKeyInteraction extends WGInteraction
{
	private final int keyCode;

	public WGKeyInteraction(@Nonnull GuiContainer guiContainer, @Nonnull final EntityPlayer entityPlayer, final int mouseX, int mouseY, final int keyCode)
	{
		super(guiContainer, entityPlayer, mouseX, mouseY);
		this.keyCode = keyCode;
	}

	public int getKeyCode()
	{
		return keyCode;
	}
}