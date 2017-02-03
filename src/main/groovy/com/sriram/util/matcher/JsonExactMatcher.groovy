package com.sriram.util.matcher

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.client.ClientHttpRequest
import org.springframework.test.web.client.RequestMatcher

class JsonExactMatcher {

    public static String UNEXPECTED_JSON = "Unexpected json"


    static RequestMatcher jsonMatcher(final String json) {
        return new RequestMatcher() {

            @Override
            public void match(ClientHttpRequest request) throws AssertionError {
                compare(formatJson(), getActualJson(request))
            }

            void compare(String fromString, String toString) {
                if (!(fromString.equals(toString))) {
                    throw new AssertionError(UNEXPECTED_JSON)
                }
            }

            String formatJson() {
                ObjectMapper mapper = new ObjectMapper()
                return mapper.writeValueAsString(mapper.readValue(json, LinkedList.class))
            }

            String getActualJson(ClientHttpRequest clientHttpRequest) {
                return ((ByteArrayOutputStream) clientHttpRequest?.getBody())?.toString()
            }
        };
    }
}
