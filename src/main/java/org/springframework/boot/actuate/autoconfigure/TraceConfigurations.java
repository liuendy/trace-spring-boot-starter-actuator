package org.springframework.boot.actuate.autoconfigure;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Created by wonwoo on 2017-07-03.
 */
class TraceConfigurations {

	private static final Map<TraceType, Class<?>> MAPPINGS;

	static {
		Map<TraceType, Class<?>> mappings = new HashMap<TraceType, Class<?>>();
		mappings.put(TraceType.MEMORY, InMemoryTraceRepositoryAutoConfiguration.class);
		mappings.put(TraceType.MONGO, MongoTraceRepositoryAutoConfiguration.class);
		mappings.put(TraceType.REDIS, RedisTraceRepositoryAutoConfiguration.class);
		MAPPINGS = Collections.unmodifiableMap(mappings);
	}

	private TraceConfigurations() {
	}

	public static String getConfigurationClass(TraceType traceType) {
		Class<?> configurationClass = MAPPINGS.get(traceType);
		Assert.state(configurationClass != null, "Unknown cache type " + traceType);
		return configurationClass.getName();
	}

	public static TraceType getType(String configurationClassName) {
		for (Map.Entry<TraceType, Class<?>> entry : MAPPINGS.entrySet()) {
			if (entry.getValue().getName().equals(configurationClassName)) {
				return entry.getKey();
			}
		}
		throw new IllegalStateException(
				"Unknown configuration class " + configurationClassName);
	}

}
