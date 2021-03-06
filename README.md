HAL+JSON feed consumer
======================

[![Build Status](https://travis-ci.org/qmetric/hal-feed-consumer.png)](https://travis-ci.org/qmetric/hal-feed-consumer)

Java library used to consume [HAL+JSON](http://stateless.co/hal_specification.html) feeds produced by [hal-feed-server](https://github.com/qmetric/hal-feed-server).

Features
---------

* Processes feed entries
* Supports multiple consumers - refer to [Competing consumer pattern](#competing-consumer-pattern)
* Pre-configured health checks and metrics provided


Usage
-----

First, configure a data store used by the consumer to track which feed entries have already been consumed.
An [Amazon SimpleDB](http://aws.amazon.com/simpledb/) based implementation is supplied as part of this library:

```java
final AmazonSimpleDB simpleDBClient = new AmazonSimpleDBClient(new BasicAWSCredentials("access key", "secret key"));
simpleDBClient.setRegion(getRegion(EU_WEST_1));

final FeedTracker feedTracker = new SimpleDBFeedTracker(simpleDBClient, "your-sdb-domain");
```

Then, build and start a feed consumer:

```java
final FeedConsumerConfiguration feedConsumerConfiguration = new FeedConsumerConfiguration()
                .fromUrl("http://your-feed-endpoint")
                .withFeedTracker(feedTracker)
                .consumeEachEntryWith(new ConsumeAction() {
                                          @Override public void consume(final FeedEntry feedEntry) {
                                              System.out.println("write your code here to consume the next feed entry...");
                                      }})
                .pollForNewEntriesEvery(5, MINUTES);

feedConsumerConfiguration.build().start()
```

Library available from [Maven central repository](http://search.maven.org/)

```
<dependency>
    <groupId>com.qmetric</groupId>
    <artifactId>hal-feed-consumer</artifactId>
    <version>${VERSION}</version>
</dependency>
```


Health checks and metrics
-------------------------

Pre-configured health checks and metrics are available by default using [codahale metrics](http://metrics.codahale.com/):

Codahale metrics and health check registries can be retrieved from your feed consumer configuration:

```java
final HealthCheckRegistry healthCheckRegistry = feedConsumerConfiguration.getHealthCheckRegistry();

final MetricRegistry metricRegistry = feedConsumerConfiguration.getMetricRegistry();
```


Competing consumer pattern
--------------------------

Supports the competing consumer pattern. Multiple consumers can read and process entries safely from the same feed.

Note: In order to allow concurrency between multiple consumers, feed entries may be processed in an order differing from their publish date.