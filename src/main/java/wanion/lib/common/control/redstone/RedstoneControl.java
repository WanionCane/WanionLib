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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import wanion.lib.Reference;
import wanion.lib.common.control.IControlNameable;
import wanion.lib.common.control.IState;
import wanion.lib.common.control.IStateNameable;
import wanion.lib.common.control.IStateProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RedstoneControl implements IStateProvider<RedstoneControl, RedstoneControl.RedstoneState>, IControlNameable
{
	private final TileEntity tileEntity;
	private RedstoneState state = RedstoneState.IGNORED;

	public RedstoneControl(@Nonnull final TileEntity tileEntity)
	{
		this.tileEntity = tileEntity;
	}

	public RedstoneControl(@Nonnull final TileEntity tileEntity, @Nonnull final RedstoneState state)
	{
		this.tileEntity = tileEntity;
		this.state = state;
	}

	@Override
	public boolean canOperate()
	{
		if (state == RedstoneState.IGNORED)
			return true;
		final boolean powered = tileEntity.getWorld().isBlockPowered(tileEntity.getPos());
		return state == RedstoneState.OFF && !powered || state == RedstoneState.ON && powered;
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound redstoneControlNBT = new NBTTagCompound();
		redstoneControlNBT.setInteger("RedstoneControl", state.ordinal());
		return redstoneControlNBT;
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		if (nbtTagCompound.hasKey("RedstoneControl"))
			state = RedstoneState.values()[MathHelper.clamp(nbtTagCompound.getInteger("RedstoneControl"), 0, RedstoneState.values().length - 1)];
	}

	@Nonnull
	@Override
	public RedstoneControl copy()
	{
		return new RedstoneControl(tileEntity, state);
	}

	@Nonnull
	@Override
	public String getControlName()
	{
		return "wanionlib.redstone.control";
	}

	@Override
	public RedstoneState getState()
	{
		return state;
	}

	@Override
	public void setState(@Nonnull final RedstoneState state)
	{
		this.state = state;
	}

	@Override
	public void writeToNBT(@Nonnull final NBTTagCompound nbtTagCompound, @Nonnull final RedstoneState state)
	{
		nbtTagCompound.setInteger("RedstoneControl", state.ordinal());
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj == this || (obj instanceof RedstoneControl && this.state == ((RedstoneControl) obj).state);
	}

	public enum RedstoneState implements IState<RedstoneState>, IStateNameable
	{
		IGNORED,
		OFF,
		ON;

		@Nonnull
		@Override
		public RedstoneState getNextState()
		{
			final int nextState = ordinal() + 1;
			return nextState > values().length - 1 ? values()[0] : values()[nextState];
		}

		@Nonnull
		@Override
		public RedstoneState getPreviousState()
		{
			final int previousState = ordinal() - 1;
			return previousState >= 0 ? values()[previousState] : values()[values().length - 1];
		}

		@Override
		public ResourceLocation getTextureResourceLocation()
		{
			return Reference.GUI_TEXTURES;
		}

		@Override
		public Pair<Integer, Integer> getTexturePos(final boolean hovered)
		{
			return new ImmutablePair<>(!hovered ? 0 : 18, 54 + (18 * ordinal()));
		}

		@Nonnull
		@Override
		public String getStateName()
		{
			return "wanionlib.redstone.control.state." + name().toLowerCase();
		}
	}
}