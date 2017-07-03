package org.springframework.boot.actuate.trace;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by wonwoo on 2017-06-29.
 */
public class RedisTraceRepositoryTests {

	private RedisTraceRepository repository;
	private RedisTemplate<String, Trace> traceRedisTemplate;

	@Before
	public void setup() {
		traceRedisTemplate = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
		repository = new RedisTraceRepository(traceRedisTemplate);
	}

	@Test
	public void configTest() {
		repository.setStartKey("s");
		repository.setEndKey("e");
		repository.setRedisKey("r");
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("header", "foo");
		info.put("body", "bar");
		Date now = new Date();
		Trace trace = new Trace(now, info);
		given(traceRedisTemplate.opsForList().range(anyString(), anyLong(), anyLong())).willReturn(Collections.singletonList(trace));
		repository.findAll();
		verify(traceRedisTemplate.opsForList()).range("r", 0, 0);
	}

	@Test
	public void findAllTest() {
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("header", "foo");
		info.put("body", "bar");
		Date now = new Date();
		Trace trace = new Trace(now, info);
		given(traceRedisTemplate.opsForList().range(anyString(), anyLong(), anyLong())).willReturn(Collections.singletonList(trace));
		List<Trace> traces = repository.findAll();
		assertThat(traces).hasSize(1);
		assertThat(traces.get(0).getTimestamp()).isEqualTo(now);
		assertThat(traces.get(0).getInfo()).isEqualTo(info);
	}
}