package wanion.lib.common.redstone;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import javax.annotation.Nonnull;

public interface IRedstoneControllable
{
	@Nonnull
	RedstoneControlState getRedstoneControlState();

	void setRedstoneControlState(@Nonnull final RedstoneControlState redstoneControlState);
}