package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.interaction.WGKeyInteraction;
import wanion.lib.client.gui.interaction.WGMouseInteraction;

import javax.annotation.Nonnull;

// W = Wanion
@SideOnly(Side.CLIENT)
public abstract class WElement
{
	protected final WGuiContainer<?> wGuiContainer;
	protected final int width, height;
	protected int x, y;
	protected boolean enabled = true;

	public WElement(@Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height)
	{
		this.wGuiContainer = wGuiContainer;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public final int getX() {
		return x;
	}

	public final int getUsableX() {
		return wGuiContainer.getGuiLeft() + x;
	}

	public final void setX(final int x) {
		this.x = x;
	}

	public final int getY() {
		return y;
	}

	public final int getUsableY()
	{
		return wGuiContainer.getGuiTop() + y;
	}

	public final void setY(final int y)
	{
		this.y = y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public final int getWindowID()
	{
		return wGuiContainer.inventorySlots.windowId;
	}

	@Nonnull
	public final WGuiContainer<?> getWGuiContainer()
	{
		return wGuiContainer;
	}

	@Nonnull
	public final EntityPlayer getEntityPlayer()
	{
		return wGuiContainer.getEntityPlayer();
	}

	public final boolean isEnabled()
	{
		return enabled;
	}

	// disabling will make it disappear on screen, and no interaction call will be send to it.
	public final void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean canInteractWith(@Nonnull final WGInteraction wgInteraction)
	{
		return wgInteraction.isHovering(this);
	}

	public void interaction(@Nonnull final WGInteraction wgInteraction)
	{
		if (wgInteraction instanceof WGKeyInteraction)
			interaction((WGKeyInteraction) wgInteraction);
		else if (wgInteraction instanceof WGMouseInteraction)
			interaction((WGMouseInteraction) wgInteraction);
	}

	public void interaction(@Nonnull final WGKeyInteraction wgKeyInteraction) {}

	public void interaction(@Nonnull final WGMouseInteraction wgMouseInteraction) {}

	public abstract void draw(@Nonnull final WGInteraction wgInteraction);

	public void drawForeground(@Nonnull final WGInteraction wgPlayer) {}

	public int getTooltipX(@Nonnull final WGInteraction wgInteraction)
	{
		return wgInteraction.getMouseX() - wGuiContainer.getGuiLeft();
	}

	public int getTooltipY(@Nonnull final WGInteraction wgInteraction)
	{
		return wgInteraction.getMouseY() - wGuiContainer.getGuiTop();
	}

	public SoundHandler getSoundHandler()
	{
		return Minecraft.getMinecraft().getSoundHandler();
	}

	public TextureManager getTextureManager()
	{
		return Minecraft.getMinecraft().getTextureManager();
	}

	public FontRenderer getFontRenderer()
	{
		return Minecraft.getMinecraft().fontRenderer;
	}

	public void playPressSound()
	{
		getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	public void playPressSound(@Nonnull final ISound sound)
	{
		getSoundHandler().playSound(sound);
	}
}