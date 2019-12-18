package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import it.unimi.dsi.fastutil.Hash;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

public final class ItemStackPairStrategy implements Hash.Strategy<Pair<ItemStack, ItemStack>>
{
	@Override
	public int hashCode(final Pair<ItemStack, ItemStack> o)
	{
		return MetaItem.get(o.getKey()) + MetaItem.get(o.getValue());
	}

	@Override
	public boolean equals(final Pair<ItemStack, ItemStack> a, final Pair<ItemStack, ItemStack> b)
	{
		final ItemStack aa = a.getKey(), ab = a.getValue(), ba = b.getKey(), bb = b.getValue();
		return (ItemStack.areItemStacksEqual(aa, ba) || ItemStack.areItemStacksEqual(aa, bb)) && (ItemStack.areItemStacksEqual(ab, ba) || ItemStack.areItemStacksEqual(ab, bb));
	}
}