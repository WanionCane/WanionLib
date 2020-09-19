package wanion.lib.client.gui.interaction;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.WElement;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class WGInteraction
{
	private final WGuiContainer<?> wGuiContainer;
	private final EntityPlayer entityPlayer;
	private final int mouseX, mouseY;
	private boolean proceed = true;

	public WGInteraction(@Nonnull final WGuiContainer<?> wGuiContainer)
	{
		this(wGuiContainer,Mouse.getEventX() * wGuiContainer.width / wGuiContainer.mc.displayWidth,  wGuiContainer.height - Mouse.getEventY() * wGuiContainer.height / wGuiContainer.mc.displayHeight - 1);
	}

	public WGInteraction(@Nonnull final WGuiContainer<?> wGuiContainer, final int mouseX, int mouseY)
	{
		this.wGuiContainer = wGuiContainer;
		this.entityPlayer = wGuiContainer.getEntityPlayer();
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	@Nonnull
	public WGuiContainer<?> getContainer()
	{
		return wGuiContainer;
	}

	@Nonnull
	public EntityPlayer getEntityPlayer()
	{
		return entityPlayer;
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	public boolean isHovering(@Nonnull final WElement wElement)
	{
		return mouseX >= wElement.getX() && mouseY >= wElement.getY() && mouseX < wElement.getX() + wElement.getWidth() && mouseY < wElement.getY() + wElement.getHeight();
	}

	public boolean isHovering(final int x, final int y, final int width, final int height)
	{
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
	}

	public final void notProceed()
	{
		proceed = false;
	}

	public final boolean proceed()
	{
		return proceed;
	}
}