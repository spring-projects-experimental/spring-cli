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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cli.runtime.engine.GeneratorResolver;
import org.springframework.shell.command.CommandCatalog;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.command.CommandRegistration.Builder;
import org.springframework.shell.command.CommandRegistration.OptionSpec;
import org.springframework.util.StringUtils;

/**
 * Helper object for registering commands with Spring CLI at runtime.
 *
 * It takes the results of scanning the `.spring/recipes` directory and registers
 * found commands with Spring Shell's CommandCatalog.
 *
 * All commands execute the same code, an instance of SpringShellGeneratorCommand.
 * The argument GeneratorResolver is passed into SpringShellGeneratorCommand.
 *
 * @author Mark Pollack
 */
public class SpringShellDynamicCommandRegistrar {

	private final Logger logger = LoggerFactory.getLogger(SpringShellDynamicCommandRegistrar.class);

	public void registerSpringCliCommands(CommandCatalog commandCatalog,
			CommandScanResults results,
			GeneratorResolver generatorResolver) {



		final Map<Command, List<Command>> commandSubcommandMap = results.getCommandSubcommandMap();
		for (Entry<Command, List<Command>> stringListEntry : commandSubcommandMap.entrySet()) {
			Command command = stringListEntry.getKey();
			List<Command> subCommandList = stringListEntry.getValue();

			String commandName = command.getName();

			for (Command subCommand : subCommandList) {
				String subCommandName = subCommand.getName();


				DynamicCommand dynamicCommand
						= new DynamicCommand(commandName, subCommandName, null);

				Builder builder = CommandRegistration.builder()
						.command(commandName + " " + subCommandName)
						.group("Dynamic Commands")
						.description(subCommand.getDescription())
						.withTarget()
							.method(dynamicCommand, "execute")
						.and();

				List<CommandOption> commandOptions = subCommand.getOptions();
				for (CommandOption commandOption : commandOptions) {
					if (StringUtils.hasText(commandOption.getName())) {
						addOption(commandOption, builder);
					}
					else {
						logger.warn("Option name not provided in subcommand " + subCommandName);
					}
				}
				logger.info("Adding command/subcommand " + commandName + "/" + subCommandName);
				CommandRegistration commandRegistration = builder.build();
				commandCatalog.register(commandRegistration);
			}
		}
	}

	private void addOption(CommandOption commandOption, Builder builder) {
		OptionSpec optionSpec = builder.withOption();
		if (StringUtils.hasText(commandOption.getName())) {
			optionSpec.longNames(commandOption.getName());
		}
		if (StringUtils.hasText(commandOption.getParamLabel())) {
			//TODO - there is no paramLabel in Spring Shell
		}
		if (StringUtils.hasText(commandOption.getDescription())) {
			optionSpec.description(commandOption.getDescription());
		}
		Optional<Class> clazz = JavaTypeConverter.getJavaClass(commandOption.getDataType());
		if (clazz.isPresent()) {
			optionSpec.type(clazz.get());
		}
		if (StringUtils.hasText(commandOption.getDefaultValue())) {
			optionSpec.defaultValue(commandOption.getDefaultValue());
		}
		if (commandOption.isRequired()) {
			optionSpec.required(true);
		}
	}

}
