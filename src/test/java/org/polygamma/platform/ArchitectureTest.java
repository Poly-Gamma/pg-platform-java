// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class ArchitectureTest {

	private static Stream<Arguments> testOfArgs() {
		return Stream.of(
			Arguments.of(Architecture.X86, List.of("x86", "i386", "i486", "i586", "i686", "ia32")),
			Arguments.of(Architecture.X86_64, List.of("x86_64", "amd64", "x64", "x32")),
			Arguments.of(Architecture.ARM, List.of("arm", "aarch", "armv7", "armv7-a")),
			Arguments.of(Architecture.ARM64, List.of("aarch64", "arm64")),
			Arguments.of(Architecture.RISCV64, List.of("riscv64", "riscv64be", "riscv64eb")),
			Arguments.of(Architecture.PPC64,
						 List.of("ppc64", "powerpc64", "ppc64le", "powerpc64le")),
			Arguments.of(Architecture.S390X, List.of("s390x", "z/Arch64")));
	}

	@ParameterizedTest
	@MethodSource("testOfArgs")
	public void testOf(Architecture expect, List<String> names) {
		names.forEach(name -> {
			assertEquals(expect, Architecture.of(name));
			assertEquals(expect, Architecture.of(name.toUpperCase(Locale.US)));
		});
	}

	@Test
	public void testOfUnknown() {
		assertThrows(IllegalArgumentException.class, () -> Architecture.of("unknown"));
	}

	private static Stream<Arguments> testWordModelArgs() {
		return Stream.of(
			Arguments.of(Architecture.X86, IntegerModel.BIT32),
			Arguments.of(Architecture.X86_64, IntegerModel.BIT64),
			Arguments.of(Architecture.ARM, IntegerModel.BIT32),
			Arguments.of(Architecture.ARM64, IntegerModel.BIT64),
			Arguments.of(Architecture.RISCV64, IntegerModel.BIT64),
			Arguments.of(Architecture.PPC64, IntegerModel.BIT64),
			Arguments.of(Architecture.S390X, IntegerModel.BIT64));
	}

	@ParameterizedTest
	@MethodSource("testWordModelArgs")
	public void testWordModel(Architecture arch, IntegerModel expectModel) {
		assertEquals(expectModel, arch.wordModel());
		assertEquals(expectModel.is64Bit(), arch.is64Bit());
	}
}
