package wanion.lib.recipe.advanced;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;
import net.minecraft.inventory.InventoryCrafting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRecipeRegistry<R extends IAdvancedRecipe>
{
	public final TShortObjectMap<List<R>> recipeMap = new TShortObjectHashMap<>();

	public final void addRecipe(@Nonnull final R recipe)
	{
		final short recipeKey = recipe.getRecipeKey();
		if (!recipeMap.containsKey(recipeKey))
			recipeMap.put(recipeKey, new ArrayList<>());
		recipeMap.get(recipeKey).add(recipe);
	}

	public final void removeRecipe(@Nullable final R recipe)
	{
		if (recipe == null)
			return;
		final List<R> recipeList = recipeMap.get(recipe.getRecipeKey());
		if (recipeList != null)
			recipeList.remove(recipe);
	}

	public final R findMatchingRecipe(@Nonnull final InventoryCrafting inventoryCrafting)
	{
		final int root = (int) Math.sqrt(inventoryCrafting.getSizeInventory());
		int offSetX = 0, offSetY = 0, width = 0, height = 0;
		short recipeKey = 0, recipeSize = 0;
		boolean foundX = false, foundY = false;
		for (int x = 0; !foundX && x < root; x++) {
			for (int y = 0; !foundX && y < root; y++)
				if (inventoryCrafting.getStackInSlot(y * root + x) != null)
					foundX = true;
			if (foundX)
				offSetX = x;
		}
		for (int y = 0; !foundY && y < root; y++) {
			for (int x = 0; x < root; x++) {
				if (inventoryCrafting.getStackInSlot(y * root + x) != null)
					foundY = true;
				if (foundY)
					offSetY = y;
			}
		}
		for (int y = 0; y < root; y++) {
			int x = 0;
			while (true) {
				final int actualY = offSetY + y;
				if (actualY < root) {
					final int actualX = offSetX + x++;
					if (actualX < root) {
						if (inventoryCrafting.getStackInSlot(actualY * root + actualX) == null)
							continue;
						final int xDifference = actualX - (offSetX - 1);
						final int yDifference = actualY - (offSetY - 1);
						if (xDifference > width)
							width = xDifference;
						if (yDifference > height)
							height = yDifference;
					} else break;
				} else break;
			}
		}
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (inventoryCrafting.getStackInSlot((offSetY + y) * root + (offSetX + x)) != null)
					recipeSize++;
		final List<R> recipeList = recipeMap.containsKey((recipeKey |= recipeSize | (width << 8) | (height << 12))) ? recipeMap.get(recipeKey) : recipeMap.get(recipeSize);
		if (recipeList != null)
			for (R recipe : recipeList)
				if (recipe.recipeMatch(inventoryCrafting, offSetX, offSetY))
					return recipe;
		return null;
	}
}