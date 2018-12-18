package wanion.lib.common.matching.matcher;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import wanion.lib.common.matching.Matching;

import javax.annotation.Nonnull;

public class ItemStackMatcher extends AbstractMatcher
{
	public ItemStackMatcher(@Nonnull final Matching matching)
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
	public AbstractMatcher validate()
	{
		return this;
	}

	@Nonnull
	@Override
	public AbstractMatcher next()
	{
		final ItemStack itemStack = getStack();
		if (!itemStack.getHasSubtypes() && !itemStack.isItemStackDamageable()) {
			final int[] ores = OreDictionary.getOreIDs(itemStack);
			return ores.length == 0 ? this : new OreDictMatcher(matching);
		} else if (itemStack.getHasSubtypes() || itemStack.isItemStackDamageable())
			return new AnyDamageMatcher(matching);
		else if (matching.shouldUseNbt() && itemStack.hasTagCompound())
			return null;
		else return this;
	}

	@Override
	public boolean matches(@Nonnull final ItemStack otherItemStack)
	{
		return getStack().isItemEqual(otherItemStack);
	}

	@Nonnull
	@Override
	public String format()
	{
		final ItemStack itemStack = getStack();
		return itemStack.getItemDamage() > 0 ? "<" + itemStack.getItem().getRegistryName() + ":" + itemStack.getItemDamage() + ">" : "<" + itemStack.getItem().getRegistryName() + ">";
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj == this || obj instanceof ItemStackMatcher;
	}
}