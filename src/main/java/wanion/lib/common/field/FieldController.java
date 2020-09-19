package wanion.lib.common.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import wanion.lib.common.IController;
import wanion.lib.common.ICopyable;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.WContainer;
import wanion.lib.network.NetworkHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldController implements IController<FieldController, IField<?>>, ICopyable<FieldController>, INBTMessage
{
	private final Map<String, IField<?>> fieldControlMap = new THashMap<>();
	private final Collection<IField<?>> values = fieldControlMap.values();
	private final IInventory inventory;

	public FieldController(@Nonnull final IInventory inventory)
	{
		this.inventory = inventory;
	}

	public FieldController(@Nonnull final IInventory inventory, final IField<?>... fields)
	{
		this(inventory);
		add(fields);
	}

	public FieldController(@Nonnull final IInventory inventory, @Nonnull final List<IField<?>> fieldList)
	{
		this(inventory);
		fieldList.forEach(this::add);
	}

	public FieldController(@Nonnull final IInventory inventory, @Nonnull final Map<String, IField<?>> fieldMap)
	{
		this(inventory);
		fieldControlMap.putAll(fieldMap);
	}

	public void add(@Nonnull final IField<?>... fields)
	{
		for (final IField<?> field : fields)
			fieldControlMap.put(field.getFieldName(), field);
	}

	public Collection<IField<?>> getInstances()
	{
		return values;
	}

	public IField<?> getField(@Nonnull final String name)
	{
		return fieldControlMap.get(name);
	}

	@Nonnull
	@Override
	public List<IField<?>> compareContents(@Nonnull final FieldController otherFieldController)
	{
		final List<IField<?>> differences = new ArrayList<>();
		for (final IField<?> field : values) {
			final IField<?> otherField = otherFieldController.fieldControlMap.get(field.getFieldName());
			if (!field.equals(otherField))
				differences.add(field);
		}
		return differences;
	}

	@Override
	public void addListener(final int windowId,  @Nonnull final WContainer<?> wContainer, @Nonnull final EntityPlayerMP entityPlayerMP)
	{
		NetworkHelper.addFieldListener(windowId, wContainer, entityPlayerMP);
	}

	@Override
	public void detectAndSendChanges(final int windowId, @Nonnull final WContainer<?> wContainer)
	{
		NetworkHelper.detectAndSendFieldChanges(windowId, wContainer);
	}

	@Nonnull
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound fieldNBT = new NBTTagCompound();
		final NBTTagList fieldTagList = new NBTTagList();
		fieldNBT.setTag("field", fieldTagList);
		values.forEach(field -> fieldTagList.appendTag(field.writeNBT()));
		return fieldNBT;
	}

	@Override
	public void afterWriteNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		final NBTTagList fieldTagList = smartNBT.getTagList("field", 10);
		if (fieldTagList.hasNoTags())
			return;
		for (int i = 0; i < fieldTagList.tagCount(); i++) {
			final NBTTagCompound fieldTag = fieldTagList.getCompoundTagAt(i);
			final IField<?> field = fieldControlMap.get(fieldTag.getString("fieldName"));
			if (field != null)
				field.afterWriteNBT(fieldTag);
		}
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		final NBTTagList fieldTagList = smartNBT.getTagList("field", 10);
		if (fieldTagList.hasNoTags())
			return;
		for (int i = 0; i < fieldTagList.tagCount(); i++) {
			final NBTTagCompound fieldTag = fieldTagList.getCompoundTagAt(i);
			final IField<?> field = fieldControlMap.get(fieldTag.getString("fieldName"));
			if (field != null)
				field.readNBT(fieldTag);
		}
		inventory.markDirty();
	}

	@Nonnull
	@Override
	public FieldController copy()
	{
		return new FieldController(inventory, values.stream().<IField<?>>map(IField::copy).collect(Collectors.toList()));
	}

	@Override

	public void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		final String fieldName = nbtTagCompound.hasKey("fieldName") ? nbtTagCompound.getString("fieldName") : null;
		final IField<?> field = fieldName != null ? fieldControlMap.get(fieldName) : null;
		if (field != null) {
			field.receiveNBT(nbtTagCompound.getCompoundTag(fieldName));
			inventory.markDirty();
		}
	}


}