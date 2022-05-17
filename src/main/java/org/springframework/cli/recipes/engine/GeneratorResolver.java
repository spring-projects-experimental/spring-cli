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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;

import org.springframework.cli.SpringCliException;

/**
 * Given a "command" in the form of a noun and a verb, this resolves a
 * {@link GeneratorExecutor} capable of running a generator.
 *
 * <p>
 * The basic implementation looks for a {@code .spring/recipes/[noun]/[verb]/} subdirectory
 * relative to the <strong>working directory</strong>, but a more complex, layered
 * strategy (for example looking in a {@code .generator} dir in the user home directory)
 * could be added.
 * </p>
 * <p>
 * Can be used as a singleton in one-off contexts (eg a CLI running in execute-and-quit
 * style) or as a prototype in long running contexts, passing in a different
 * <strong>working directory</strong> each time.
 * </p>
 *
 * @author Mark Pollack
 * @author Eric Bottard
 */
public class GeneratorResolver {

	private final Path workingDirectory;

	private final Iterable<ModelPopulator> modelPopulators;

	public static File GENERATOR_DIR = new File(".generators");

	public GeneratorResolver(Path workingDirectory, Iterable<ModelPopulator> modelPopulators) {
		this.workingDirectory = Objects.requireNonNull(workingDirectory);
		this.modelPopulators = Objects.requireNonNullElse(modelPopulators, Collections.emptySet());
	}

	public GeneratorExecutor resolve(String noun, String verb) {
		final Path path = workingDirectory.resolve(".generators").resolve(noun).resolve(verb);
		if (Files.isDirectory(path)) {
			return new GeneratorExecutor(path, modelPopulators);
		}
		else {
			throw new SpringCliException(
					String.format("Could not resolve generator %s/%s against %s", noun, verb, workingDirectory));
		}
	}

}
