package org.springframework.boot.actuate.trace;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by wonwoo on 2017-06-29.
 */
@ConfigurationProperties(prefix = "trace.redis")
public class RedisTraceRepository extends AbstractTraceRepository implements TraceRepository {
	private String redisKey = "trace";

	private final RedisTemplate<String, Trace> traceRedisTemplate;

	public RedisTraceRepository(RedisTemplate<String, Trace> traceRedisTemplate) {
		this.traceRedisTemplate = traceRedisTemplate;
	}

	@Override
	public void add(Trace trace) {
		traceRedisTemplate.opsForList().rightPush(redisKey, trace);
	}

	@Override
	public List<Trace> findAll(HttpServletRequest request, Pageable pageable) {
		// FIXME
		return traceRedisTemplate.opsForList().range(redisKey, pageable.getOffset(), pageable.getOffset() + pageable.getPageSize() - 1);
	}

	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
}

