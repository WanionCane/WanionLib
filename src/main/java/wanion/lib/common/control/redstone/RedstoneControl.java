package wanion.lib.common.control.redstone;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import wanion.lib.common.control.IControl;

import javax.annotation.Nonnull;

public final class RedstoneControl implements IControl<RedstoneControl>
{
	private final TileEntity tileEntity;
	private State state = State.IGNORED;

	public RedstoneControl(@Nonnull final TileEntity tileEntity)
	{
		this.tileEntity = tileEntity;
	}

	public RedstoneControl(@Nonnull final TileEntity tileEntity, @Nonnull final State state)
	{
		this.tileEntity = tileEntity;
		this.state = state;
	}

	public State getRedstoneControlState()
	{
		return state != null ? state : State.IGNORED;
	}

	public void setRedstoneControlState(@Nonnull final State state)
	{
		this.state = state;
	}

	@Override
	public boolean canOperate()
	{
		if (state == State.IGNORED)
			return true;
		final boolean powered = tileEntity.getWorld().isBlockPowered(tileEntity.getPos());
		return state == State.OFF && !powered || state == State.ON && powered;
	}

	@Override
	public void writeToNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		nbtTagCompound.setInteger("RedstoneControl", state.ordinal());
	}

	@Override
	public void readFromNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		if (nbtTagCompound.hasKey("RedstoneControl"))
			state = State.values()[MathHelper.clamp(nbtTagCompound.getInteger("RedstoneControl"), 0, State.values().length - 1)];
	}

	@Nonnull
	@Override
	public RedstoneControl copy()
	{
		return new RedstoneControl(tileEntity, state);
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj == this || (obj instanceof RedstoneControl && this.state == ((RedstoneControl) obj).state);
	}

	public enum State
	{
		IGNORED,
		OFF,
		ON;

		public static State getNextRedstoneControlState(@Nonnull final State state)
		{
			final int nextState = state.ordinal() + 1;
			return nextState > State.values().length - 1 ? State.values()[0] : State.values()[nextState];
		}

		public static State getPreviousRedstoneControlState(@Nonnull final State state)
		{
			final int previousState = state.ordinal() - 1;
			return previousState >= 0 ? State.values()[previousState] : State.values()[State.values().length - 1];
		}

		public static Pair<Integer, Integer> getTexturePos(@Nonnull final State state, final boolean hovered)
		{
			return new ImmutablePair<>(!hovered ? 0 : 18, 54 + (18 * state.ordinal()));
		}

		@Nonnull
		public static State getState(final int state)
		{
			return State.values()[MathHelper.clamp(state, 0, State.values().length)];
		}

		public static String getRedstoneControlName()
		{
			return "wanionlib.redstone.control";
		}

		public static String getStateName(@Nonnull final State state)
		{
			return "wanionlib.redstone.control.state." + state.name().toLowerCase();
		}

		public static String getStateDescription(@Nonnull final State state)
		{
			return getStateName(state) + ".desc";
		}
	}
}