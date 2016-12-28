package com.outoonline.gcdquiz.inputprovision;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;

/**
  *
  */
public class JSoupWebPageScraperInputProviderTest {

    private WebPageScraperInputProvider webPageScraperInputProvider;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());
    private static final String KEY_WITH_NAME_1 = "key-with-name-1";
    private static final String KEY_WITH_NAME_2 = "key-with-name-2";
    private static final String FIRST_NUMBER = "45607738";
    private static final String SECOND_NUMBER = "95682358";
    private static final String EXPECTED_CONTENTS = "<html><head></head><body><form id=\"quiz\"><p><strong>" + FIRST_NUMBER + "</strong> and <strong>" + SECOND_NUMBER + "</strong></p></form></body></html>";
    private static final String EXPECTED_COOKIE_NAME = "cookie-name";
    private static final String EXPECTED_COOKIE_VALUE = "cookie-value";
    private static final Map<String, String> EXPECTED_QUERIES = new HashMap<String, String>() {
        {
            put(KEY_WITH_NAME_1, "#quiz>p>strong:eq(0)");
            put(KEY_WITH_NAME_2, "#quiz>p>strong:eq(1)");
        }
    };
    private static final Map<String, String> EXPECTED_INPUTS = new HashMap<String, String>() {
        {
            put(KEY_WITH_NAME_1, FIRST_NUMBER);
            put(KEY_WITH_NAME_2, SECOND_NUMBER);
        }
    };
    private static final Map<String, String> EXPECTED_COOKIES = new HashMap<String, String>() {
        {
            put(EXPECTED_COOKIE_NAME, EXPECTED_COOKIE_VALUE);
        }
    };

    @Before
    public void setUp() throws Exception {
        String port = String.valueOf(wireMockRule.port());
        webPageScraperInputProvider = new JSoupWebPageScraperInputProvider(
                "http://localhost:" + port + "/some/page",
                EXPECTED_QUERIES);

        String expectedCookieString = "";
        for (Map.Entry<String, String> cookieEntry : EXPECTED_COOKIES.entrySet()) {
            expectedCookieString += String.format("%s=%s;", cookieEntry.getKey(), cookieEntry.getValue());
        }

        stubFor(get(urlEqualTo("/some/page"))
                .withHeader("Accept", containing("text/html"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withHeader("Set-Cookie", expectedCookieString)
                        .withBody(EXPECTED_CONTENTS)));

    }

    @Test
    public void shouldRetrievePageSuccessfully() throws Exception {
        webPageScraperInputProvider.getInputs();
        verify(getRequestedFor(urlMatching("/some/page")));
    }

    @Test
    public void shouldExtractData() throws Exception {
        Map<String, String> inputs = webPageScraperInputProvider.getInputs();
        assertThat(inputs.entrySet(), org.hamcrest.CoreMatchers.equalTo(EXPECTED_INPUTS.entrySet()));
    }

    @Test
    public void shouldRetrieveCookiesToBeSet() throws Exception {
        webPageScraperInputProvider.getInputs();
        assertThat(webPageScraperInputProvider.getCookiesToBeSet(), IsMapContaining.hasEntry(EXPECTED_COOKIE_NAME, EXPECTED_COOKIE_VALUE));
    }

    @Test
    public void shouldIndicateAnErrorIfInputsCannotBeProvided() throws Exception {
        webPageScraperInputProvider = new JSoupWebPageScraperInputProvider("http://localhost/non-existent-url", EXPECTED_QUERIES);
        expectedException.expect(UnableToProvideInputsException.class);
        webPageScraperInputProvider.getInputs();
    }

}