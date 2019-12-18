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

public final class ItemStackStrategy implements Hash.Strategy<ItemStack>
{
	@Override
	public int hashCode(final ItemStack o)
	{
		return MetaItem.get(o);
	}

	@Override
	public boolean equals(final ItemStack a, final ItemStack b)
	{
		return ItemStack.areItemStacksEqual(a, b);
	}
}