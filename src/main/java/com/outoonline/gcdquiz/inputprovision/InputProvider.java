package com.outoonline.gcdquiz.inputprovision;

import java.util.Map;

/**
 *
 */
public interface InputProvider {
    Map<String, String> getInputs() throws UnableToProvideInputsException;
}
