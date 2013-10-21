package com.qmetric.feed.consumer.metrics;

import com.codahale.metrics.health.HealthCheck;
import com.qmetric.feed.consumer.store.FeedTracker;

import static com.codahale.metrics.health.HealthCheck.Result.healthy;
import static com.codahale.metrics.health.HealthCheck.Result.unhealthy;

public class ConsumedStoreConnectivityHealthCheck extends HealthCheck
{
    private final FeedTracker feedTracker;

    public ConsumedStoreConnectivityHealthCheck(final FeedTracker feedTracker)
    {
        this.feedTracker = feedTracker;
    }

    @Override protected Result check() throws Exception
    {
        try
        {
            feedTracker.checkConnectivity();

            return healthy("Consumed store connectivity is healthy");
        }
        catch (final Exception exception)
        {
            return unhealthy(exception);
        }
    }
}

