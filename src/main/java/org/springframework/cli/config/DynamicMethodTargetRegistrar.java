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


package org.springframework.cli.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cli.runtime.command.CommandScanResults;
import org.springframework.cli.runtime.command.CommandScanner;
import org.springframework.cli.runtime.command.SpringShellDynamicCommandRegistrar;
import org.springframework.cli.runtime.engine.GeneratorResolver;
import org.springframework.shell.MethodTargetRegistrar;
import org.springframework.shell.command.CommandCatalog;

public class DynamicMethodTargetRegistrar implements MethodTargetRegistrar {


	private static final Logger logger = LoggerFactory.getLogger(DynamicMethodTargetRegistrar.class);


	public DynamicMethodTargetRegistrar() {

	}

	@Override
	public void register(CommandCatalog commandCatalog) {
		SpringShellDynamicCommandRegistrar springCliDynamicCommandRegistrar = new SpringShellDynamicCommandRegistrar();
		Path cwd = Path.of("").toAbsolutePath();
		Path pathToUse = Paths.get(cwd.toString(), ".spring", "commands");
		logger.debug("Looking for dynamic commands in directory " + pathToUse);
		CommandScanner scanner = new CommandScanner(pathToUse);
		CommandScanResults commandScanResults = scanner.scan();
		logger.debug("Found commands " + commandScanResults);
		springCliDynamicCommandRegistrar.registerSpringCliCommands(commandCatalog,
				commandScanResults,
				new GeneratorResolver(cwd, null));

	}
}
