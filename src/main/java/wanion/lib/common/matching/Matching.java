package wanion.lib.common.matching;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import wanion.lib.common.control.IControl;
import wanion.lib.common.control.IControlNameable;
import wanion.lib.common.matching.matcher.AbstractMatcher;
import wanion.lib.common.matching.matcher.ItemStackMatcher;
import wanion.lib.common.matching.matcher.MatcherEnum;

import javax.annotation.Nonnull;

public class Matching implements IControl<Matching>, IControlNameable
{
	private final NonNullList<ItemStack> itemStacks;
	private final int number;
	private final String stringNumber;
	private final boolean shouldUseNbt;
	private AbstractMatcher matcher = new ItemStackMatcher(this);

	public Matching(@Nonnull final NonNullList<ItemStack> itemStacks, final int number)
	{
		this(itemStacks, number, false);
	}

	public Matching(@Nonnull final NonNullList<ItemStack> itemStacks, final int number, final boolean shouldUseNbt)
	{
		this.itemStacks = itemStacks;
		this.stringNumber = Integer.toString(this.number = number);
		this.shouldUseNbt = shouldUseNbt;
	}

	public void resetMatcher()
	{
		this.matcher = new ItemStackMatcher(this);
	}

	public void nextMatcher()
	{
		this.matcher = matcher.next();
	}

	public AbstractMatcher getMatcher()
	{
		return matcher;
	}

	public ItemStack getStack()
	{
		return itemStacks.get(number);
	}

	public boolean shouldUseNbt()
	{
		return shouldUseNbt;
	}

	@Override
	public void writeToNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		if (!nbtTagCompound.hasKey("Matching"))
			nbtTagCompound.setTag("Matching", new NBTTagCompound());
		final NBTTagCompound subCompound = nbtTagCompound.getCompoundTag("Matching");
		final NBTTagCompound matchingCompound = new NBTTagCompound();
		matchingCompound.setInteger("matcherType", matcher.getMatcherEnum().ordinal());
		matcher.writeToNBT(matchingCompound);
		subCompound.setTag(stringNumber, matchingCompound);
	}

	@Override
	public void readFromNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		if (nbtTagCompound.hasKey("Matching")) {
			final NBTTagCompound subCompound = nbtTagCompound.getCompoundTag("Matching");
			if (subCompound.hasKey(stringNumber)) {
				final NBTTagCompound matchingCompound = subCompound.getCompoundTag(stringNumber);
				final MatcherEnum matcherEnum = MatcherEnum.values()[matchingCompound.getInteger("matcherType")];
				final AbstractMatcher matcher = matcherEnum.getMatcher(this);
				matcher.readFromNBT(matchingCompound);
				this.matcher = matcher.validate();
			}
		}
	}

	@Nonnull
	@Override
	public Matching copy()
	{
		final Matching matching = new Matching(itemStacks, number);
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		matching.readFromNBT(nbtTagCompound);
		return matching;
	}

	@Nonnull
	@Override
	public String getControlName()
	{
		return "wanionlib.matching.control";
	}

	@Override
	public int hashCode()
	{
		return number;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj instanceof Matching) {
			final Matching matching = (Matching) obj;
			if (matching.number == this.number)
				return this.matcher.equals(matching.matcher);
			else return false;
		} else return false;
	}
}