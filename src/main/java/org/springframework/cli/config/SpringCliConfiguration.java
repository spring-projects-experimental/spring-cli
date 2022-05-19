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

import io.netty.resolver.DefaultAddressResolverGroup;

import org.springframework.boot.autoconfigure.web.reactive.function.client.ReactorNettyHttpClientMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cli.initializr.InitializrClient;
import org.springframework.cli.support.SpringCliUserConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.shell.MethodTargetRegistrar;
import org.springframework.shell.command.CommandCatalog;
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
	public MethodTargetRegistrar dynamicMethodTargetRegistrar() {
		return new DynamicMethodTargetRegistrar();

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
	public InitializrClient initializrClient(WebClient.Builder webClientBuilder, SpringCliProperties upCliProperties) {
		return InitializrClient.builder(webClientBuilder)
				.target(upCliProperties.getInitializr().getBaseUrl())
				.build();
	}

	@Bean
	public SpringCliUserConfig upCliUserConfig() {
		return new SpringCliUserConfig();
	}
}
