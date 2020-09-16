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
import wanion.lib.client.gui.WGContainer;
import wanion.lib.client.gui.WGElement;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class WGInteraction
{
	public final WGContainer<?> WGContainer;
	public final EntityPlayer entityPlayer;
	private final int mouseX, mouseY;

	public WGInteraction(@Nonnull final WGContainer<?> WGContainer, final int mouseX, int mouseY)
	{
		this.WGContainer = WGContainer;
		this.entityPlayer = WGContainer.getEntityPlayer();
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	public WGInteraction(@Nonnull final WGContainer<?> WGContainer)
	{
		this.WGContainer = WGContainer;
		this.entityPlayer = WGContainer.getEntityPlayer();
		this.mouseX =  Mouse.getEventX() * this.WGContainer.width / this.WGContainer.mc.displayWidth;
		this.mouseY = this.WGContainer.height - Mouse.getEventY() * this.WGContainer.height / this.WGContainer.mc.displayHeight - 1;
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	public boolean isHovering(@Nonnull final WGElement wgElement)
	{
		return mouseX >= wgElement.getX() && mouseY >= wgElement.getY() && mouseX < wgElement.getX() + wgElement.getWidth() && mouseY < wgElement.getY() + wgElement.getHeight();
	}

	public boolean isHovering(final int x, final int y, final int width, final int height)
	{
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
	}
}