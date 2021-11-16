package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import wanion.lib.common.matching.AbstractMatching;

import javax.annotation.Nonnull;

public final class NbtMatcher extends AbstractMatcher<NbtMatcher>
{
	public NbtMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		super(matching);
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.NBT;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		return getStack().hasTagCompound() ? this : matching.getDefaultMatcher();
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> next()
	{
		return new OreDictMatcher(matching);
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		final ItemStack itemStack = getStack();
		return otherItemStack.hasTagCompound() && itemStack.isItemEqual(otherItemStack) && ItemStack.areItemStackTagsEqual(itemStack, otherItemStack);
	}

	@Nonnull
	@Override
	public NbtMatcher copy()
	{
		return new NbtMatcher(matching);
	}


	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof NbtMatcher;
	}

}