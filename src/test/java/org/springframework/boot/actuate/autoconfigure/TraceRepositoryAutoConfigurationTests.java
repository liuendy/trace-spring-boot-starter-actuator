package org.springframework.boot.actuate.autoconfigure;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.actuate.trace.InMemoryTraceRepository;
import org.springframework.boot.actuate.trace.RedisTraceRepository;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wonwoo on 2017-06-29.
 */
public class TraceRepositoryAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void configuresTraceRedisTemplateTest() throws Exception {
		load();
		assertThat(context.getBean("traceRedisTemplate", RedisTemplate.class)).isNotNull();
	}

	@Test
	public void configuresRedisTraceRepositoryTest() throws Exception {
		load();
		assertThat(context.getBean(RedisTraceRepository.class)).isNotNull();
	}

	@Test
	public void configuresInMemoryTraceRepositoryTest() throws Exception {
		load(InMemoryTraceRepositoryConfig.class);
		assertThat(context.getBean(InMemoryTraceRepository.class)).isNotNull();
		assertThat(context.getBeansOfType(RedisTraceRepository.class)).hasSize(0);
	}

	private void load(String... environment) {
		load(null, environment);
	}

	private void load(Class<?> config, String... environment) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		EnvironmentTestUtils.addEnvironment(ctx, environment);
		if (config != null) {
			ctx.register(config);
		}
		ctx.register(RedisAutoConfiguration.class, TraceRepositoryAutoConfiguration.class);
		ctx.refresh();
		this.context = ctx;
	}

	@Configuration
	static class InMemoryTraceRepositoryConfig {
		@Bean
		public InMemoryTraceRepository traceRepository() {
			return new InMemoryTraceRepository();
		}
	}
}