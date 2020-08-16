package wanion.lib.common.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import gnu.trove.map.hash.THashMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.ISmartNBTSync;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FieldController implements ISmartNBTSync, INBTMessage
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
		for (final IField<?> field : fields)
			add(field);
	}

	public FieldController(@Nonnull final IInventory inventory, @Nonnull final List<IField	<?>> matchingList)
	{
		this(inventory);
		matchingList.forEach(this::add);
	}

	public FieldController(@Nonnull final IInventory inventory, @Nonnull final Map<String, IField<?>> fieldMap)
	{
		this(inventory);
		fieldControlMap.putAll(fieldMap);
	}

	public void add(@Nonnull final IField<?> field)
	{
		fieldControlMap.put(field.getFieldName(), field);
	}

	public Collection<IField<?>> getInstances()
	{
		return values;
	}

	public IField<?> getField(@Nonnull final String nane)
	{
		return fieldControlMap.get(nane);
	}

	@Nonnull
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
	public void smartNBTSync(@Nonnull NBTTagCompound smartNBT)
	{
		final NBTTagCompound fieldNBT = smartNBT.getCompoundTag("field");
		if (fieldNBT.hasNoTags())
			return;
		fieldControlMap.values().forEach(field -> field.readFromNBT(fieldNBT));
		inventory.markDirty();
	}

	public NBTTagCompound formatFieldNBT(@Nonnull final IField<?> field)
	{
		final NBTTagCompound fieldNBT = new NBTTagCompound();
		fieldNBT.setString("fieldName", field.getFieldName());
		return fieldNBT;
	}

	@Override
	public void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		final String fieldName = nbtTagCompound.hasKey("fieldName") ? nbtTagCompound.getString("fieldName") : null;
		final IField<?> field = fieldName != null ? fieldControlMap.get(fieldName) : null;
		if (field != null)
			field.receiveNBT(nbtTagCompound.getCompoundTag(fieldName));
		inventory.markDirty();
	}
}