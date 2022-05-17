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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.lang.Nullable;

/**
 * Represents the command data contained in a command.yaml file.
 *
 * <p>
 * This object is used to create the Spring Shell objects that are registered at runtime.
 * </p>
 *
 * @author Eric Bottard
 * @author Mark Pollack
 */
public class Command {

	@Nullable
	private final String name;

	@Nullable
	private final String description;

	@Nullable
	private final String help;

	private final List<CommandOption> options;

	@Nullable
	public String getName() {
		return name;
	}

	@Nullable
	public String getDescription() {
		return description;
	}

	@Nullable
	public String getHelp() {
		return help;
	}

	public List<CommandOption> getOptions() {
		return options;
	}

	public Command() {
		this(null, null, null, null);
	}

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public Command(@JsonProperty("name") @Nullable String name,
			@JsonProperty("description") @Nullable String description,
			@JsonProperty("help") @Nullable String help,
			@JsonProperty("options") List<CommandOption> options) {
		this.name = name;
		this.description = description;
		this.help = help;
		this.options = Objects.requireNonNullElse(options, new ArrayList<>());
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Command{");
		sb.append("name='").append(name).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", options=").append(options);
		sb.append('}');
		return sb.toString();
	}

}
