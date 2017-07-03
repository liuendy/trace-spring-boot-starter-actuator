package org.springframework.boot.actuate.autoconfigure;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.actuate.trace.InMemoryTraceRepository;
import org.springframework.boot.actuate.trace.MongoTraceRepository;
import org.springframework.boot.actuate.trace.RedisTraceRepository;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wonwoo on 2017-07-03.
 */
public class TraceAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void inMemoryConfigTraceTest() {
		load("spring.trace.type=memory");
		assertThat(context.getBean(InMemoryTraceRepository.class)).isNotNull();
		assertThat(context.getBeansOfType(RedisTraceRepository.class)).hasSize(0);
		assertThat(context.getBeansOfType(MongoTraceRepository.class)).hasSize(0);
	}

	@Test
	public void redisConfigTraceTest() {
		load(RedisAutoConfiguration.class, "spring.trace.type=redis");
		assertThat(context.getBean(RedisTraceRepository.class)).isNotNull();
		assertThat(context.getBeansOfType(InMemoryTraceRepository.class)).hasSize(0);
		assertThat(context.getBeansOfType(MongoTraceRepository.class)).hasSize(0);
	}

	@Test
	public void mongoConfigTraceTest() {
		load("spring.trace.type=mongo", MongoAutoConfiguration.class, MongoDataAutoConfiguration.class);
		assertThat(context.getBean(MongoTraceRepository.class)).isNotNull();
		assertThat(context.getBeansOfType(InMemoryTraceRepository.class)).hasSize(0);
		assertThat(context.getBeansOfType(RedisTraceRepository.class)).hasSize(0);
	}

	private void load(String environment, Class<?>... config) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		if (environment != null) {
			EnvironmentTestUtils.addEnvironment(ctx, environment);
		}
		if (config != null) {
			ctx.register(config);
		}
		ctx.register(TraceAutoConfiguration.class);
		ctx.refresh();
		this.context = ctx;
	}

	private void load(Class<?> config, String... environment) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		EnvironmentTestUtils.addEnvironment(ctx, environment);
		if (config != null) {
			ctx.register(config);
		}
		ctx.register(TraceAutoConfiguration.class);
		ctx.refresh();
		this.context = ctx;
	}
}