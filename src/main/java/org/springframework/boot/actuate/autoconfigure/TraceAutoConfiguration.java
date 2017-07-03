package org.springframework.boot.actuate.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by wonwoo on 2017-07-03.
 */
@Configuration
@EnableConfigurationProperties(TraceProperties.class)
@AutoConfigureBefore(TraceRepositoryAutoConfiguration.class)
@Import(TraceAutoConfiguration.TraceConfigurationImportSelector.class)
public class TraceAutoConfiguration {

	static class TraceConfigurationImportSelector implements ImportSelector {

		@Override
		public String[] selectImports(AnnotationMetadata importingClassMetadata) {
			TraceType[] types = TraceType.values();
			String[] imports = new String[types.length];
			for (int i = 0; i < types.length; i++) {
				imports[i] = TraceConfigurations.getConfigurationClass(types[i]);
			}
			return imports;
		}
	}
}
