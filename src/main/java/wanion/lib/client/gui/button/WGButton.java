package wanion.lib.client.gui.button;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import wanion.lib.client.gui.IWGElement;

import javax.annotation.Nonnull;

public abstract class WGButton implements IWGElement
{
	protected final EntityPlayer entityPlayer;
	protected final GuiContainer guiContainer;
	protected final int x, y, width, height;
	protected boolean enabled;

	public WGButton(@Nonnull final GuiContainer guiContainer, @Nonnull final EntityPlayer entityPlayer, final int x, final int y, final int width, final int height)
	{
		this(guiContainer, entityPlayer, x, y, width, height, true);
	}

	public WGButton(@Nonnull final GuiContainer guiContainer, final EntityPlayer entityPlayer, final int x, final int y, final int width, final int height, final boolean enabled)
	{
		this.guiContainer = guiContainer;
		this.entityPlayer = entityPlayer;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.enabled = enabled;
	}

	@Override
	public int getX()
	{
		return x;
	}

	@Override
	public int getY()
	{
		return y;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	@Nonnull
	public EntityPlayer getEntityPlayer()
	{
		return entityPlayer;
	}

	@Nonnull
	@Override
	public GuiContainer getGuiContainer()
	{
		return guiContainer;
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	@Override
	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}
}