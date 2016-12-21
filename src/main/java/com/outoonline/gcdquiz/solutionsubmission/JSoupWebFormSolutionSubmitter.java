package com.outoonline.gcdquiz.solutionsubmission;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
public class JSoupWebFormSolutionSubmitter implements WebFormSolutionSubmitter {

    private final String url;
    private Document doc;

    public JSoupWebFormSolutionSubmitter(String url) {
        this.url = url;
    }

    @Override
    public void submitSolution(Map<String, String> cookies, Map<String, String> solution) throws UnableToSubmitSolutionException {
        doc = null;
        try {
            Optional<String> body = solution.entrySet().stream()
                    .map(keyValue -> String.format("%s=%s\n", keyValue.getKey(), keyValue.getValue()))
                    .reduce(String::concat);

            doc = Jsoup.connect(url)
                    .header("Accept", "text/html; charset=UTF-8")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .cookies(cookies)
                    .requestBody(body.isPresent() ? body.get() : "")
                    .post();
        } catch (IOException e) {
            throw new UnableToSubmitSolutionException(e);
        }
    }

    @Override
    public void submitSolution(Map<String, String> solution) throws UnableToSubmitSolutionException {
        submitSolution(new HashMap<>(), solution);
    }

    @Override
    public String getResponseBody() {
        return doc.outputSettings(new Document.OutputSettings().prettyPrint(false)).toString();
    }

}
