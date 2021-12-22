package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import wanion.lib.common.matching.matcher.*;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public final class CraftTweakerHelper
{
	public static final String NULL = "null";

	private CraftTweakerHelper() {}

	public static String MatcherToCtFormat(@Nonnull final AbstractMatcher<?> matcher)
	{
		final ItemStack itemStack = matcher.getStack();
		if (matcher instanceof EmptyMatcher)
			return NULL;
		else if (matcher instanceof ItemStackMatcher) {
			final StringBuilder formatBuilder = new StringBuilder().append('<');
			formatBuilder.append(itemStack.getItem().getRegistryName());
			if (itemStack.getItemDamage() > 0)
				formatBuilder.append(':').append(itemStack.getItemDamage());
			formatBuilder.append('>');
			if (itemStack.getCount() > 1)
				formatBuilder.append(" * ").append(itemStack.getCount());
			return formatBuilder.toString();
		} else if (matcher instanceof AnyDamageMatcher) {
			final StringBuilder formatBuilder = new StringBuilder().append('<');
			formatBuilder.append(itemStack.getItem().getRegistryName()).append(":*>");
			if (itemStack.getCount() > 1)
				formatBuilder.append(" * ").append(itemStack.getCount());
			return formatBuilder.toString();
		} else if (matcher instanceof NbtMatcher) {
			final boolean greaterThanOne = itemStack.getCount() > 1;
			final StringBuilder formatBuilder = new StringBuilder();
			if (greaterThanOne)
				formatBuilder.append('(');
			formatBuilder.append('<').append(itemStack.getItem().getRegistryName());
			if (itemStack.getItemDamage() > 0)
				formatBuilder.append(':').append(itemStack.getItemDamage());
			formatBuilder.append('>');
			if (greaterThanOne)
				formatBuilder.append(" * ").append(itemStack.getCount()).append(')');
			final NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
			if (nbtTagCompound != null)
				formatBuilder.append(".withTag(").append(NBTUtils.formatNbt(nbtTagCompound)).append(')');
			return formatBuilder.toString();
		} else if (matcher instanceof OreDictMatcher) {
			return "<ore:" + OreDictionary.getOreName(((OreDictMatcher) matcher).getOre()) + ">";
		} else return NULL;
	}

	public static ItemStack toStack(final IItemStack item)
	{
		if (item == null)
			return null;
		final Object internal = item.getInternal();
		if (!(internal instanceof ItemStack))
			CraftTweakerAPI.getLogger().logError("Not a valid item stack: " + item);
		return internal instanceof ItemStack ? (ItemStack) internal : null;
	}

	public static Object[] toObjects(final IIngredient[] list)
	{
		if (list == null)
			return null;
		Object[] ingredients = new Object[list.length];
		for (int x = 0; x < list.length; x++) {
			ingredients[x] = toObject(list[x]);
		}
		return ingredients;
	}

	public static Object toActualObject(final IIngredient ingredient)
	{
		if (ingredient == null) return null;
		else {
			if (ingredient instanceof IOreDictEntry) {
				return OreDictionary.getOres(toString((IOreDictEntry) ingredient));
			} else if (ingredient instanceof IItemStack) {
				return toStack((IItemStack) ingredient);
			} else return null;
		}
	}

	public static Object toObject(final IIngredient ingredient)
	{
		if (ingredient == null)
			return null;
		if (ingredient instanceof IOreDictEntry) {
			return toString((IOreDictEntry) ingredient);
		} else if (ingredient instanceof IItemStack) {
			return toStack((IItemStack) ingredient);
		} else return null;
	}

	public static String toString(final IOreDictEntry entry)
	{
		return entry.getName();
	}
}