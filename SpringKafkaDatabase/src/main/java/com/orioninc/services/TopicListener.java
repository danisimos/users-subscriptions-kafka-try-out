package com.orioninc.services;

import com.orioninc.models.Interval;
import com.orioninc.models.ProcessedIntervalSubscriptions;
import com.orioninc.models.Subscription;

public interface TopicListener {
    void listenProcessedIntervalSubscriptions(ProcessedIntervalSubscriptions data, Interval interval, String topicName);
    void listenMetricCount(String timestamp, String topicName, Subscription subscription);
}
