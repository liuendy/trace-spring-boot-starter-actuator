package org.springframework.boot.actuate.trace;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by wonwoo on 2017-06-29.
 */
@ConfigurationProperties(prefix = "trace.redis")
public class RedisTraceRepository implements TraceRepository {

	private String startKey = "start";
	private String endKey = "end";
	private String redisKey = "trace";

	private final RedisTemplate<String, Trace> traceRedisTemplate;

	public RedisTraceRepository(RedisTemplate<String, Trace> traceRedisTemplate) {
		this.traceRedisTemplate = traceRedisTemplate;
	}

	@Override
	public List<Trace> findAll() {
		HttpServletRequest request = getRequest();
		long start = nullSafeValue(request, startKey);
		long end = nullSafeValue(request, endKey);
		return traceRedisTemplate.opsForList().range(redisKey, start, end);
	}

	@Override
	public void add(Map<String, Object> traceInfo) {
		Trace trace = new Trace(new Date(), traceInfo);
		traceRedisTemplate.opsForList().rightPush(redisKey, trace);
	}

	private static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) requestAttributes).getRequest();
		}
		return null;
	}

	public void setStartKey(String startKey) {
		this.startKey = startKey;
	}

	public void setEndKey(String endKey) {
		this.endKey = endKey;
	}

	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}

	private long nullSafeValue(HttpServletRequest request, String key) {
		if(request == null) {
			return 0L;
		}
		String parameter = request.getParameter(key);
		if (parameter != null) {
			return Long.valueOf(parameter);
		}
		return 0L;
	}
}

