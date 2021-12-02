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

public final class DamagedMatcher extends AbstractMatcher<DamagedMatcher>
{
	public DamagedMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		super(matching);
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.DAMAGED;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		return getStack().isItemDamaged() ? this : matching.getDefaultMatcher();
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		final ItemStack itemStack = getStack();
		return !itemStack.isEmpty() && itemStack.getItem() == otherItemStack.getItem() && itemStack.isItemDamaged() && otherItemStack.isItemDamaged();
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof DamagedMatcher;
	}

	@Nonnull
	@Override
	public DamagedMatcher copy()
	{
		return new DamagedMatcher(matching);
	}
}