package com.hpe;

import com.hpe.data.Counters;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

/**
 * An in-memory database for storing the counters for metrics and KPIs per file read.
 */
@Repository
public class Cache extends ConcurrentHashMap<String, Counters> {
}
