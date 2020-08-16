package wanion.lib.client.gui.field;

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
import wanion.lib.common.field.IField;

import javax.annotation.Nonnull;

public abstract class WGField<F extends IField<F>> implements IWGElement
{
	protected final F field;
	protected final GuiContainer guiContainer;
	protected final EntityPlayer entityPlayer;
	protected final int x, y, width, height;

	public WGField(F field, @Nonnull GuiContainer guiContainer, @Nonnull final EntityPlayer entityPlayer, final int x, final int y, final int width, final int height)
	{
		this.field = field;
		this.guiContainer = guiContainer;
		this.entityPlayer = entityPlayer;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public F getField()
	{
		return field;
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

	@Nonnull
	@Override
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
		return true;
	}
}