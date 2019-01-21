/*
 * $id$
 *
 */
package ru.vybornov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Статус счетчиков.
 *
 * @author vybornov
 * @version $Revision$
 */
public class Stats
{
    @JsonProperty("currencyRequests")
    private Map<Integer, Long> currencies = new ConcurrentHashMap<>();

    @JsonProperty("success")
    private long successRequests = 0;

    @JsonProperty("fails")
    private long failedRequests = 0;

    public Map<Integer, Long> getCurrencies()
    {
        return currencies;
    }

    public long getSuccessRequests()
    {
        return successRequests;
    }

    public void setSuccessRequests(long successRequests)
    {
        this.successRequests = successRequests;
    }

    public long getFailedRequests()
    {
        return failedRequests;
    }

    public void setFailedRequests(long failedRequests)
    {
        this.failedRequests = failedRequests;
    }
}
