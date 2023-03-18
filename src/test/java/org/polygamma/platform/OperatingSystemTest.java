// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OperatingSystemTest {

	private static Stream<Arguments> testOfArgs() {
		return Stream.of(
			Arguments.of(OperatingSystem.LINUX, List.of("linux")),
			Arguments.of(OperatingSystem.NETBSD, List.of("netbsd")),
			Arguments.of(OperatingSystem.FREEBSD, List.of("freebsd")),
			Arguments.of(OperatingSystem.OPENBSD, List.of("openbsd")),
			Arguments.of(OperatingSystem.MACOS, List.of("macos", "osx", "darwin")),
			Arguments.of(OperatingSystem.WINDOWS, List.of("windows")));
	}

	@ParameterizedTest
	@MethodSource("testOfArgs")
	public void testOf(OperatingSystem expect, List<String> names) {
		names.forEach(name -> {
			assertEquals(expect, OperatingSystem.of(name));
			assertEquals(expect, OperatingSystem.of(name.toUpperCase(Locale.US)));
		});
	}

	@Test
	public void testOfUnknown() {
		assertThrows(IllegalArgumentException.class, () -> OperatingSystem.of("unknown"));
	}

	private static Stream<Arguments> testOsArgs() {
		return Stream.of(OperatingSystem.values()).map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource("testOsArgs")
	public void testOs(OperatingSystem os) {
		assertEquals(os != OperatingSystem.WINDOWS, os.isUnix());
		assertEquals(os.isUnix() ? "lib" : "", os.toLibraryPrefix());
		assertEquals(os == OperatingSystem.WINDOWS ? "dll" :
					 os == OperatingSystem.MACOS ? "dylib" : "so", os.toLibraryExtension());
		assertEquals(String.format("%s%s.%s", os.toLibraryPrefix(), "foo", os.toLibraryExtension()),
					 os.mapLibraryFileName("foo"));
	}
}
