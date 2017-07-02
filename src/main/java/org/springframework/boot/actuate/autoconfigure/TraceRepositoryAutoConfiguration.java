package org.springframework.boot.actuate.autoconfigure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.actuate.trace.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

/**
 * Created by wonwoo on 2017-06-29.
 */
@Configuration
public class TraceRepositoryAutoConfiguration {

	@ConditionalOnClass(RedisTemplate.class)
	static class RedisTraceRepositoryAutoConfiguration {

		@Bean
		@ConditionalOnMissingBean(TraceRepository.class)
		public RedisTraceRepository redisTraceRepository(RedisTemplate<String, Trace> traceRedisTemplate)
				throws UnknownHostException {
			return new RedisTraceRepository(traceRedisTemplate);
		}

		@Bean
		@ConditionalOnMissingBean(name = "traceRedisTemplate")
		public RedisTemplate<String, Trace> traceRedisTemplate(RedisConnectionFactory redisConnectionFactory)
				throws UnknownHostException {
			RedisTemplate<String, Trace> template = new RedisTemplate<String, Trace>();
			Jackson2JsonRedisSerializer<Trace> serializer = new Jackson2JsonRedisSerializer<Trace>(Trace.class);
			ObjectMapper objectMapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addDeserializer(Trace.class, new TraceDeserializer(objectMapper));
			objectMapper.registerModule(module);
			serializer.setObjectMapper(objectMapper);
			template.setValueSerializer(serializer);
			template.setConnectionFactory(redisConnectionFactory);
			return template;
		}

		static class TraceDeserializer extends JsonObjectDeserializer<Trace> {

			private final ObjectMapper objectMapper;
			private static final String TIME_STAMP = "timestamp";
			private static final String INFO = "info";

			TraceDeserializer(ObjectMapper objectMapper) {
				this.objectMapper = objectMapper;
			}

			@Override
			protected Trace deserializeObject(JsonParser jsonParser,
					DeserializationContext deserializationContext,
					ObjectCodec objectCodec, JsonNode jsonNode) throws IOException {
				JsonNode timestamp = jsonNode.get(TIME_STAMP);
				JsonNode info = jsonNode.get(INFO);
				Map<String, Object> result = objectMapper.convertValue(info,
						new TypeReference<Map<String, Object>>() {});
				return new Trace(new Date(timestamp.asLong()), result);
			}
		}
	}

	@ConditionalOnClass(MongoOperations.class)
	static class MongoTraceRepositoryAutoConfiguration {

		@Bean
		@ConditionalOnMissingBean(TraceRepository.class)
		public MongoTraceRepository mongoTraceRepository(MongoOperations mongoOperations)
				throws UnknownHostException {
			return new MongoTraceRepository(mongoOperations);
		}
	}
		@ConditionalOnMissingBean(TraceRepository.class)
	@Bean
	public InMemoryTraceRepository traceRepository() {
		return new InMemoryTraceRepository();
	}
}
