package com.example.boxinator.ratelimiter;

import io.github.bucket4j.Bucket;

public interface RateLimiterService {
    public Bucket resolveBucket(String bucketIdentifier);
}
