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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import org.springframework.lang.Nullable;

/**
 * Represents the options for command data contained in a command.yaml file.
 *
 * @author Mark Pollack
 * @author Eric Bottard
 */
@JsonDeserialize(builder = CommandOption.Builder.class)
public final class CommandOption {

	private final String name;

	@Nullable
	private final String paramLabel;

	@Nullable
	private final String description;

	private final String dataType;

	private final String defaultValue;

	private final boolean required;

	// TODO: assume some default?
	private final String inputType;

	private final Map<String, String> choices;

	private CommandOption(String name, String paramLabel, String description, String dataType, String defaultValue,
			boolean required, String inputType, Map<String, String> choices) {
		this.name = Objects.requireNonNull(name);
		this.dataType = Objects.requireNonNullElse(dataType, "string");
		this.required = required;
		this.paramLabel = paramLabel;
		this.description = description;
		this.defaultValue = defaultValue;
		this.inputType = inputType;
		this.choices = Objects.requireNonNullElse(choices, new LinkedHashMap<>());
	}

	public String getName() {
		return name;
	}

	@Nullable
	public String getParamLabel() {
		return paramLabel;
	}

	@Nullable
	public String getDescription() {
		return description;
	}

	public String getDataType() {
		return dataType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public boolean isRequired() {
		return required;
	}

	public String getInputType() {
		return inputType;
	}

	public Map<String, String> getChoices() {
		return choices;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("CommandOption{");
		sb.append("name='").append(name).append('\'');
		sb.append(", paramLabel='").append(paramLabel).append('\'');
		sb.append(", description='").append(description).append('\'');
		sb.append(", dataType='").append(dataType).append('\'');
		sb.append(", defaultValue='").append(defaultValue).append('\'');
		sb.append(", required=").append(required);
		sb.append(", inputType='").append(inputType).append('\'');
		sb.append(", choices=").append(choices);
		sb.append('}');
		return sb.toString();
	}

	@JsonPOJOBuilder
	public static class Builder {

		private String name;

		private String paramLabel;

		private String description;

		private String dataType;

		private String defaultValue;

		private boolean required;

		private String inputType;

		private Map<String, String> choices = new LinkedHashMap<>();

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withParamLabel(String paramLabel) {
			this.paramLabel = paramLabel;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder withDataType(String dataType) {
			this.dataType = dataType;
			return this;
		}

		public Builder withDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
			return this;
		}

		public Builder withRequired(boolean required) {
			this.required = required;
			return this;
		}

		public Builder withInputType(String inputType) {
			this.inputType = inputType;
			return this;
		}

		public Builder withChoices(Map<String, String> choices) {
			this.choices = choices;
			return this;
		}

		public Builder addChoice(String key, String value) {
			this.choices.put(key, value);
			return this;
		}

		public CommandOption build() {
			return new CommandOption(name, paramLabel, description, dataType, defaultValue, required, inputType,
					choices);
		}

	}

}
