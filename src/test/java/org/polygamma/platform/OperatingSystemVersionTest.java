// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class OperatingSystemVersionTest {

	private static Stream<Arguments> testOfArgs() {
		return Stream.of(
			Arguments.of(new OperatingSystemVersion(0), "0"),
			Arguments.of(new OperatingSystemVersion(1), "1"),
			Arguments.of(new OperatingSystemVersion(1, 2), "1.2"),
			Arguments.of(new OperatingSystemVersion(1, 2, 3), "1.2.3"));
	}

	@ParameterizedTest
	@MethodSource("testOfArgs")
	public void testOf(OperatingSystemVersion expect, String version) {
		assertEquals(expect, OperatingSystemVersion.of(version));
		assertEquals(expect, OperatingSystemVersion.of("prefix-" + version));
		assertEquals(expect, OperatingSystemVersion.of(version + "-suffix"));
		assertEquals(expect, OperatingSystemVersion.of("prefix-" + version + "-suffix"));
	}

	@Test
	public void testOfInvalid() {
		assertThrows(IllegalArgumentException.class, () -> OperatingSystemVersion.of(""));
		assertThrows(IllegalArgumentException.class, () -> OperatingSystemVersion.of("abc"));
	}

	@Test
	public void testCompareTo() {
		OperatingSystemVersion a = OperatingSystemVersion.of("1.0.0");
		OperatingSystemVersion b = OperatingSystemVersion.of("1.1.0");
		OperatingSystemVersion c = OperatingSystemVersion.of("1.1.1");

		assertTrue(a.compareTo(a) == 0);
		assertTrue(a.compareTo(b) < 0);
		assertTrue(a.compareTo(c) < 0);

		assertTrue(b.compareTo(c) < 0);
		assertTrue(c.compareTo(b) > 0);
	}
}
