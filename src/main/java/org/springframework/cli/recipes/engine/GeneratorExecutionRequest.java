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

package org.springframework.cli.recipes.engine;

import java.nio.file.Path;
import java.util.Map;

/**
 * Encapsulates the request to execute a generator against a given model, asking to
 * generate content in a given {@link #destinationPath}.
 *
 * @author Eric Bottard
 */
public class GeneratorExecutionRequest {

	/**
	 * Where to generate files. Unused for now, assumed to be the project root by
	 * {@link GeneratorExecutor} but if it could be set, would belong to this request.
	 */
	private final Path destinationPath;

	private final Map<String, Object> model;

	/**
	 * Whether this execution refers to an initial code generation step.
	 */
	private final boolean initialCodeGeneration;

	public GeneratorExecutionRequest(Map<String, Object> model, boolean initialCodeGeneration, Path destinationPath) {
		this.model = model;
		this.initialCodeGeneration = initialCodeGeneration;
		this.destinationPath = destinationPath;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public boolean isInitialCodeGeneration() {
		return initialCodeGeneration;
	}

	public Path getDestinationPath() {
		return destinationPath;
	}

}
