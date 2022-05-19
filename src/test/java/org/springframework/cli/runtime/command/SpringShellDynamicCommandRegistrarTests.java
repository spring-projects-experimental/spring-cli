/*
 * Copyright 2021 the original author or authors.
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


package org.springframework.cli.runtime.command;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.cli.runtime.engine.GeneratorResolver;
import org.springframework.shell.command.CommandCatalog;
import org.springframework.shell.command.CommandRegistration;

import static org.assertj.core.api.Assertions.assertThat;


public class SpringShellDynamicCommandRegistrarTests {

	@Test
	void testRegistrar() {

		// Create the objects that represent commands to be registered at runtime
		Map<Command, List<Command>> commands = new LinkedHashMap<>();

		// Create first command/subcommand
		Command k8sSimple = new Command("k8s-simple", "command description", null);
		Command k8sSimpleNew = new Command("new", "subcommand description", null);
		CommandOption option1 = new CommandOption.Builder()
				.withName("with-gusto")
				.withDataType("boolean")
				.withDescription("what a nice simple option")
				.withDefaultValue("true")
				.withRequired(true)
				.build();
		k8sSimpleNew.getOptions().add(option1);

		CommandOption option2 = new CommandOption.Builder()
				.withName("with-greeting")
				.withDataType("string")
				.build();
		k8sSimpleNew.getOptions().add(option2);

		// Create second command/subcommand
		Command k8sSimpleNewServices = new Command("new-services",
				"Create the Kubernetes resource files for the application's services.", null);
		CommandOption optionService1 = new CommandOption.Builder().withName("services-with-gusto").build();
		k8sSimpleNewServices.getOptions().add(optionService1);
		CommandOption optionService2 = new CommandOption.Builder().withName("services-with-greeting").build();
		k8sSimpleNewServices.getOptions().add(optionService2);

		commands.put(k8sSimple, Arrays.asList(k8sSimpleNew, k8sSimpleNewServices));

		// package up the commands in a result object
		CommandScanResults commandScanResults = new CommandScanResults(commands);

		SpringShellDynamicCommandRegistrar springCliDynamicCommandRegistrar = new SpringShellDynamicCommandRegistrar();
		Path cwd = Path.of("").toAbsolutePath();
		CommandCatalog commandCatalog = CommandCatalog.of();
		springCliDynamicCommandRegistrar.registerSpringCliCommands(commandCatalog,
				commandScanResults,
				new GeneratorResolver(cwd, null));

		Map<String, CommandRegistration> commandRegistrationMap = commandCatalog.getRegistrations();

		assertThat(commandCatalog.getRegistrations()).hasSize(2);
		assertThat(commandCatalog.getRegistrations().get("k8s-simple new")).satisfies(registration -> {
			assertThat(registration.getDescription()).isEqualTo("subcommand description");
			assertThat(registration.getOptions()).hasSize(2);
			assertThat(registration.getOptions().get(0)).satisfies(option -> {
				assertThat(option.getLongNames()).contains("with-gusto");
				assertThat(option.getType().getType()).isEqualTo(Boolean.class);
				assertThat(option.getDescription()).isEqualTo("what a nice simple option");
				assertThat(option.getDefaultValue()).isEqualTo("true");
				assertThat(option.isRequired()).isTrue();
			});
			assertThat(registration.getOptions().get(1)).satisfies(option -> {
				assertThat(option.getLongNames()).contains("with-greeting");
				assertThat(option.getType().getType()).isEqualTo(String.class);
			});
		});

	}
}
