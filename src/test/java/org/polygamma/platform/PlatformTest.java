// SPDX-License-Identifier: Apache-2.0

package org.polygamma.platform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlatformTest {

	@Test
	public void testCurrent() {
		assertNotNull(Platform.current());
	}
}
