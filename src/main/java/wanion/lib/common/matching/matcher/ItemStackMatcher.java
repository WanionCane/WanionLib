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
import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;

public final class ItemStackMatcher extends AbstractMatcher<ItemStackMatcher>
{
	public ItemStackMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		super(matching);
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.ITEM_STACK;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		return !getStack().isEmpty() ? this : new EmptyMatcher(matching);
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> next()
	{
		final ItemStack itemStack = getStack();
		if (itemStack.getHasSubtypes() || itemStack.isItemStackDamageable())
			return new AnyDamageMatcher(matching);
		else if (matching instanceof Matching && ((Matching) matching).shouldUseNbt() && itemStack.hasTagCompound())
			return new NbtMatcher(matching);
		else
			return new OreDictMatcher(matching);
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		return getStack().isItemEqual(otherItemStack);
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof ItemStackMatcher;
	}

	@Nonnull
	@Override
	public ItemStackMatcher copy()
	{
		return new ItemStackMatcher(matching);
	}
}