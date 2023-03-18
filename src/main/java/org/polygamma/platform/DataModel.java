// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import org.checkerframework.dataflow.qual.Pure;

/**
 * Platform data model.
 *
 * <p>The data model of a platform describes the size of standard platform types. For example, the
 * {@link #ILP64 ILP64} data model defines the size of {@code int}, {@code long}, and {@code
 * pointer} types as 64-bit; while the {@linkplain #LP64} data model defines the size of {@code
 * int} types as 32-bit, and, {@code long} and {@code pointer} types as 64-bit.
 *
 * @since 1.0
 */
public enum DataModel {

	/**
	 * {@code int}, {@code long}, {@code long long}, and {@code pointer} types are 32-bit.
	 *
	 * @since 1.0
	 */
	ILP32(4, 4, 4, 4),

	/**
	 * {@code int}, {@code long}, {@code long long}, and {@code pointer} types are 64-bit.
	 *
	 * @since 1.0
	 */
	ILP64(8, 8, 8, 8),

	/**
	 * {@code int} and {@code long} types are 32-bit, and, {@code long long} and {@code pointer}
	 * types are 64-bit.
	 *
	 * @since 1.0
	 */
	LLP64(4, 4, 8, 8),

	/**
	 * {@code int} types are 32-bit, and, {@code long}, {@code long long}, and {@code pointer}
	 * types are 64-bit.
	 *
	 * @since 1.0
	 */
	LP64(4, 8, 8, 8);

	private final IntegerModel intModel;
	private final IntegerModel longModel;
	private final IntegerModel llongModel;
	private final IntegerModel pointerModel;

	private DataModel(int intSizeBytes, int longSizeBytes, int llongSizeBytes,
					  int pointerSizeBytes) {
		this.intModel = IntegerModel.ofBytes(intSizeBytes);
		this.longModel = IntegerModel.ofBytes(longSizeBytes);
		this.llongModel = IntegerModel.ofBytes(llongSizeBytes);
		this.pointerModel = IntegerModel.ofBytes(pointerSizeBytes);
	}

	/**
	 * Integer model of {@code int} types of this.
	 *
	 * @return {@code int} model.
	 * @since 1.0
	 */
	@Pure
	public IntegerModel intModel() {
		return this.intModel;
	}

	/**
	 * Integer model of {@code long} types of this.
	 *
	 * @return {@code long} model.
	 * @since 1.0
	 */
	@Pure
	public IntegerModel longModel() {
		return this.longModel;
	}

	/**
	 * Integer model of {@code long long} types of this.
	 *
	 * @return {@code long long} model.
	 * @since 1.0
	 */
	@Pure
	public IntegerModel llongModel() {
		return this.llongModel;
	}

	/**
	 * Integer model of {@code pointer} types of this.
	 *
	 * @return {@code long long} model.
	 * @since 1.0
	 */
	@Pure
	public IntegerModel pointerModel() {
		return this.pointerModel;
	}
}
