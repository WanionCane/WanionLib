package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.control.IControlContainer;
import wanion.lib.common.field.FieldController;
import wanion.lib.common.field.IFieldContainer;
import wanion.lib.common.matching.IMatchingContainer;
import wanion.lib.common.matching.MatchingController;
import wanion.lib.network.NetworkHelper;

import javax.annotation.Nonnull;
import java.util.Collection;

public class WContainer<T extends WTileEntity> extends Container implements IControlContainer, IFieldContainer, IMatchingContainer
{
    private final Dependencies<IController<?, ?>> controllers = new Dependencies<>();
    private final T wTileEntity;
    private final boolean hasControls, hasFields, hasMatchings;

    public WContainer(@Nonnull final T wTileEntity)
    {
        this.wTileEntity = wTileEntity;
        if (hasControls = wTileEntity.hasControls())
            controllers.add(wTileEntity.getController(ControlController.class).copy());
        if (hasFields = wTileEntity.hasFields())
            controllers.add(wTileEntity.getController(FieldController.class).copy());
        if (hasMatchings = wTileEntity.hasMatchings())
            controllers.add(wTileEntity.getController(MatchingController.class).copy());
    }

    public final T getTile()
    {
        return wTileEntity;
    }

    public final String getTileName()
    {
        return wTileEntity.getName();
    }

    @Override
    public void addListener(@Nonnull final IContainerListener listener)
    {
        super.addListener(listener);
        if (!(listener instanceof EntityPlayerMP))
            return;
        if (hasControls)
            NetworkHelper.addControlListener(windowId, this, (EntityPlayerMP) listener);
        if (hasFields)
            NetworkHelper.addFieldListener(windowId, this, (EntityPlayerMP) listener);
        if (hasMatchings)
            NetworkHelper.addMatchingListener(windowId, this, (EntityPlayerMP) listener);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        if (hasControls)
            NetworkHelper.detectAndSendControlChanges(windowId, this);
        if (hasFields)
            NetworkHelper.detectAndSendFieldChanges(windowId, this);
        if (hasMatchings)
            NetworkHelper.detectAndSendMatchingChanges(windowId, this);
    }

    @Override
    public final boolean canInteractWith(@Nonnull final EntityPlayer playerIn)
    {
        return wTileEntity.isUsableByPlayer(playerIn);
    }

    @Nonnull
    @Override
    public final ControlController getContainerControlController()
    {
        return controllers.get(ControlController.class);
    }

    @Nonnull
    @Override
    public final FieldController getContainerFieldController()
    {
        return controllers.get(FieldController.class);
    }

    @Nonnull
    @Override
    public final MatchingController getContainerMatchingController()
    {
        return controllers.get(MatchingController.class);
    }

    @Override
    public final Collection<IContainerListener> getListeners()
    {
        return listeners;
    }

    @Override
    public final void readNBT(@Nonnull final NBTTagCompound smartNBT)
    {
        if (hasControls)
            getContainerControlController().readNBT(smartNBT);
        if (hasFields)
            getContainerFieldController().readNBT(smartNBT);
        if (hasMatchings)
            getContainerMatchingController().readNBT(smartNBT);
        wTileEntity.markDirty();
    }

    @Override
    public final void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        getContainerFieldController().readNBT(nbtTagCompound);
        wTileEntity.markDirty();
    }

    @Nonnull
    @Override
    public final ControlController getControlController()
    {
        return wTileEntity.getController(ControlController.class);
    }

    @Nonnull
    @Override
    public final FieldController getFieldController()
    {
        return wTileEntity.getController(FieldController.class);
    }

    @Nonnull
    @Override
    public final MatchingController getMatchingController()
    {
        return wTileEntity.getController(MatchingController.class);
    }
}