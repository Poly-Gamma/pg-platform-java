// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import java.util.Locale;

import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * Operating system enumeration.
 *
 * @since 1.0
 */
public enum OperatingSystem {

	/**
	 * Linux kernel based operating system.
	 *
	 * @since 1.0
	 */
	LINUX,

	/**
	 * FreeBSD operating system.
	 *
	 * @since 1.0
	 */
	FREEBSD,

	/**
	 * NetBSD operating system.
	 *
	 * @since 1.0
	 */
	NETBSD,

	/**
	 * OpenBSD operating system.
	 *
	 * @since 1.0
	 */
	OPENBSD,

	/**
	 * Apple's macOS operating system.
	 *
	 * @since 1.0
	 */
	MACOS,

	/**
	 * Microsoft's Windows operating system.
	 *
	 * @since 1.0
	 */
	WINDOWS;

	/**
	 * Operating system for name.
	 *
	 * <p>This normalizes {@code name} by {@linkplain String#toLowerCase() lower-casing} all
	 * characters, and removing all non-alphanumeric characters. The normalized {@code name} is
	 * then matched against the patterns below.
	 *
	 * <table class='striped'>
	 *     <caption>Name Match Patterns</caption>
	 *     <thead>
	 *         <tr>
	 *             <th>Operating System</th>
	 *             <th>Pattern</th>
	 *         </tr>
	 *     </thead>
	 *     <tbody>
	 *         <tr>
	 *             <td>{@linkplain #LINUX Linux}</td>
	 *             <td>{@code *linux*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #FREEBSD FreeBSD}</td>
	 *             <td>{@code *freebsd*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #NETBSD NetBSD}</td>
	 *             <td>{@code *netbsd*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #OPENBSD OpenBSD}</td>
	 *             <td>{@code *openbsd*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #MACOS macOS}</td>
	 *             <td>{@code *darwin*|(mac|osx)*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #WINDOWS Windows}</td>
	 *             <td>{@code *windows*}</td>
	 *         </tr>
	 *     </tbody>
	 * </table>
	 *
	 * @param name Name to find operating system of.
	 * @return Operating system matching {@code name}.
	 * @throws IllegalArgumentException {@code name} was not recognized as a valid operating system.
	 * @since 1.0
	 */
	@SideEffectFree
	public static OperatingSystem of(String name) {
		String origName = name;
		OperatingSystem os;

		name = name.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
		if (name.contains("linux"))
			os = OperatingSystem.LINUX;
		else if (name.contains("freebsd"))
			os = OperatingSystem.FREEBSD;
		else if (name.contains("netbsd"))
			os = OperatingSystem.NETBSD;
		else if (name.contains("openbsd"))
			os = OperatingSystem.OPENBSD;
		else if (name.contains("darwin") || name.startsWith("mac") || name.startsWith("osx"))
			os = OperatingSystem.MACOS;
		else if (name.contains("windows"))
			os = OperatingSystem.WINDOWS;
		else
			throw new IllegalArgumentException(
				String.format("Unknown operating system: %s", origName));
		return os;
	}

	/**
	 * This is a Unix based operating system.
	 *
	 * @return {@code true} <i>if and only if</i> Unix based operating system.
	 * @since 1.0
	 */
	@Pure
	public boolean isUnix() {
		return this != WINDOWS;
	}

	/**
	 * Library file prefix of this.
	 *
	 * @return Library file prefix.
	 * @since 1.0
	 */
	@Pure
	public String toLibraryPrefix() {
		return this.isUnix() ? "lib" : "";
	}

	/**
	 * Library file extension of this.
	 *
	 * @return Library file extension, without leading dot.
	 * @since 1.0
	 */
	@Pure
	public String toLibraryExtension() {
		return this == WINDOWS ? "dll" : this == MACOS ? "dylib" : "so";
	}

	/**
	 * Map library name to corresponding file name for this.
	 *
	 * <p>This maps {@code libraryName} to the corresponding library file name for this operating
	 * system. In general, this is equal to prepending the {@linkplain #toLibraryPrefix() library
	 * prefix} to {@code libraryName}, and appending the {@linkplain #toLibraryExtension() library
	 * extension} (with dot) to {@code libraryName}. For example, if this is {@linkplain #MACOS}
	 * and {@code libraryName} is {@code foo}, then this returns {@code libfoo.dylib}.
	 *
	 * @param libraryName Library name.
	 * @return Library file name.
	 * @since 1.0
	 * @see #toLibraryPrefix()
	 * @see #toLibraryExtension()
	 */
	@Pure
	public String mapLibraryFileName(String libraryName) {
		return String.format("%s%s.%s", this.toLibraryPrefix(), libraryName,
							 this.toLibraryExtension());
	}
}
