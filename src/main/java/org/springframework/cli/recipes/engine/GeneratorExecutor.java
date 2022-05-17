/*
 * Copyright 2019 the original author or authors.
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

package org.springframework.cli.recipes.engine;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cli.recipes.command.CommandManifest;

/**
 * Executes the actions defined by files with front-matter sections that describe
 * an action to take.  Usually the same directory as where an optional command.yaml
 * file is located that describes options that can be pssed into the command.
 *
 * <p>
 * At a high level, this class
 * <ul>
 * <li>enriches the execution model with project specific entities such as the maven
 * model, gradle model, or the current date),</li>
 * <li>lastly, processes templates with front matter, which can in turn create/modify
 * files in the working directory.</li>
 * </ul>
 * </p>
 *
 * @author Mark Pollack
 * @author Eric Bottard
 */
public class GeneratorExecutor {

	private static final Logger logger = LoggerFactory.getLogger(GeneratorExecutor.class);

	/**
	 * The path of the directory where the command.yaml file can (optionally) be found.
	 */
	private final Path commandPath;

	/**
	 * Used to iteratively add variables to the model before execution.
	 */
	private final Iterable<ModelPopulator> modelPopulators;

	public GeneratorExecutor(Path commandPath, Iterable<ModelPopulator> modelPopulators) {
		this.commandPath = Objects.requireNonNull(commandPath);
		this.modelPopulators = Objects.requireNonNullElse(modelPopulators, Collections.emptySet());
	}

	public CommandResponse execute(GeneratorExecutionRequest generatorExecutionRequest) {

		Path destinationPath = generatorExecutionRequest.getDestinationPath();
		logger.info("Executing generator from {} into {}", commandPath, destinationPath);

		// TODO process files that have font-matter sections and execute the actions contained therein

		return new CommandResponse("Generator executed successfully");
	}


}
