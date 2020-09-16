package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import com.google.common.collect.Lists;
import joptsimple.internal.Strings;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.Reference;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.common.control.energy.EnergyControl;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class EnergyElement extends WGElement
{
    protected final EnergyControl energyControl;

    public EnergyElement(@Nonnull final EnergyControl energyControl, @Nonnull final WGContainer<?> wgContainer, final int x, final int y)
    {
        super(wgContainer, x, y, 18, 54);
        this.energyControl = energyControl;
    }

    @Override
    public void draw(@Nonnull final WGInteraction wgInteraction)
    {
        wgContainer.mc.getTextureManager().bindTexture(Reference.GUI_TEXTURES);
        Gui.drawModalRectWithCustomSizedTexture(wgContainer.getGuiLeft() + x, wgContainer.getGuiTop() + y - 54, 0, 0, width, height, 128, 128);
        final int size = scalePowerCentage();
        if (size != 0)
            Gui.drawModalRectWithCustomSizedTexture(wgContainer.getGuiLeft() + x, wgContainer.getGuiTop() + y - size, 18, 54 - size, 18, size, 128, 128);
    }

    @Override
    public void drawForegroundLayer(@Nonnull final WGInteraction interaction)
    {
        if (interaction.isHovering(this))
            wgContainer.drawHoveringText(Lists.newArrayList(energyControl.getEnergyStored() + " / " + energyControl.getMaxEnergyStored() + " FE", Strings.EMPTY, TextFormatting.GOLD + I18n.format("wanionlib.consumes", energyControl.getEnergyUsage()), TextFormatting.GOLD + I18n.format("wanionlib.per.operation")), getTooltipX(interaction), getTooltipY(interaction));
    }

    @Override
    public boolean canInteractWith(@Nonnull final WGInteraction wgInteraction)
    {
        return false;
    }

    private int scalePowerCentage()
    {
        final int energyStored = energyControl.getEnergyStored();
        return energyStored != 0 ? (int) (54 * energyStored / (double) energyControl.getMaxEnergyStored()) : 0;
    }
}