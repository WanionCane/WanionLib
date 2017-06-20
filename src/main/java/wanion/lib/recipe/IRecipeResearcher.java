package wanion.lib.recipe;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public interface IRecipeResearcher<S extends IRecipe, L extends IRecipe>
{
	int getShapedRecipeKey(@Nonnull final S recipe);

	int getShapelessRecipeKey(@Nonnull final L recipe);

	@Nonnull
	List<Class<? extends S>> getShapedRecipeClasses();

	@Nonnull
	List<Class<? extends L>> getShapelessRecipeClasses();

	ShapedOreRecipe getNewShapedRecipe(@Nonnull final S recipe);

	ShapedOreRecipe getNewShapedFromShapelessRecipe(@Nonnull L recipe);

	ShapelessOreRecipe getNewShapelessRecipe(@Nonnull final L recipe);

	ShapelessOreRecipe getNewShapelessFromShapedRecipe(@Nonnull final S recipe);
}