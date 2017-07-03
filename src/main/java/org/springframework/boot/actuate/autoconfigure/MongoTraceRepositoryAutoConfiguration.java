package org.springframework.boot.actuate.autoconfigure;

import java.net.UnknownHostException;

import org.springframework.boot.actuate.trace.MongoTraceRepository;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Created by wonwoo on 2017-07-03.
 */
@Configuration
@ConditionalOnClass(MongoOperations.class)
@Conditional(TraceCondition.class)
public class MongoTraceRepositoryAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(TraceRepository.class)
	public MongoTraceRepository mongoTraceRepository(MongoOperations mongoOperations)
			throws UnknownHostException {
		return new MongoTraceRepository(mongoOperations);
	}
}
