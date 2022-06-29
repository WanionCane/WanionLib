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
import org.lwjgl.input.Keyboard;
import wanion.lib.client.gui.WGuiContainer;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public final class WKeyInteraction extends WInteraction
{
	private final char key;
	private final int keyCode;

	public WKeyInteraction(@Nonnull final WGuiContainer<?> wGuiContainer, final int mouseX, int mouseY, final char key, final int keyCode)
	{
		super(wGuiContainer, mouseX, mouseY);
		this.key = key;
		this.keyCode = keyCode;
	}

	public WKeyInteraction(@Nonnull final WGuiContainer<?> wGuiContainer, final char key, final int keyCode)
	{
		super(wGuiContainer);
		this.key = key;
		this.keyCode = keyCode;
	}

	public WKeyInteraction(@Nonnull final WGuiContainer<?> wGuiContainer)
	{
		super(wGuiContainer);
		this.key = Keyboard.getEventCharacter();
		this.keyCode = Keyboard.getEventKey();
	}

	public char getKey()
	{
		return key;
	}

	public int getKeyCode()
	{
		return keyCode;
	}

	@FunctionalInterface
	public interface IWKeyInteraction
	{
		void interact(@Nonnull final WKeyInteraction wKeyInteraction);
	}
}