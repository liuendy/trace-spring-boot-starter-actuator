package org.springframework.boot.actuate.autoconfigure;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wonwoo on 2017-07-03.
 */
@Configuration
@Conditional(TraceCondition.class)
public class InMemoryTraceRepositoryAutoConfiguration extends TraceRepositoryAutoConfiguration {
}

