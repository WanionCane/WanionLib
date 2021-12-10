package wanion.lib.client.gui.button;

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
import wanion.lib.client.gui.WElement;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public abstract class WButton<B extends WButton<B>> extends WElement<B>
{
	public WButton(@Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height)
	{
		this(wGuiContainer, x, y, width, height, true);
	}

	public WButton(@Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height, final boolean enabled)
	{
		super(wGuiContainer, x, y, width, height);
		this.enabled = enabled;
	}
}