// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import java.util.Objects;
import java.util.function.Function;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

/**
 * Platform descriptor.
 *
 * <p>A platform is described by its operating system, architecture, and data model. These three
 * components define an execution platform. The data model of the platform dictates one of its
 * ABI components, useful when interfacing between native libraries.
 *
 * @param os Operating system.
 * @param osVersion Operating system version, or {@code null}.
 * @param architecture Processor architecture.
 * @param dataModel Platform data model.
 * @since 1.0
 */
public record Platform(OperatingSystem os, @Nullable OperatingSystemVersion osVersion,
					   Architecture architecture, DataModel dataModel) {

	private static final Object CURRENT;

	static {
		Function<String, String> property = name -> {
			@Nullable String value = System.getProperty(name);

			if (value == null)
				throw new IllegalStateException(
					String.format("System property %s not defined", name));
			return value;
		};
		Object current;

		try {
			OperatingSystem os = OperatingSystem.of(property.apply("os.name"));
			OperatingSystemVersion osv = OperatingSystemVersion.of(property.apply("os.version"));
			String archName = property.apply("os.arch");
			Architecture arch = Architecture.of(archName);
			DataModel dataModel;

			if ("x32".equals(archName) || !arch.is64Bit())
				dataModel = DataModel.ILP32;
			else
				dataModel = os == OperatingSystem.WINDOWS ? DataModel.LLP64 : DataModel.LP64;
			current = new Platform(os, osv, arch, dataModel);
		} catch (Throwable e) {
			current = new IllegalStateException("Failed to probe current platform", e);
		}
		CURRENT = current;
	}

	/**
	 * Platform JVM is executing on.
	 *
	 * @return JVM executing platform.
	 * @throws UnsupportedOperationException An error was encountered while probing executing
	 * platform.
	 * @since 1.0
	 */
	@Pure
	public static Platform current() {
		if (CURRENT instanceof Platform current)
			return current;
		throw new UnsupportedOperationException((Throwable) CURRENT);
	}

	/**
	 * Construct new platform descriptor.
	 *
	 * @param os Operating system.
	 * @param osVersion Operating system version, or {@code null}.
	 * @param architecture Processor architecture.
	 * @param dataModel Platform data model.
	 * @since 1.0
	 */
	@Pure
	public Platform {
		Objects.requireNonNull(os);
		Objects.requireNonNull(architecture);
		Objects.requireNonNull(dataModel);
	}

	/**
	 * Construct new platform descriptor, without operating system version.
	 *
	 * <p>Shorthand for:
	 *
	 * {@snippet :
	 * new Platform(os, null, architecture, dataModel); // @link substring="Platform" target="#Platform(OperatingSystem, OperatingSystemVersion, Architecture, DataModel)"
	 * }
	 *
	 * @param os Operating system.
	 * @param architecture Processor architecture.
	 * @param dataModel Platform data model.
	 * @since 1.0
	 * @see #Platform(OperatingSystem, OperatingSystemVersion, Architecture, DataModel)
	 */
	@Pure
	public Platform(OperatingSystem os, Architecture architecture, DataModel dataModel) {
		this(os, null, architecture, dataModel);
	}

	/**
	 * Construct new platform descriptor, with default data model.
	 *
	 * <p>Like {@link #Platform(OperatingSystem, OperatingSystemVersion, Architecture, DataModel)};
	 * however, this determines the data model based on {@code os} and {@code architecture}. For
	 * example, if {@code os} is {@linkplain OperatingSystem#WINDOWS Windows} and {@code
	 * architecture} is {@linkplain Architecture#X86_64 x86-64}, then the {@linkplain
	 * DataModel#LLP64 LLP64} data model is used.
	 *
	 * @param os Operating system.
	 * @param osVersion Operating system version, or {@code null}.
	 * @param architecture Processor architecture.
	 * @since 1.0
	 */
	@Pure
	public Platform(OperatingSystem os, @Nullable OperatingSystemVersion osVersion,
					Architecture architecture) {
		this(os, osVersion, architecture,
			 architecture.is64Bit() ? os.isUnix() ? DataModel.LP64 : DataModel.LLP64 :
			 DataModel.ILP32);
	}

	/**
	 * Construct new platform descriptor, with default data model and without operating system
	 * version.
	 *
	 * <p>Shorthand for:
	 *
	 * {@snippet :
	 * new Platform(os, null, architecture); // @link substring="Platform" target="#Platform(OperatingSystem, OperatingSystemVersion, Architecture)"
	 * }
	 *
	 * @param os Operating system.
	 * @param architecture Processor architecture.
	 * @since 1.0
	 * @see #Platform(OperatingSystem, OperatingSystemVersion, Architecture)
	 */
	@Pure
	public Platform(OperatingSystem os, Architecture architecture) {
		this(os, null, architecture);
	}

	/**
	 * Test if {@linkplain #os() operating system} of this is equal to another.
	 *
	 * @param that Operating system to test against.
	 * @return {@code true} <i>if and only if</i> operating system is equal to {@code that}.
	 * @since 1.0
	 */
	@Pure
	public boolean isOs(OperatingSystem that) {
		return this.os == that;
	}

	/**
	 * Compare {@linkplain #osVersion() operating system version} of this to another.
	 *
	 * <p>If the operating system version of this is {@code null}, then this returns {@code -1}
	 * unconditionally.
	 *
	 * @param that Operating system version to compare against.
	 * @return Value less than, equal to, or greater than {@code 0} if operating system version is
	 * less than, equal to, or greater than {@code that}, respectively.
	 * @since 1.0
	 */
	@Pure
	public int compareOsVersion(OperatingSystemVersion that) {
		return this.osVersion == null ? -1 : this.osVersion.compareTo(that);
	}

	/**
	 * Compare {@linkplain #osVersion() operating system version} of this to components.
	 *
	 * <p>Shorthand for:
	 *
	 * {@snippet :
	 * compareOsVersion(new OperatingSystemVersion(major, minor, patch)); // @link substring="compareOsVersion" target="#compareOsVersion(OperatingSystemVersion)"
	 * }
	 *
	 * @param major Major version component.
	 * @param minor Minor version component.
	 * @param patch Patch level component.
	 * @return Value less than, equal to, or greater than {@code 0} if operating system version is
	 * less than, equal to, or greater than specified components, respectively.
	 * @throws IllegalArgumentException {@code major}, {@code minor}, or {@code patch} is negative.
	 * @since 1.0
	 * @see #compareOsVersion(OperatingSystemVersion)
	 */
	@Pure
	public int compareOsVersion(int major, int minor, int patch) {
		return this.compareOsVersion(new OperatingSystemVersion(major, minor, patch));
	}

	/**
	 * Compare {@linkplain #osVersion() operating system version} of this to major and minor
	 * version components.
	 *
	 * <p>Shorthand for:
	 *
	 * {@snippet :
	 * compareOsVersion(major, minor, 0); // @link substring="compareOsVersion" target="#compareOsVersion(int, int, int)"
	 * }
	 *
	 * @param major Major version component.
	 * @param minor Minor version component.
	 * @return Value less than, equal to, or greater than {@code 0} if operating system version is
	 * less than, equal to, or greater than specified components, respectively.
	 * @throws IllegalArgumentException {@code major} or {@code minor} is negative.
	 * @since 1.0
	 * @see #compareOsVersion(int, int, int)
	 */
	@Pure
	public int compareOsVersion(int major, int minor) {
		return this.compareOsVersion(major, minor, 0);
	}

	/**
	 * Compare {@linkplain #osVersion() operating system version} of this to major version
	 * component.
	 *
	 * <p>Shorthand for:
	 *
	 * {@snippet :
	 * compareOsVersion(major, 0, 0); // @link substring="compareOsVersion" target="#compareOsVersion(int, int, int)"
	 * }
	 *
	 * @param major Major version component.
	 * @return Value less than, equal to, or greater than {@code 0} if major operating system
	 * version component is less than, equal to, or greater than {@code major}, respectively.
	 * @throws IllegalArgumentException {@code major} is negative.
	 * @since 1.0
	 * @see #compareOsVersion(int, int, int)
	 */
	@Pure
	public int compareOsVersion(int major) {
		return this.compareOsVersion(major, 0, 0);
	}

	/**
	 * Test if {@linkplain #architecture() architecture} of this is equal to another.
	 *
	 * @param that Architecture to test against.
	 * @return {@code true} <i>if and only if</i> architecture is equal to {@code that}.
	 * @since 1.0
	 */
	@Pure
	public boolean isArchitecture(Architecture that) {
		return this.architecture == that;
	}

	/**
	 * Test if {@linkplain #dataModel() data model} of this is equal to another.
	 *
	 * @param that Data model to test against.
	 * @return {@code true} <i>if and only if</i> data model is equal to {@code that}.
	 * @since 1.0
	 */
	@Pure
	public boolean isDataModel(DataModel that) {
		return this.dataModel == that;
	}

	/**
	 * Test if native processor word size of this is 64-bit.
	 *
	 * <p>Shorthand for:
	 *
	 * {@snippet :
	 * architecture().is64Bit(); // @link substring="is64Bit" target="Architecture#is64Bit()"
	 * }
	 *
	 * @return {@code true} <i>if and only if</i> processor word is 64-bit.
	 * @since 1.0
	 * @see #architecture()
	 * @see Architecture#is64Bit()
	 */
	@Pure
	public boolean is64BitWord() {
		return this.architecture.is64Bit();
	}

	/**
	 * Test if native address-space size is 64-bit.
	 *
	 * <p>If the {@linkplain #dataModel() data model} of this platform has 64-bit {@link
	 * DataModel#pointerModel() pointer} model, this returns {@code true}.
	 *
	 * @return {@code true} <i>if and only if</i> address-space is 64-bit.
	 * @since 1.0
	 * @see #dataModel()
	 * @see DataModel#pointerModel()
	 * @see IntegerModel#is64Bit()
	 */
	@Pure
	public boolean is64BitAddress() {
		return this.dataModel.pointerModel().is64Bit();
	}
}
