package wanion.lib.client.gui.button;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import wanion.lib.client.gui.IWGElement;

public abstract class WGButton implements IWGElement
{
	protected final int x, y, width, height;
	private boolean enabled;

	public WGButton(final int x, final int y, final int width, final int height)
	{
		this(x, y, width, height, true);
	}

	public WGButton(final int x, final int y, final int width, final int height, final boolean enabled)
	{
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
	public boolean enabled()
	{
		return enabled;
	}

	@Override
	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}
}