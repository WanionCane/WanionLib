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

public final class DisabledMatcher extends AbstractMatcher<DisabledMatcher>
{
	public DisabledMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		super(matching);
	}

	@Nonnull
	@Override
	public DisabledMatcher copy()
	{
		return new DisabledMatcher(matching);
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.DISABLED;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		return this;
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		return false;
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj instanceof DisabledMatcher;
	}
}