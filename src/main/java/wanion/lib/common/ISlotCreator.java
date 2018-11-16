package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.inventory.Slot;

import javax.annotation.Nonnull;
import java.util.List;

public interface ISlotCreator
{
	void create(@Nonnull List<Slot> slotList);
}