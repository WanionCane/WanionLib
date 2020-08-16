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

public abstract class WGInteraction
{
	public final GuiContainer guiContainer;
	public final EntityPlayer entityPlayer;
	private final int mouseX, mouseY;

	public WGInteraction(@Nonnull final GuiContainer guiContainer, @Nonnull final EntityPlayer entityPlayer, final int mouseX, int mouseY)
	{
		this.guiContainer = guiContainer;
		this.entityPlayer = entityPlayer;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	public boolean isHovering(final int x, final int y, final int width, final int height)
	{
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
	}
}