// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import java.util.Locale;

import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;

/**
 * Processor architecture enumeration.
 *
 * @since 1.0
 */
public enum Architecture {

	/**
	 * x86 processor architecture, 32-bit.
	 *
	 * @since 1.0
	 */
	X86(4),

	/**
	 * x86 processor architecture, 64-bit.
	 *
	 * @since 1.0
	 */
	X86_64(8),

	/**
	 * ARM (version 7 or above) processor architecture, 32-bit.
	 *
	 * @since 1.0
	 */
	ARM(4),

	/**
	 * ARM (version 8 or above) processor architecture, 64-bit.
	 *
	 * @since 1.0
	 */
	ARM64(8),

	/**
	 * RISC-V processor architecture, 64-bit.
	 *
	 * @since 1.0
	 */
	RISCV64(8),

	/**
	 * PowerPC processor architecture, 64-bit.
	 *
	 * @since 1.0
	 */
	PPC64(8),

	/**
	 * S390X processor architecture, 64-bit.
	 *
	 * @since 1.0
	 */
	S390X(8);

	/**
	 * Processor architecture for name.
	 *
	 * <p>This normalizes {@code name} by {@linkplain String#toLowerCase() lower-casing} all
	 * characters, and removing all non-alphanumeric characters. The normalized {@code name} is
	 * then matched against the patterns below.
	 *
	 * <table class='striped'>
	 *     <caption>Name Match Patterns</caption>
	 *     <thead>
	 *         <tr>
	 *             <th>Processor Architecture</th>
	 *             <th>Pattern</th>
	 *         </tr>
	 *     </thead>
	 *     <tbody>
	 *         <tr>
	 *             <td>{@linkplain #X86 x86}</td>
	 *             <td>{@code x86(32)?|i[3-6]86|ia32}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #X86_64 x86-64}</td>
	 *             <td>{@code (x(86)?|amd)64|ia32e|em64t|x32}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #ARM ARM}</td>
	 *             <td>{@code (aarch|arm)(32)?[a-z]*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #ARM64 ARM64}</td>
	 *             <td>{@code (aarch|arm)64[a-z]*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #RISCV64 RISC-V 64}</td>
	 *             <td>{@code riscv64[a-z]*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #PPC64 PowerPC64}</td>
	 *             <td>{@code (powerpc|ppc)64[a-z]*}</td>
	 *         </tr>
	 *         <tr>
	 *             <td>{@linkplain #S390X s390x}</td>
	 *             <td>{@code s390x|zarch64}</td>
	 *         </tr>
	 *     </tbody>
	 * </table>
	 *
	 * @param name Name to find processor architecture of.
	 * @return Processor architecture matching {@code name}.
	 * @throws IllegalArgumentException {@code name} was not recognized as a valid processor
	 * architecture.
	 * @since 1.0
	 */
	@SideEffectFree
	public static Architecture of(String name) {
		String origName = name;
		Architecture arch;

		name = name.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
		if (name.matches("^x86(32)?|i[3-6]86|ia32$"))
			arch = X86;
		else if (name.matches("^(x(86)?|amd)64|ia32e|em64t|x32$"))
			arch = X86_64;
		else if (name.matches("^(aarch|arm)(32)?(v7)?[a-z]*$"))
			arch = ARM;
		else if (name.matches("^(aarch|arm)64[a-z]*$"))
			arch = ARM64;
		else if (name.matches("^riscv64[a-z]*$"))
			arch = RISCV64;
		else if (name.matches("^(powerpc|ppc)64[a-z]*$"))
			arch = PPC64;
		else if (name.matches("^s390x|zarch64$"))
			arch = S390X;
		else
			throw new IllegalArgumentException(String.format("Unknown architecture: %s", origName));
		return arch;
	}

	private final IntegerModel wordModel;

	private Architecture(int wordSizeBytes) {
		this.wordModel = IntegerModel.ofBytes(wordSizeBytes);
	}

	/**
	 * Native word model of this.
	 *
	 * @return Native processor word model.
	 * @since 1.0
	 */
	@Pure
	public IntegerModel wordModel() {
		return this.wordModel;
	}

	/**
	 * Native word model of this is 64-bit.
	 *
	 * @return {@code true} <i>if and only if</i> native processor word size is 64-bit.
	 * @since 1.0
	 * @see #wordModel()
	 */
	@Pure
	public boolean is64Bit() {
		return this.wordModel.is64Bit();
	}
}
