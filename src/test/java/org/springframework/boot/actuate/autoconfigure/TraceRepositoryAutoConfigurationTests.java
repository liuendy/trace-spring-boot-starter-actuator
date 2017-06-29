package org.springframework.boot.actuate.autoconfigure;

import org.junit.After;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by wonwoo on 2017-06-29.
 */
public class TraceRepositoryAutoConfigurationTests {

	//TODO test

	private AnnotationConfigApplicationContext context;

	@After
	public void close() {
		if (this.context != null) {
			this.context.close();
		}
	}

}