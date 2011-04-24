package com.redygest.metrics;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Measure {
	double measureDataQuality(String fileGoldPath, String fileOutputPath)  throws FileNotFoundException, IOException;
}
