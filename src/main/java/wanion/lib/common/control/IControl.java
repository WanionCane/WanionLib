package wanion.lib.common.control;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import wanion.lib.common.ICopyable;
import wanion.lib.common.ISmartNBT;

public interface IControl<C extends IControl<C>> extends ISmartNBT, ICopyable<C>
{
	default boolean canOperate()
	{
		return true;
	}

	default void operate() {}
}