package wanion.lib.client.button;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.GuiButton;

public abstract class ControlButton extends GuiButton
{
	public ControlButton(final int buttonId, final int x, final int y, final String buttonText)
	{
		this(buttonId, x, y, 18, 18, buttonText);
	}

	public ControlButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText)
	{
		super(buttonId, x, y ,widthIn, heightIn, buttonText);
	}

	public abstract void controlAction(final boolean leftClick);
}