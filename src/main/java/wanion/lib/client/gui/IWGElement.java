package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.interaction.WGMouseInteraction;

import javax.annotation.Nonnull;

// WG = Wanion GUI
public interface IWGElement
{
	int getX();

	int getY();

	int getWidth();

	int getHeight();

	@Nonnull
	EntityPlayer getEntityPlayer();

	@Nonnull
	GuiContainer getGuiContainer();

	boolean isEnabled();

	default void interaction(@Nonnull WGInteraction wgPlayer) {}

	void draw(@Nonnull WGMouseInteraction wgPlayer);

	default void drawForegroundLayer(@Nonnull WGMouseInteraction wgPlayer) {}

	default void setEnabled(boolean enabled) {}

	default void playPressSound(@Nonnull final SoundHandler soundHandler) {}

	default int getTooltipX(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		return mouseInteraction.getMouseX() - mouseInteraction.guiContainer.getGuiLeft();
	}

	default int getTooltipY(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		return mouseInteraction.getMouseX() - mouseInteraction.guiContainer.getGuiTop();
	}

	default TextureManager getTextureManager()
	{
		return Minecraft.getMinecraft().getTextureManager();
	}

	default FontRenderer getFontRenderer()
	{
		return Minecraft.getMinecraft().fontRenderer;
	}
}