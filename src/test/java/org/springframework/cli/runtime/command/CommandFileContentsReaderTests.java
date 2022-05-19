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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Pollack
 */
public class CommandFileContentsReaderTests {

	@Test
	void deserialization() throws IOException {
		CommandFileContents commandFileContents = CommandFileReader.read(Paths.get("src/test/resources",
				packageAsPath(CommandFileContentsReaderTests.class), "manifest-full-deserialization.yaml"));

		// Test command
		Command command = commandFileContents.getCommand();
		assertThat(command).isNotNull();
		assertThat(command.getName()).isEqualTo("new-demo");
		assertThat(command.getDescription()).isEqualTo("Create a new demo project");

		// Test command options
		List<CommandOption> optionsList = command.getOptions();
		assertThat(optionsList).hasSize(3);
		CommandOption commandOption = optionsList.stream().filter((option) -> "platform".equals(option.getName()))
				.findAny().orElseThrow();
		assertThat(commandOption.getDescription()).isEqualTo("platform to target");
		assertThat(commandOption.getDataType()).isEqualTo("string");
		assertThat(commandOption.getDefaultValue()).isEqualTo("azure");
		assertThat(commandOption.getInputType()).isEqualTo("select");
		assertThat(commandOption.getChoices()).containsKeys("azure", "lambda", "google");

	}


	private static String packageAsPath(Class<?> clazz) {
		return clazz.getPackageName().replace('.', '/');
	}

}
