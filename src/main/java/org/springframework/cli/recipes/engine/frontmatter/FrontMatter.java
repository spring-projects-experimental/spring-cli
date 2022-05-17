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

package org.springframework.cli.recipes.engine.frontmatter;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.lang.Nullable;

/**
 * @author Mark Pollack
 */
public class FrontMatter {

	// Default only because GraalVM is having trouble with handlebars
	private final String engine;

	/**
	 * The actions to execute against the template. If {@code null}, the file is not
	 * processed.
	 */
	private final Actions actions;

	private final Conditional conditional;

	public Actions getActions() {
		return actions;
	}

	public String getEngine() {
		return engine;
	}

	public Conditional getConditional() {
		return conditional;
	}

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	FrontMatter(@JsonProperty("engine") String engine, @JsonProperty("actions") @Nullable Actions actions,
			@JsonProperty("conditional") Conditional conditional) {
		this.engine = Objects.requireNonNullElse(engine, "mustache");
		this.actions = Objects.requireNonNull(actions);
		this.conditional = Objects.requireNonNullElse(conditional, new Conditional());
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Metadata{");
		sb.append("engine='").append(engine).append('\'');
		sb.append(", actions=").append(actions);
		sb.append(", conditional=").append(conditional);
		sb.append('}');
		return sb.toString();
	}

}
