package wanion.lib.common.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import wanion.lib.common.IListenerProvider;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.ISmartNBT;

import javax.annotation.Nonnull;

public interface IFieldContainer extends IFieldControllerProvider, IListenerProvider, ISmartNBT, INBTMessage
{
	@Nonnull
	FieldController getContainerFieldController();
}