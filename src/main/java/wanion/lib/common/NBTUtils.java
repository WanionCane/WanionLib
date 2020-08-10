package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import gnu.trove.iterator.TByteIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TByteList;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.nbt.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class NBTUtils
{
	private static final String NEW_LINE = System.lineSeparator();
	private static final char SPACE = ' ';

	private NBTUtils() {}

	@Nonnull
	public static String formatNbt(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		final List<String> keyList = new ArrayList<>(nbtTagCompound.getKeySet());
		Collections.sort(keyList);
		final StringBuilder nbtBuilder = new StringBuilder().append('{');
		for (final Iterator<String> keyIterator = keyList.iterator(); keyIterator.hasNext(); ) {
			final String key = keyIterator.next();
			final String formattedBase = formatNbtBase(nbtTagCompound.getTag(key));
			if (formattedBase == null)
				continue;
			final int lastNewLine = nbtBuilder.lastIndexOf(NEW_LINE);
			final int difference = lastNewLine == -1 ? nbtBuilder.length() : nbtBuilder.length() - lastNewLine;
			if (difference > 320) {
				if (nbtBuilder.charAt(nbtBuilder.length() - 1) == SPACE)
					nbtBuilder.deleteCharAt(nbtBuilder.length() - 1);
				nbtBuilder.append(NEW_LINE);
			}
			nbtBuilder.append(key).append(": ").append(formattedBase);
			if (keyIterator.hasNext())
				nbtBuilder.append(", ");
		}
		nbtBuilder.append('}');
		return nbtBuilder.toString();
	}

	private static String formatNbtBase(@Nonnull final NBTBase nbtBase)
	{
		if (nbtBase instanceof NBTTagCompound)
			return formatNbt((NBTTagCompound) nbtBase);
		else if (nbtBase instanceof NBTTagString)
			return "\"" + ((NBTTagString) nbtBase).getString() + "\"";
		else if (nbtBase instanceof NBTTagFloat)
			return Float.toString(((NBTTagFloat) nbtBase).getFloat());
		else if (nbtBase instanceof NBTTagDouble)
			return Double.toString(((NBTTagDouble) nbtBase).getDouble());
		else if (nbtBase instanceof NBTPrimitive)
			return Long.toString(((NBTPrimitive) nbtBase).getLong());
		else if (nbtBase instanceof NBTTagByteArray) {
			final TByteList byteList = new TByteArrayList(((NBTTagByteArray) nbtBase).getByteArray());
			final StringBuilder byteStringBuilder = new StringBuilder().append('[');
			for (final TByteIterator byteIterator = byteList.iterator(); byteIterator.hasNext(); ) {
				final int lastNewLine = byteStringBuilder.lastIndexOf(NEW_LINE);
				final int difference = lastNewLine == -1 ? byteStringBuilder.length() : byteStringBuilder.length() - lastNewLine;
				if (difference > 320) {
					if (byteStringBuilder.charAt(byteStringBuilder.length() - 1) == SPACE)
						byteStringBuilder.deleteCharAt(byteStringBuilder.length() - 1);
					byteStringBuilder.append(NEW_LINE);
				}
				byteStringBuilder.append(byteIterator.next());
				if (byteIterator.hasNext())
					byteStringBuilder.append(", ");
			}
			byteStringBuilder.append(']');
			return byteStringBuilder.toString();
		} else if (nbtBase instanceof NBTTagIntArray) {
			final TIntList intList = new TIntArrayList(((NBTTagIntArray) nbtBase).getIntArray());
			final StringBuilder intStringBuilder = new StringBuilder().append('[');
			for (final TIntIterator intIterator = intList.iterator(); intIterator.hasNext(); ) {
				final int lastNewLine = intStringBuilder.lastIndexOf(NEW_LINE);
				final int difference = lastNewLine == -1 ? intStringBuilder.length() : intStringBuilder.length() - lastNewLine;
				if (difference > 320) {
					if (intStringBuilder.charAt(intStringBuilder.length() - 1) == SPACE)
						intStringBuilder.deleteCharAt(intStringBuilder.length() - 1);
					intStringBuilder.append(NEW_LINE);
				}
				intStringBuilder.append(intIterator.next());
				if (intIterator.hasNext())
					intStringBuilder.append(", ");
			}
			intStringBuilder.append(']');
			return intStringBuilder.toString();
		} else if (nbtBase instanceof NBTTagList) {
			final NBTTagList nbtTagList = (NBTTagList) nbtBase;
			final StringBuilder nbtListStringBuilder = new StringBuilder().append('[');
			if (nbtTagList.tagCount() > 0) {
				for (final Iterator<NBTBase> nbtBaseIterator = nbtTagList.iterator(); nbtBaseIterator.hasNext(); ) {
					final String fb = formatNbtBase(nbtBaseIterator.next());
					if (fb == null)
						continue;
					final int lastNewLine = nbtListStringBuilder.lastIndexOf(NEW_LINE);
					final int difference = lastNewLine == -1 ? nbtListStringBuilder.length() : nbtListStringBuilder.length() - lastNewLine;
					if (difference > 320) {
						if (nbtListStringBuilder.charAt(nbtListStringBuilder.length() - 1) == SPACE)
							nbtListStringBuilder.deleteCharAt(nbtListStringBuilder.length() - 1);
						nbtListStringBuilder.append(NEW_LINE);
					}
					nbtListStringBuilder.append(fb);
					if (nbtBaseIterator.hasNext())
						nbtListStringBuilder.append(", ");
				}
			}
			nbtListStringBuilder.append(']');
			return nbtListStringBuilder.toString();
		} else return null;
	}
}