package wanion.lib.recipe;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import gnu.trove.map.TObjectCharMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TObjectCharHashMap;
import net.minecraft.item.ItemStack;
import wanion.lib.common.MetaItem;

import javax.annotation.Nonnull;
import java.util.Map;

public final class RecipeHelper
{
	private RecipeHelper() {}

	@Nonnull
	public static Object[] rawShapeToShape(@Nonnull final Object[] objects)
	{
		return rawShapeToShape(objects, 3);
	}

	@Nonnull
	public static Object[] rawShapeToShape(@Nonnull final Object[] objects, final int squareRoot)
	{
		int f = 65;
		final char[][] almostTheShape = new char[squareRoot][squareRoot];
		final TObjectCharMap<Object> thingToCharMap = new TObjectCharHashMap<>();
		final Map<Integer, ItemStack> keyStackMap = new THashMap<>();
		boolean done = false;
		for (int y = 0; y < squareRoot && !done; y++) {
			for (int x = 0; x < squareRoot && !done; x++) {
				final int index = y * squareRoot + x;
				if (done = !(index < objects.length))
					continue;
				final boolean isItemStack = objects[index] instanceof ItemStack;
				final Object key = isItemStack ? MetaItem.get((ItemStack) objects[index]) : objects[index];
				if (key == null || (isItemStack && ((ItemStack) objects[index]).isEmpty())) {
					almostTheShape[x][y] = ' ';
					continue;
				} else if (key instanceof Integer)
					keyStackMap.put((Integer) key, (ItemStack) objects[index]);
				if (thingToCharMap.containsKey(key))
					almostTheShape[x][y] = thingToCharMap.get(key);
				else
					thingToCharMap.put(key, almostTheShape[x][y] = (char) f++);
			}
		}
		final Object[] shape = new Object[squareRoot + thingToCharMap.size() * 2];
		for (int i = 0; i < squareRoot; i++)
			shape[i] = new String(almostTheShape[i]);
		int i = 0;
		for (final Object object : thingToCharMap.keySet()) {
			shape[squareRoot + (2 * i)] = thingToCharMap.get(object);
			shape[squareRoot + 1 + (2 * i++)] = (object instanceof Integer) ? keyStackMap.get(object) : object;
		}
		return shape;
	}
}