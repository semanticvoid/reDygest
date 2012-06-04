package com.redygest.grok.prefilter;

import com.redygest.grok.filtering.data.preextraction.PreExtractionPrefilterRunner;
import com.redygest.grok.filtering.data.preextraction.PreExtractionPrefilterType;

import junit.framework.TestCase;

public class PrefilterRunnerTest extends TestCase {

	public void testRunner() {
		PreExtractionPrefilterRunner runner = new PreExtractionPrefilterRunner(
				PreExtractionPrefilterType.NONENLGISH_LANG_FILTER);
		String text = "Lokpal Bill debate: BJP's ideology is to destroy, says Kapil Sibal";
		boolean result = runner.runFilters(text);
		assertTrue(result);

		text = "Esta es una prueba de espa–ol";
		result = runner.runFilters(text);
		assertFalse(result);
	}

}
