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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cli.SpringCliException;

// TODO what about .spring/commands instead
/**
 * Scans a directory, by default .spring/recipes in the current working
 * directory, and finds all command and subcommand directories.
 *
 * For each command/subcommand directory, a Command object is created. The
 * Command objects are then used to create the Spring Shell objects that
 * are registered at runtime.
 *
 * @author Mark Pollack
 */
public class CommandScanner {

	private final Logger logger = LoggerFactory.getLogger(CommandScanner.class);

	public CommandScanner() {

	}

	/**
	 * Scan the generator directory and create Command objects for later use in
	 * registering with Spring Shell at runtime.
	 * @return A results object that contains the commands and subcommand hierarchy as
	 * Command objects.
	 */
	public CommandScanResults scan() {
		Map<Command, List<Command>> results = new HashMap<>();

		final Optional<File> generatorsDirectory = getGeneratorDirectory();
		if (generatorsDirectory.isPresent()) {
			// Look for command/subcommand structures
			File[] files = generatorsDirectory.get().listFiles();
			for (File commandDirectory : files) {
				if (commandDirectory.isDirectory() && !commandDirectory.isHidden()) {
					Command command = getCommandObject(commandDirectory);
					File[] subFiles = commandDirectory.listFiles();
					List<Command> subCommandList = new ArrayList<>();
					for (File subCommandDirectory : subFiles) {
						if (subCommandDirectory.isDirectory()) {
							Command subCommand = getCommandObject(subCommandDirectory);
							subCommandList.add(subCommand);
						}
					}
					results.put(command, subCommandList);
				}
			}
		}
		return new CommandScanResults(results);
	}

	/**
	 * Creates a {@link Command} object for each generator command and intermediary
	 * directory.
	 *
	 * <p>
	 * If a "generator.yaml" file is found in the directory, values from that file are
	 * used to populate the {@link Command} object. The properties "name" and
	 * "description" will always be set with reasonable defaults if the generator.yaml
	 * file is not found.
	 * </p>
	 */
	private Command getCommandObject(File directory) {
		File manifestFile = new File(directory, "generator.yaml");
		Command command = new Command();
		if (manifestFile.exists()) {
			logger.debug("Found generator.yaml file in " + manifestFile.getAbsolutePath());
			final CommandManifest manifest;
			try {
				manifest = CommandManifestReader.read(manifestFile.toPath());
			}
			catch (IOException e) {
				e.printStackTrace();
				throw new SpringCliException("Failed to read " + manifestFile, e);
			}
			command = manifest.getCommand();
		}
		if (command.getDescription() == null) {
			command = new Command(command.getName(), directory.getName() + " commands", command.getHelp(), command.getOptions());
		}
		if (command.getName() == null) {
			command = new Command(directory.getName(), command.getDescription(), command.getHelp(), command.getOptions());
		}
		return command;
	}

	/**
	 * Return the existing directory to source generators from, or {@code empty} if such a
	 * directory doesn't exist.
	 */
	private Optional<File> getGeneratorDirectory() {
		// TODO - allow for looking in the home directory and then fall back to local directory
		final File dir = new File(".spring/recipes");
		return dir.isDirectory() ? Optional.of(dir) : Optional.empty();
	}

}
