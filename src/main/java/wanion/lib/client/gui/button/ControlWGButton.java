package wanion.lib.client.gui.button;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import wanion.lib.WanionLib;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.interaction.WGMouseInteraction;
import wanion.lib.common.control.IControlNameable;
import wanion.lib.common.control.IState;
import wanion.lib.common.control.IStateNameable;
import wanion.lib.common.control.IStateProvider;
import wanion.lib.network.SmartNBTSync;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ControlWGButton<C extends IStateProvider<C, S>, S extends IState<S>> extends WGButton
{
	protected final C stateProvider;
	protected final ResourceLocation resourceLocation;
	protected int lineWidth = 0;

	public ControlWGButton(@Nonnull final GuiContainer guiContainer, @Nonnull final EntityPlayer entityPlayer, @Nonnull final C stateProvider, @Nonnull final ResourceLocation resourceLocation, final int x, final int y)
	{
		this(guiContainer, entityPlayer, stateProvider, resourceLocation, x, y, 18, 18);
	}

	public ControlWGButton(@Nonnull final GuiContainer guiContainer, @Nonnull final EntityPlayer entityPlayer, @Nonnull final C stateProvider, @Nonnull final ResourceLocation resourceLocation, final int x, final int y, final int widthIn, final int heightIn)
	{
		super(guiContainer, entityPlayer, x,y, widthIn, heightIn);
		this.stateProvider = stateProvider;
		this.resourceLocation = resourceLocation;
	}

	@Override
	public void draw(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		if (!this.isEnabled())
			return;
		final S state = stateProvider.getState();
		boolean hovered = mouseInteraction.isHovering(x, y, width, height);
		final Pair<Integer, Integer> texturePos = state.getTexturePos(hovered);
		if (texturePos == null)
			return;
		getTextureManager().bindTexture(resourceLocation);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.drawModalRectWithCustomSizedTexture(x, y, texturePos.getLeft(), texturePos.getRight(), width, height, 128, 128);
	}

	@Override
	public void drawForegroundLayer(@Nonnull final WGMouseInteraction mouseInteraction)
	{
		final FontRenderer fontRenderer = getFontRenderer();
		final List<String> description = new ArrayList<>();
		final S state = stateProvider.getState();
		if (stateProvider instanceof IControlNameable)
			description.add(state instanceof IStateNameable ? TextFormatting.RED + I18n.format(((IControlNameable) stateProvider).getControlName()) + ": " + TextFormatting.WHITE + I18n.format(((IStateNameable) state).getStateName()) : TextFormatting.RED + I18n.format(((IControlNameable) stateProvider).getControlName()));
		if (state instanceof IStateNameable) {
			final String desc = ((IStateNameable) state).getStateDescription();
			if (desc != null)
				description.add(I18n.format(desc));
		}
		this.lineWidth = 0;
		for (final String line : description) {
			final int lineWidth = fontRenderer.getStringWidth(line);
			if (lineWidth > this.lineWidth)
				this.lineWidth = lineWidth;
		}
		guiContainer.drawHoveringText(description, getTooltipX(mouseInteraction), getTooltipY(mouseInteraction));
	}

	@Override
	public void interaction(@Nonnull final WGInteraction interaction)
	{
		if (!(interaction instanceof WGMouseInteraction))
			return;
		playPressSound(guiContainer.mc.getSoundHandler());
		final S state = stateProvider.getState();
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		stateProvider.writeToNBT(nbtTagCompound, ((WGMouseInteraction) interaction).getMouseButton() == 0 ? state.getNextState() : state.getPreviousState());
		WanionLib.networkWrapper.sendToServer(new SmartNBTSync(guiContainer.inventorySlots.windowId, nbtTagCompound));
	}
}