package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import wanion.lib.common.Util;
import wanion.lib.common.matching.AbstractMatching;

import javax.annotation.Nonnull;

public final class VanillaMatcher extends AbstractMatcher<VanillaMatcher>
{
	public VanillaMatcher(@Nonnull final AbstractMatching<?> matching)
	{
		super(matching);
	}

	@Override
	@Nonnull
	public VanillaMatcher copy()
	{
		return new VanillaMatcher(matching);
	}

	@Nonnull
	@Override
	public MatcherEnum getMatcherEnum()
	{
		return MatcherEnum.MOD;
	}

	@Nonnull
	@Override
	public AbstractMatcher<?> validate()
	{
		return Util.isFromVanilla(getStack()) ? this : matching.getDefaultMatcher();
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		final ItemStack stack = getStack();
		return !stack.isEmpty() && !otherItemStack.isEmpty() && Util.areFromSameMod(stack, otherItemStack);
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj == this || (obj instanceof VanillaMatcher && Util.areFromSameMod(getStack(), ((VanillaMatcher) obj).getStack()));
	}
}