package org.springframework.boot.actuate.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by wonwoo on 2017-07-03.
 */
@ConfigurationProperties("spring.trace")
public class TraceProperties {
	private TraceType type;

	public TraceType getType() {
		return type;
	}

	public void setType(TraceType type) {
		this.type = type;
	}
}
