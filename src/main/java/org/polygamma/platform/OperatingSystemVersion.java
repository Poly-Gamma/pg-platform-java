// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * Operating system version components.
 *
 * @param major Major version component.
 * @param minor Minor version component.
 * @param patch Patch level component.
 */
public record OperatingSystemVersion(@NonNegative int major, @NonNegative int minor,
									 @NonNegative int patch)
implements Comparable<OperatingSystemVersion> {

	/**
	 * Construct operating system version from string.
	 *
	 * <p>This strips build prefix and suffix from {@code version}, parsing at most the first 3
	 * integer version components: major, minor, and patch. Any component not present is assumed
	 * to be {@code 0}. For example, if {@code version} is {@code "1.2"}, then the resulting
	 * version will have {@linkplain #major() major}, {@linkplain #minor() minor}, and {@linkplain
	 * #patch() patch level} components equal to {@code 1}, {@code 2}, and {@code 0}, respectively.
	 *
	 * @param version Version string.
	 * @return Operating system version.
	 * @throws IllegalArgumentException {@code version} is malformed.
	 * @since 1.0
	 */
	@SideEffectFree
	public static OperatingSystemVersion of(String version) {
		Pattern pattern =
			Pattern.compile("^([-+_0-9a-z]*-)?(?<version>[0-9]+(\\.[0-9]+)*)(-[-+_0-9a-z]*)?$",
							Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(version);

		if (!matcher.matches())
			throw new IllegalArgumentException(
				String.format("Malformed version string: %s", version));

		String normalized = (@NonNull String) matcher.group("version");
		String[] components = normalized.split("\\.");

		return new OperatingSystemVersion(
			Integer.parseInt(components[0], 10),
			components.length > 1 ? Integer.parseInt(components[1], 10) : 0,
			components.length > 2 ? Integer.parseInt(components[2], 10) : 0);
	}

	/**
	 * Construct new operating system version.
	 *
	 * @param major Major version component.
	 * @param minor Minor version component.
	 * @param patch Patch level.
	 * @throws IllegalArgumentException {@code major}, {@code minor}, or {@code patch} is negative.
	 * @since 1.0
	 */
	@Pure
	public OperatingSystemVersion {
		if ((major | minor | patch) < 0)
			throw new IllegalArgumentException(
				String.format("Malformed version: %d.%d.%d", major, minor, patch));
	}

	/**
	 * Construct new operating system version, with patch-level of {@code 0}.
	 *
	 * <p>Shorthand for:
	 *
	 * {@snippet :
	 * new OperatingSystemVersion(major, minor, 0); // @link substring="OperatingSystemVersion" target="#OperatingSystemVersion(int, int, int)"
	 * }
	 *
	 * @param major Major version component.
	 * @param minor Minor version component.
	 * @throws IllegalArgumentException {@code major} or {@code minor} is negative.
	 * @since 1.0
	 * @see #OperatingSystemVersion(int, int, int)
	 */
	@Pure
	public OperatingSystemVersion(int major, int minor) {
		this(major, minor, 0);
	}

	/**
	 * Construct new operating system version, with minor version and patch-level of {@code 0}.
	 *
	 * <p>Shorthand for:
	 *
	 * {@snippet :
	 * new OperatingSystemVersion(major, 0, 0); // @link substring="OperatingSystemVersion" target="#OperatingSystemVersion(int, int, int)"
	 * }
	 *
	 * @param major Major version component.
	 * @throws IllegalArgumentException {@code major} is negative.
	 * @since 1.0
	 * @see #OperatingSystemVersion(int, int, int)
	 */
	@Pure
	public OperatingSystemVersion(int major) {
		this(major, 0, 0);
	}

	@Pure
	@Override
	public int compareTo(OperatingSystemVersion that) {
		int res = Integer.compare(this.major, that.major);

		if (res == 0) {
			res = Integer.compare(this.minor, that.minor);
			if (res == 0)
				res = Integer.compare(this.patch, that.patch);
		}
		return res;
	}
}
