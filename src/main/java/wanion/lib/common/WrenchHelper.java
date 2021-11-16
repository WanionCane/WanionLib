package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public final class WrenchHelper
{
	public static final WrenchHelper INSTANCE = new WrenchHelper();
	public final HashSet<Item> wrenches = new HashSet<>();

	private WrenchHelper()
	{
		final List<Item> wrenchList = Lists.newArrayList(
				Item.getByNameOrId("thermalfoundation:wrench"),					Item.getByNameOrId("appliedenergistics2:certus_quartz_wrench"),
				Item.getByNameOrId("appliedenergistics2:nether_quartz_wrench"), Item.getByNameOrId("enderio:item_yeta_wrench"),
				Item.getByNameOrId("ic2:wrench"), 								Item.getByNameOrId("ic2:electric_wrench"),
				Item.getByNameOrId("redstonearsenal:tool.wrench_flux"),			Item.getByNameOrId("redstonearsenal:tool.battlewrench_flux"),
				Item.getByNameOrId("pneumaticcraft:pneumatic_wrench")
		);
		wrenchList.stream().filter(Objects::nonNull).forEach(wrenches::add);
	}

	public boolean isWrench(@Nonnull final ItemStack stack)
	{
		return wrenches.contains(stack.getItem());
	}
}