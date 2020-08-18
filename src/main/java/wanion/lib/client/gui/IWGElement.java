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
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.interaction.WGMouseInteraction;

import javax.annotation.Nonnull;

// WG = Wanion GUI
public interface IWGElement
{
	int getX();

	default void setX(final int x) {}

	int getY();

	default void setY(final int y) {}

	int getWidth();

	int getHeight();

	@Nonnull
	EntityPlayer getEntityPlayer();

	@Nonnull
	GuiContainer getGuiContainer();

	boolean isEnabled();

	default void interaction(@Nonnull final WGInteraction wgPlayer) {}

	void draw(@Nonnull final WGMouseInteraction wgPlayer);

	default void drawForegroundLayer(@Nonnull final WGMouseInteraction wgPlayer) {}

	default void setEnabled(final boolean enabled) {}

	default int getTooltipX(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		return mouseInteraction.getMouseX() - mouseInteraction.guiContainer.getGuiLeft();
	}

	default int getTooltipY(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		return mouseInteraction.getMouseX() - mouseInteraction.guiContainer.getGuiTop();
	}

	default SoundHandler getSoundHandler()
	{
		return Minecraft.getMinecraft().getSoundHandler();
	}

	default TextureManager getTextureManager()
	{
		return Minecraft.getMinecraft().getTextureManager();
	}

	default FontRenderer getFontRenderer()
	{
		return Minecraft.getMinecraft().fontRenderer;
	}

	default void playPressSound()
	{
		getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}

	default void playPressSound(@Nonnull final ISound sound)
	{
		getSoundHandler().playSound(sound);
	}
}