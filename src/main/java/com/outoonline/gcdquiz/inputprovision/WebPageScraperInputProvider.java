package com.outoonline.gcdquiz.inputprovision;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public interface WebPageScraperInputProvider extends InputProvider {

    Map<String, String> getCookiesToBeSet() throws IOException;

}
