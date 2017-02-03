package com.sriram.util.response.creator

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.response.DefaultResponseCreator

class CommonResponseCreator extends DefaultResponseCreator {
    /**
     * Protected constructor.
     * Use static factory methods in {@link CommonResponseCreator}.
     */
    protected CommonResponseCreator(HttpStatus statusCode) {
        super(statusCode)
    }

    static CommonResponseCreator withResponseDetails(HttpStatus statusCode = HttpStatus.OK, String body, MediaType mediaType, HttpHeaders responseHeaders) {
        CommonResponseCreator commonResponseCreator = new CommonResponseCreator(statusCode)
        commonResponseCreator.body(body).contentType(mediaType)
        commonResponseCreator.headers(responseHeaders)
        return commonResponseCreator
    }
}
