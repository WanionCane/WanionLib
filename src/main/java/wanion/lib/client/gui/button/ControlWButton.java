package wanion.lib.client.gui.button;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import wanion.lib.WanionLib;
import wanion.lib.client.gui.ITooltipSupplier;
import wanion.lib.client.gui.WGuiContainer;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.client.gui.interaction.WMouseInteraction;
import wanion.lib.common.control.IControlNameable;
import wanion.lib.common.control.IState;
import wanion.lib.common.control.IStateNameable;
import wanion.lib.common.control.IStateProvider;
import wanion.lib.network.SmartNBTMessage;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public class ControlWButton<C extends IStateProvider<C, S>, S extends IState<S>> extends WButton<ControlWButton<C, S>>
{
	protected final C stateProvider;
	protected int lineWidth = 0;

	public ControlWButton(@Nonnull final C stateProvider, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
	{
		this(stateProvider, wGuiContainer, x, y, 18, 18);
	}

	public ControlWButton(@Nonnull final C stateProvider, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y, final int width, final int height)
	{
		super(wGuiContainer, x,y, width, height);
		this.stateProvider = stateProvider;
		setTooltipSupplier(new ControlWButtonTooltipSupplier());
	}

	@Override
	public void draw(@Nonnull final WInteraction wInteraction)
	{
		final S state = stateProvider.getState();
		final ResourceLocation textureResourceLocation = state.getTextureResourceLocation();
		final Pair<Integer, Integer> texturePos = state.getTexturePos(wInteraction.isHovering(this));
		if (textureResourceLocation == null || texturePos == null)
			return;
		getTextureManager().bindTexture(textureResourceLocation);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.drawModalRectWithCustomSizedTexture(getUsableX(), getUsableY(), texturePos.getLeft(), texturePos.getRight(), width, height, 128, 128);
	}

	@Override
	public void interaction(@Nonnull final WMouseInteraction mouseInteraction)
	{
		final S state = stateProvider.getState();
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		final NBTTagList controlTagList = new NBTTagList();
		final NBTTagCompound controlNBT = new NBTTagCompound();
		stateProvider.writeToNBT(controlNBT, mouseInteraction.getMouseButton() == 0 ? state.getNextState() : state.getPreviousState());
		controlTagList.appendTag(controlNBT);
		nbtTagCompound.setTag("control", controlTagList);
		WanionLib.networkWrapper.sendToServer(new SmartNBTMessage(getWindowID(), nbtTagCompound));
		playPressSound();
	}

	private class ControlWButtonTooltipSupplier implements ITooltipSupplier
	{
		@Override
		public List<String> getTooltip(@Nonnull final WInteraction interaction, @Nonnull final Supplier<ItemStack> stackSupplier)
		{
			final List<String> description = new ArrayList<>();
			final S state = stateProvider.getState();
			if (stateProvider instanceof IControlNameable)
				description.add(state instanceof IStateNameable ? TextFormatting.RED + I18n.format(((IControlNameable) stateProvider).getControlName()) + ": " + TextFormatting.WHITE + I18n.format(((IStateNameable) state).getStateName()) : TextFormatting.RED + I18n.format(((IControlNameable) stateProvider).getControlName()));
			if (state instanceof IStateNameable) {
				final String desc = ((IStateNameable) state).getStateDescription();
				if (desc != null)
					description.add(I18n.format(desc));
			}
			lineWidth = 0;
			for (final String line : description) {
				final int localLineWidth = getFontRenderer().getStringWidth(line);
				if (localLineWidth > lineWidth)
					lineWidth = localLineWidth;
			}
			return description;
		}
	}
}