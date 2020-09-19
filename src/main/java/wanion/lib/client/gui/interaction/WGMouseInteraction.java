package wanion.lib.client.gui.interaction;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import wanion.lib.client.gui.WGuiContainer;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class WGMouseInteraction extends WGInteraction
{
	private final int mouseButton;

	public WGMouseInteraction(@Nonnull final WGuiContainer<?> wGuiContainer, final int mouseX, int mouseY, int mouseButton)
	{
		super(wGuiContainer, mouseX, mouseY);
		this.mouseButton = mouseButton;
	}

	public WGMouseInteraction(@Nonnull final WGuiContainer<?> wGuiContainer)
	{
		super(wGuiContainer);
		this.mouseButton = Mouse.getEventButton();
	}

	public int getMouseButton()
	{
		return mouseButton;
	}
}