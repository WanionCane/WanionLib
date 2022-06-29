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
public abstract class Animation<R>
{
	private final R[] frames;
	protected final int lastFrameIndex;
	protected int currentFrame = 0;

	public Animation(@Nonnull final R[] frames)
	{
		if ((lastFrameIndex = (this.frames = frames).length - 1) == -1)
			throw new RuntimeException("What is an Animation with no frames?");
	}

	public abstract void updateAnimation();

	public final R getCurrentFrame()
	{
		return frames[currentFrame];
	}
}