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
import wanion.lib.common.ICopyable;
import wanion.lib.common.ISmartNBT;
import wanion.lib.common.control.IControlNameable;
import wanion.lib.common.matching.matcher.AbstractMatcher;
import wanion.lib.common.matching.matcher.ItemStackMatcher;
import wanion.lib.common.matching.matcher.MatcherEnum;

import javax.annotation.Nonnull;
import java.util.List;

public class Matching implements ISmartNBT, ICopyable<Matching>, IControlNameable
{
	private final List<ItemStack> itemStacks;
	private final int number;
	private final String stringNumber;
	private final boolean shouldUseNbt;
	private AbstractMatcher<?> matcher = new ItemStackMatcher(this);

	public Matching(@Nonnull final List<ItemStack> itemStacks, final int number)
	{
		this(itemStacks, number, false);
	}

	public Matching(@Nonnull final List<ItemStack> itemStacks, final int number, final boolean shouldUseNbt)
	{
		this.itemStacks = itemStacks;
		this.stringNumber = Integer.toString(this.number = number);
		this.shouldUseNbt = shouldUseNbt;
	}

	public Matching(@Nonnull final List<ItemStack> itemStacks, final int number, final boolean shouldUseNbt, @Nonnull NBTTagCompound tagToRead)
	{
		this.itemStacks = itemStacks;
		this.stringNumber = Integer.toString(this.number = number);
		this.shouldUseNbt = shouldUseNbt;
		readNBT(tagToRead);
	}

	public void resetMatcher()
	{
		this.matcher = new ItemStackMatcher(this);
	}

	public void nextMatcher()
	{
		this.matcher = matcher.next();
	}

	public AbstractMatcher<?> getMatcher()
	{
		return matcher;
	}

	public void setMatcher(@Nonnull final AbstractMatcher<?> matcher)
	{
		this.matcher = matcher.validate();
	}

	public ItemStack getStack()
	{
		return itemStacks.get(number);
	}

	public boolean shouldUseNbt()
	{
		return shouldUseNbt;
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound matcherNBT = new NBTTagCompound();
		matcherNBT.setInteger("number", number);
		matcherNBT.setInteger("matcherType", matcher.getMatcherEnum().ordinal());
		matcherNBT.setTag("matcher", matcher.writeNBT());
		return matcherNBT;
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		final MatcherEnum matcherEnum = MatcherEnum.values()[nbtTagCompound.getInteger("matcherType")];
		final AbstractMatcher<?> matcher = matcherEnum.getMatcher(this);
		matcher.readNBT(nbtTagCompound.getCompoundTag("matcher"));
		setMatcher(matcher);
	}

	@Nonnull
	@Override
	public Matching copy()
	{
		return new Matching(itemStacks, number, shouldUseNbt, writeNBT());
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