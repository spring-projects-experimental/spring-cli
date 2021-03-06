/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cli.config;

import java.time.Duration;
import java.util.Collection;

import io.netty.resolver.DefaultAddressResolverGroup;

import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cli.initializr.InitializrClientCache;
import org.springframework.cli.runtime.engine.model.MavenModelPopulator;
import org.springframework.cli.runtime.engine.model.ModelPopulator;
import org.springframework.cli.runtime.engine.model.RootPackageModelPopulator;
import org.springframework.cli.runtime.engine.model.SystemModelPopulator;
import org.springframework.cli.support.SpringCliUserConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.shell.MethodTargetRegistrar;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for cli related beans.
 *
 * @author Janne Valkealahti
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SpringCliProperties.class)
public class SpringCliConfiguration {

	@Bean
	public ModelPopulator systemModelPopulator() {
		return new SystemModelPopulator();
	}

	@Bean
	public ModelPopulator mavenModelPopulator() {
		return new MavenModelPopulator();
	}

	@Bean
	public ModelPopulator rootPackageModelPopulator() {
		return new RootPackageModelPopulator();
	}

	@Bean
	public MethodTargetRegistrar dynamicMethodTargetRegistrar(Collection<ModelPopulator> modelPopulators) {
		return new DynamicMethodTargetRegistrar(modelPopulators);
	}

    @Bean
    public ReactorResourceFactory reactorClientResourceFactory() {
		// change default 2s quiet period so that context terminates more quick
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setShutdownQuietPeriod(Duration.ZERO);
        return factory;
    }

	@Bean
	ReactorNettyHttpClientMapper reactorNettyHttpClientMapper() {
        // workaround for native/graal issue
        // https://github.com/spring-projects-experimental/spring-native/issues/1319
		// There's also issue #4304 on https://github.com/oracle/graal
		return httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE);
	}

	@Bean
	InitializrClientCache initializrClientCache(WebClient.Builder webClientBuilder) {
		return new InitializrClientCache(webClientBuilder);
	}

	@Bean
	public SpringCliUserConfig upCliUserConfig() {
		return new SpringCliUserConfig();
	}
}
