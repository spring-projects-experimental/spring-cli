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


package org.springframework.cli.recipes.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cli.recipes.engine.GeneratorResolver;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandParser.CommandParserResult;
import org.springframework.shell.command.CommandParser.CommandParserResults;

/**
 * Spring Shell command object that is executed for all dynamic commands discovered
 * at runtime.
 *
 * It uses the GeneratorResolver to read files contained in the command/subcommand directory
 * as specified by the arguments commandName and subCommandName
 *
 * Delegates to
 * and interprets files

 */
public class SpringShellGeneratorCommand {

	private GeneratorResolver generatorResolver;

	private String commandName;

	private String subCommandName;

	public SpringShellGeneratorCommand(String commandName, String subCommandName, GeneratorResolver generatorResolver) {
		this.commandName = commandName;
		this.subCommandName = subCommandName;
		this.generatorResolver = generatorResolver;
	}

	public void runGenerator(CommandContext commandContext) {

		Map<String, Object> model = new HashMap<>();

		//addMatchedOptions(model);

		//addUnmatchedOptions(model);

		lookupAndExecute(commandContext, model);
	}

	private void lookupAndExecute(CommandContext commandContext, Map<String, Object> model) {
		CommandParserResults parserResults = commandContext.getParserResults();
		List<CommandParserResult> commandParserResultsList = parserResults.results();

	}


}
