package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import wanion.lib.common.control.IControlInventory;
import wanion.lib.common.field.IFieldInventory;
import wanion.lib.common.matching.IMatchingInventory;

public interface IControlFieldMatchingInventory extends IControlInventory, IFieldInventory, IMatchingInventory {}