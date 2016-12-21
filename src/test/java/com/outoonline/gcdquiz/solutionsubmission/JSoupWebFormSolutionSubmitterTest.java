package com.outoonline.gcdquiz.solutionsubmission;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
  *
  */
public class JSoupWebFormSolutionSubmitterTest {

    private WebFormSolutionSubmitter webFormSolutionSubmitter;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final Map<String, String> SOLUTION = new HashMap<String, String>() {{
        put("key", "value");
    }};
    private static final String EXPECTED_REQUEST_BODY = "key=value\n";
    private static final String EXPECTED_RESPONSE_BODY = "<html><head></head><body></body></html>";
    private static final String EXPECTED_COOKIE_NAME = "cookie-name";
    private static final String EXPECTED_COOKIE_VALUE = "cookie-value";
    private static final int HTTP_STATUS_OK = 200;
    private static final Map<String, String> EXPECTED_COOKIES = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        EXPECTED_COOKIES.put(EXPECTED_COOKIE_NAME, EXPECTED_COOKIE_VALUE);

        String port = String.valueOf(wireMockRule.port());
        webFormSolutionSubmitter = new JSoupWebFormSolutionSubmitter("http://localhost:" + port + "/some/page");

        stubFor(post(urlEqualTo("/some/page"))
                .withHeader("Accept", containing("text/html; charset=UTF-8"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                .withRequestBody(equalTo(EXPECTED_REQUEST_BODY))
                .willReturn(aResponse()
                        .withStatus(HTTP_STATUS_OK)
                        .withHeader("Content-Type", "text/html")
                        .withBody(EXPECTED_RESPONSE_BODY)));
    }

    @Test
    public void shouldSubmitToAPage() throws Exception {
        webFormSolutionSubmitter.submitSolution(EXPECTED_COOKIES, SOLUTION);
        verify(postRequestedFor(urlMatching("/some/page"))
                .withRequestBody(equalTo(EXPECTED_REQUEST_BODY)));

    }

    @Test
    public void shouldReturnResponseBody() throws Exception {
        webFormSolutionSubmitter.submitSolution(EXPECTED_COOKIES, SOLUTION);
        String actualResponseBody = webFormSolutionSubmitter.getResponseBody();
        assertThat(actualResponseBody, is(CoreMatchers.equalTo(EXPECTED_RESPONSE_BODY)));
    }

    @Test
    public void shouldSubmitWithCookiesIfTheyAreDeclared() throws Exception {
        webFormSolutionSubmitter.submitSolution(EXPECTED_COOKIES, SOLUTION);
        verify(postRequestedFor(urlMatching("/some/page"))
                .withCookie(EXPECTED_COOKIE_NAME, equalTo(EXPECTED_COOKIE_VALUE)));
    }

    @Test
    public void shouldSubmitWithoutCookiesIfTheyAreNotDeclared() throws Exception {
        webFormSolutionSubmitter.submitSolution(SOLUTION);
        verify(postRequestedFor(urlMatching("/some/page"))
                .withCookie(EXPECTED_COOKIE_NAME, notMatching(EXPECTED_COOKIE_VALUE)));
    }

    @Test
    public void shouldIndicateProblemDuringSubmission() throws Exception {
        webFormSolutionSubmitter = new JSoupWebFormSolutionSubmitter("http://non-existent");
        expectedException.expect(UnableToSubmitSolutionException.class);
        webFormSolutionSubmitter.submitSolution(SOLUTION);
    }
}