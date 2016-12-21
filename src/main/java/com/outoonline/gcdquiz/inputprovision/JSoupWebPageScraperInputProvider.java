package com.outoonline.gcdquiz.inputprovision;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class JSoupWebPageScraperInputProvider implements WebPageScraperInputProvider {

    private final String url;
    private final Map<String, String> queries;
    private Connection.Response response;

    public JSoupWebPageScraperInputProvider(String url, Map<String, String> queries) {
        this.url = url;
        this.queries = queries;
    }

    @Override
    public Map<String, String> getInputs() throws UnableToProvideInputsException {
        try {
            response = Jsoup.connect(url).method(Connection.Method.GET).execute();

            Document document = response.parse();
            return queries.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, o -> document.select(o.getValue()).text()));
        } catch (IOException e) {
            throw new UnableToProvideInputsException(e);
        }
    }

    @Override
    public Map<String, String> getCookiesToBeSet() throws IOException {
        return response != null ? response.cookies() : new HashMap<>();
    }
}

