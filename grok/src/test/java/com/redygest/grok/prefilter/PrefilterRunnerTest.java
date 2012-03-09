package com.redygest.grok.prefilter;

import junit.framework.TestCase;

public class PrefilterRunnerTest extends TestCase {

	public void testRunner() {
		PrefilterRunner runner = new PrefilterRunner(
				PrefilterType.NONENLGISH_LANG_FILTER);
		String text = "Lokpal Bill debate: BJP's ideology is to destroy, says Kapil Sibal";
		boolean result = runner.runFilters(text);
		assertTrue(result);

		text = "Esta es una prueba de espa�ol";
		result = runner.runFilters(text);
		assertFalse(result);
	}

}