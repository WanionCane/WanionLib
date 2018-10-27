package wanion.lib.recipe.advanced;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractShapelessAdvancedRecipe implements IAdvancedRecipe
{
	private final ItemStack output;
	private final short recipeSize;
	public final List<Object> inputs;

	public AbstractShapelessAdvancedRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs)
	{
		this.output = output.copy();
		short recipeSize = 0;
		final List<Object> temporaryInputs = new ArrayList<>();
		for (final Object input : inputs) {
			if (input instanceof ItemStack) {
				if (((ItemStack) input).isEmpty())
					continue;
				final ItemStack newInput = ((ItemStack) input).copy();
				newInput.setCount(1);
				temporaryInputs.add(newInput);
			} else if (input instanceof String) {
				final List<ItemStack> oreList = OreDictionary.getOres((String) input, false);
				if (oreList != null && !oreList.isEmpty())
					temporaryInputs.add(oreList);
				else
					continue;
			} else if (input instanceof List)
				if (!((List) input).isEmpty() && ((List) input).get(0) instanceof ItemStack)
					temporaryInputs.add(input);
				else
					continue;
			else
				continue;
			recipeSize++;
		}
		if (recipeSize == 0 || recipeSize > getMaxRecipeSize())
			throw new RuntimeException("Invalid " + getRecipeType());
		this.inputs = NonNullList.withSize(this.recipeSize = recipeSize, ItemStack.EMPTY);
		for (int i = 0; i < temporaryInputs.size(); i++)
			this.inputs.set(i, temporaryInputs	.get(i));
	}

	@Override
	public boolean isShaped()
	{
		return false;
	}

	@Override
	public short getRecipeKey()
	{
		return recipeSize;
	}

	@Override
	public short getRecipeSize()
	{
		return recipeSize;
	}

	@Override
	public int getWidth()
	{
		return 0;
	}

	@Override
	public int getHeight()
	{
		return 0;
	}

	@Nonnull
	@Override
	public List<Object> getInputs()
	{
		return inputs;
	}

	@Nonnull
	@Override
	public ItemStack getOutput()
	{
		return output.copy();
	}

	public abstract String getRecipeType();

	public abstract short getMaxRecipeSize();

	@SuppressWarnings("unchecked")
	@Override
	public boolean recipeMatches(@Nonnull final InventoryCrafting inventoryCrafting, final int offSetX, final int offSetY)
	{
		final List<Object> inputs = new ArrayList<>(this.inputs);
		final List<ItemStack> slotItemStacks = new ArrayList<>();
		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
			final ItemStack slotItemStack = inventoryCrafting.getStackInSlot(i);
			if (!slotItemStack.isEmpty())
				slotItemStacks.add(slotItemStack);
		}
		for (final Iterator<Object> inputsIterator = inputs.iterator(); inputsIterator.hasNext(); ) {
			final Object input = inputsIterator.next();
			boolean found = false;
			if (input instanceof ItemStack) {
				for (final Iterator<ItemStack> slotItemStackIterator = slotItemStacks.iterator(); slotItemStackIterator.hasNext(); ) {
					final ItemStack slotItemStack = slotItemStackIterator.next();
					if (slotItemStack.getItem() == ((ItemStack) input).getItem() && (!((ItemStack) input).getHasSubtypes() || ((ItemStack) input).getItemDamage() == slotItemStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(((ItemStack) input), slotItemStack)) {
						slotItemStackIterator.remove();
						found = true;
						break;
					}
				}
			} else if (input instanceof List) {
				final List<ItemStack> oreDict = (List<ItemStack>) input;
				for (final ItemStack entry : oreDict) {
					for (final Iterator<ItemStack> slotItemStackIterator = slotItemStacks.iterator(); slotItemStackIterator.hasNext(); ) {
						final ItemStack slotItemStack = slotItemStackIterator.next();
						if (entry.getItem() == slotItemStack.getItem() && (entry.getItemDamage() == slotItemStack.getItemDamage() || entry.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
							slotItemStackIterator.remove();
							found = true;
							break;
						}
					}
					if (found)
						break;
				}
			}
			if (found)
				inputsIterator.remove();
		}
		return inputs.isEmpty() && slotItemStacks.isEmpty();
	}
}