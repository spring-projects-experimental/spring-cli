/*
 * Copyright 2020 the original author or authors.
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

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Pollack
 */
public class CommandScannerTests {

	@Test
	void test() {
		File resourceDir = new File("src/test/resources/command-scanner-tests");
		CommandScanner scanner = new CommandScanner(resourceDir.toPath());
		CommandScanResults results = scanner.scan();
		final Map<Command, List<Command>> commandSubcommandMap = results.getCommandSubcommandMap();

		// There is a `.generators` hidden directory that is ignore, so this will be equal
		// to 1
		assertThat(commandSubcommandMap).size().isEqualTo(1);

		for (Entry<Command, List<Command>> commandListEntry : commandSubcommandMap.entrySet()) {
			Command command = commandListEntry.getKey();
			assertThat(command.getName()).isEqualTo("k8s-simple");
			// Get the sub-commands for k8s-simple
			final List<Command> commandListEntryValue = commandListEntry.getValue();
			List<String> subCommandNameList = commandListEntryValue.stream().map(Command::getName)
					.collect(Collectors.toList());
			System.out.println(subCommandNameList);
			assertThat(subCommandNameList).containsExactlyInAnyOrder("new", "new-services");
			for (Command subCommand : commandListEntryValue) {
				if (subCommand.getName().equals("new")) {
					assertThat(subCommand.getDescription())
							.isEqualTo("Create the Kubernetes resource files for the application");
					List<CommandOption> optionsList = subCommand.getOptions();
					CommandOption commandOption = optionsList.stream()
							.filter((option) -> "platform".equals(option.getName())).findAny().orElseThrow();
					assertThat(commandOption.getDescription()).isEqualTo("platform to target");
					assertThat(commandOption.getDataType()).isEqualTo("string");
					assertThat(commandOption.getDefaultValue()).isEqualTo("azure");
					assertThat(commandOption.getInputType()).isEqualTo("select");
					assertThat(commandOption.getChoices()).containsKeys("azure", "lambda", "google");
				}
			}
		}
	}

}
