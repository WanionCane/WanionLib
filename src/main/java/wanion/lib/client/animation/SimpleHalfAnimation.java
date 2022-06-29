package wanion.lib.client.animation;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public final class SimpleHalfAnimation<R> extends SimpleAnimation<R>
{
	private boolean half;

	public SimpleHalfAnimation(@Nonnull final R[] frames)
	{
		super(frames);
	}

	@Override
	public void updateAnimation()
	{
		if (half ^= true)
			super.updateAnimation();
	}
}