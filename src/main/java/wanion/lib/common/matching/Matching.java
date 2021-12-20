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
import wanion.lib.common.matching.matcher.AbstractMatcher;
import wanion.lib.common.matching.matcher.ItemStackMatcher;
import wanion.lib.common.matching.matcher.MatcherEnum;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class Matching extends AbstractMatching<Matching>
{
	protected final boolean shouldUseNbt;

	public Matching(@Nonnull final List<ItemStack> itemStacks, final int number)
	{
		this(itemStacks, number, false);
	}

	public Matching(@Nonnull final List<ItemStack> itemStacks, final int number, final boolean shouldUseNbt)
	{
		this(itemStacks, number, shouldUseNbt, null);
	}

	public Matching(@Nonnull final List<ItemStack> itemStacks, final int number, final boolean shouldUseNbt, final NBTTagCompound tagToRead)
	{
		this(() -> itemStacks.get(number), number, shouldUseNbt, tagToRead);
	}

	public Matching(@Nonnull final Supplier<ItemStack> stackSupplier, final int number, final boolean shouldUseNbt, final NBTTagCompound tagToRead)
	{
		super(stackSupplier, number, tagToRead);
		this.shouldUseNbt = shouldUseNbt;
	}

	public final boolean shouldUseNbt()
	{
		return shouldUseNbt;
	}

	@Override
	public void nextMatcher()
	{
		final ItemStack stack = getStack();
		MatcherEnum matcherEnum = matcher.getMatcherEnum();
		do {
			matcherEnum = matcherEnum.getNextMatcherEnum(matcher);
			if (!shouldUseNbt && matcherEnum == MatcherEnum.NBT)
				continue;
			if (matcherEnum.accepts(stack))
				break;
		} while (matcherEnum != MatcherEnum.ITEM_STACK);
		this.matcher = matcherEnum.getMatcher(this).validate();
	}

	@Override
	@Nonnull
	public Matching copy()
	{
		return new Matching(stackSupplier, number, shouldUseNbt, writeNBT());
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