package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.item.ItemStack;
import wanion.lib.client.gui.interaction.WInteraction;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface ITooltipSupplier
{
	List<String> getTooltip(@Nonnull final WInteraction interaction, @Nonnull final Supplier<ItemStack> stackSupplier);
}