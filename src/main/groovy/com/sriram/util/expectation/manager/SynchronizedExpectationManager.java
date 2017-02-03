package com.sriram.util.expectation.manager;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.web.client.AbstractRequestExpectationManager;
import org.springframework.test.web.client.RequestExpectation;

import java.io.IOException;
import java.util.*;


public class SynchronizedExpectationManager extends AbstractRequestExpectationManager {
    private final List<RequestExpectation> expectations = Collections.synchronizedList(new LinkedList<RequestExpectation>());
    private final List<ClientHttpRequest> requests = Collections.synchronizedList(new LinkedList<ClientHttpRequest>());
    private final SynchronizedRequestExpectationGroup remainingExpectations = new SynchronizedRequestExpectationGroup();

    @Override
    protected List<RequestExpectation> getExpectations() {
        return this.expectations;
    }

    @Override
    protected List<ClientHttpRequest> getRequests() {
        return this.requests;
    }

    protected void afterExpectationsDeclared() {
        this.remainingExpectations.updateAll(this.getExpectations());
    }

    public ClientHttpResponse validateRequestInternal(ClientHttpRequest request) throws IOException {
        RequestExpectation expectation = this.remainingExpectations.findExpectation(request);

        if (expectation != null) {
            ClientHttpResponse response = expectation.createResponse(request);
            this.remainingExpectations.update(expectation);
            return response;
        }

        throw this.createUnexpectedRequestError(request);
    }

    public void reset() {
        super.reset();
        this.remainingExpectations.reset();
    }

    protected static class SynchronizedRequestExpectationGroup extends RequestExpectationGroup {

        private final Set<RequestExpectation> expectations = Collections.synchronizedSet(new LinkedHashSet<>());

        @Override
        public Set<RequestExpectation> getExpectations() {
            return this.expectations;
        }

        @Override
        public void updateAll(Collection<RequestExpectation> expectations) {
            synchronized (expectations) {
                super.updateAll(expectations);
            }
        }

        @Override
        public RequestExpectation findExpectation(ClientHttpRequest request) throws IOException {
            synchronized (expectations) {
                return super.findExpectation(request);
            }
        }
    }
}
