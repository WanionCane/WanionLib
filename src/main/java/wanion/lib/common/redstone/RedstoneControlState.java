package wanion.lib.common.redstone;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public enum RedstoneControlState
{
	IGNORED,
	OFF,
	ON;

	public static RedstoneControlState getNextRedstoneControlState(@Nonnull final RedstoneControlState redstoneControlState)
	{
		final int nextState = redstoneControlState.ordinal() + 1;
		return nextState > RedstoneControlState.values().length - 1 ? RedstoneControlState.values()[0] : RedstoneControlState.values()[nextState];
	}

	public static RedstoneControlState getPreviousRedstoneControlState(@Nonnull final RedstoneControlState redstoneControlState)
	{
		final int previousState = redstoneControlState.ordinal() - 1;
		return previousState >= 0 ? RedstoneControlState.values()[previousState] : RedstoneControlState.values()[RedstoneControlState.values().length - 1];
	}

	public static Pair<Integer, Integer> getTexturePos(@Nonnull final RedstoneControlState redstoneControlState, final boolean hovered)
	{
		return new ImmutablePair<>(!hovered ? 0 : 18, 54 + (18 * redstoneControlState.ordinal()));
	}

	@Nonnull
	public static RedstoneControlState getState(final int state)
	{
		return RedstoneControlState.values()[MathHelper.clamp(state, 0, RedstoneControlState.values().length)];
	}

	public static String getRedstoneControlName()
	{
		return "wanionlib.redstone.control";
	}

	public static String getStateName(@Nonnull final RedstoneControlState redstoneControlState)
	{
		return "wanionlib.redstone.control.state." + redstoneControlState.name().toLowerCase();
	}

	public static String getStateDescription(@Nonnull final RedstoneControlState redstoneControlState)
	{
		return getStateName(redstoneControlState) + ".desc";
	}
}