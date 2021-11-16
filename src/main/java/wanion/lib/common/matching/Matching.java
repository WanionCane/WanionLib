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

import javax.annotation.Nonnull;
import java.util.List;

public class Matching extends AbstractMatching<Matching>
{
	protected final boolean shouldUseNbt;

	public Matching(@Nonnull final List<ItemStack> itemStacks, final int number)
	{
		super(itemStacks, number);
		this.shouldUseNbt = false;
	}

	public Matching(@Nonnull final List<ItemStack> itemStacks, final int number, final NBTTagCompound tagToRead, final boolean shouldUseNbt)
	{
		super(itemStacks, number, tagToRead);
		this.shouldUseNbt = shouldUseNbt;
	}

	public final boolean shouldUseNbt()
	{
		return shouldUseNbt;
	}

	@Override
	@Nonnull
	public AbstractMatcher<?> getDefaultMatcher()
	{
		return new ItemStackMatcher(this).validate();
	}

	@Override
	public void nextMatcher()
	{
		this.matcher = matcher.next().validate();
	}

	@Override
	@Nonnull
	public Matching copy()
	{
		return new Matching(itemStacks, number, writeNBT(), shouldUseNbt);
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