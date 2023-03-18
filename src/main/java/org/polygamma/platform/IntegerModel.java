// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import org.checkerframework.common.reflection.qual.ClassVal;
import org.checkerframework.common.value.qual.IntVal;
import org.checkerframework.dataflow.qual.Pure;

/**
 * Platform integer data model enumeration.
 *
 * @since 1.0
 */
public enum IntegerModel {

	/**
	 * 32-bit integer.
	 *
	 * @since 1.0
	 */
	BIT32(4),

	/**
	 * 64-bit integer.
	 *
	 * @since 1.0
	 */
	BIT64(8);

	/**
	 * Integer data model of {@code byte} size.
	 *
	 * @param bytes Byte size.
	 * @return Integer data model with {@linkplain #bytes() byte size} of {@code bytes}.
	 * @throws IllegalArgumentException {@code bytes} is not {@code 8} or {@code 4}.
	 * @since 1.0
	 */
	@Pure
	public static IntegerModel ofBytes(int bytes) {
		return switch (bytes) {
			case 8 -> BIT64;
			case 4 -> BIT32;
			default -> throw new IllegalArgumentException("Invalid integer size");
		};
	}


	/**
	 * Integer data model of {@code bit} size.
	 *
	 * @param bits Bit size.
	 * @return Integer data model with {@linkplain #bits() bit size} of {@code bits}.
	 * @throws IllegalArgumentException {@code bits} is not {@code 64} or {@code 32}.
	 * @since 1.0
	 */
	@Pure
	public static IntegerModel ofBits(int bits) {
		return ofBytes(bits >> 3);
	}

	private final @IntVal({ 4, 8 }) int bytes;

	private IntegerModel(int bytes) {
		assert bytes == 4 || bytes == 8 : "@AssumeAssertion(value)";
		this.bytes = bytes;
	}

	/**
	 * Size, in bytes, of this.
	 *
	 * @return Integer size, in bytes.
	 * @since 1.0
	 */
	@Pure
	public @IntVal({ 4, 8 }) int bytes() {
		return this.bytes;
	}

	/**
	 * Size, in bits, of this.
	 *
	 * @return Integer size, in bits.
	 * @since 1.0
	 */
	@Pure
	public @IntVal({ 32, 64 }) int bits() {
		return this.bytes << 3;
	}

	/**
	 * Size, in bits, of this is 64-bit.
	 *
	 * @return {@code true} <i>if and only if</i> {@linkplain #bits() bit size} is 64.
	 * @since 1.0
	 */
	@Pure
	public boolean is64Bit() {
		return this.bytes == 8;
	}


	/**
	 * Size, in bits, of this is 32-bit.
	 *
	 * @return {@code true} <i>if and only if</i> {@linkplain #bits() bit size} is 32.
	 * @since 1.0
	 */
	@Pure
	public boolean is32Bit() {
		return !this.is64Bit();
	}

	/**
	 * Java type integers of this model map to.
	 *
	 * @return {@code long.class} or {@code int.class} if {@linkplain #bits() bit size} is 64 or
	 * 32, respectively.
	 * @since 1.0
	 */
	@Pure
	public @ClassVal({ "int", "long" }) Class<?> toJavaType() {
		return this.is64Bit() ? long.class : int.class;
	}
}
