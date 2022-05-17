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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Holds the command data model that describes a command and possibly additional
 * metadata in the future
 *
 * @author Eric Bottard
 * @author Mark Pollack
 */
public final class CommandManifest {

	private final Command command;

	public CommandManifest() {
		this(null);
	}

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public CommandManifest(@JsonProperty("command") Command command) {
		this.command = Objects.requireNonNullElse(command, new Command());
	}

	public Command getCommand() {
		return command;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("CommandManifest{");
		sb.append("command=").append(command);
		sb.append('}');
		return sb.toString();
	}

}
