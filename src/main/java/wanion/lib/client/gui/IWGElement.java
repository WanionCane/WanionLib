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

import javax.annotation.Nonnull;

// WG == Wanion GUI
public interface IWGElement
{
	int getX();

	int getY();

	int getWidth();

	int getHeight();

	boolean enabled();

	void interact(@Nonnull WGPlayer wgPlayer);

	void draw(@Nonnull WGPlayer wgPlayer);

	default void drawForegroundLayer(@Nonnull WGPlayer wgPlayer) {}

	default void setEnabled(boolean enabled) {}

	default void playPressSound(@Nonnull final SoundHandler soundHandler) {}

	default int getTooltipX(@Nonnull final GuiContainer guiContainer, final int mouseX)
	{
		return mouseX - guiContainer.getGuiLeft();
	}

	default int getTooltipY(@Nonnull final GuiContainer guiContainer, final int mouseY)
	{
		return mouseY - guiContainer.getGuiTop();
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