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

package org.springframework.cli.recipes.engine.frontmatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.lang.Nullable;

/**
 * The actions to undergo with the template file.
 *
 * @author Mark Pollack
 */
public class Actions {

	/**
	 * Where to generate the template, relative to the {@code destinationDirectory}. May
	 * contain template placeholders. If this is null, the file is not rendered.
	 */
	@Nullable
	private String to;

	/**
	 * If set to false, generation of the template is skipped if the {@link #to
	 * destination file} already exists.
	 */
	private boolean overwrite;

	/**
	 * If set to true, the contents of the template generation is printed to the console.
	 */
	private boolean console;

	@Nullable
	private Exec exec;

	@Nullable
	private final InjectMavenPlugin injectMavenPlugin;

	@Nullable
	private final InjectMavenDependency injectMavenDependency;

	@Nullable
	private final InjectProperties injectProperties;

	@Nullable
	private final Inject inject;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	Actions(@JsonProperty("to") @Nullable String to, @JsonProperty("overwrite") boolean overwrite,
			@JsonProperty("console") boolean console, @JsonProperty("exec") @Nullable Exec exec,
			@JsonProperty("injectMavenPlugin") @Nullable InjectMavenPlugin injectMavenPlugin,
			@JsonProperty("injectMavenDependency") @Nullable InjectMavenDependency injectMavenDependency,
			@JsonProperty("injectProperties") @Nullable InjectProperties injectProperties,
			@JsonProperty("inject") @Nullable Inject inject) {
		this.to = to;
		this.overwrite = overwrite;
		this.console = console;
		this.exec = exec;
		this.injectMavenPlugin = injectMavenPlugin;
		this.injectMavenDependency = injectMavenDependency;
		this.injectProperties = injectProperties;
		this.inject = inject;
	}

	@Nullable
	public String getTo() {
		return to;
	}

	public boolean getConsole() {
		return console;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	@Nullable
	public Exec getExec() {
		return exec;
	}

	@Nullable
	public InjectMavenPlugin getInjectMavenPlugin() {
		return injectMavenPlugin;
	}

	@Nullable
	public InjectMavenDependency getInjectMavenDependency() {
		return injectMavenDependency;
	}

	@Nullable
	public InjectProperties getInjectProperties() {
		return injectProperties;
	}

	@Nullable
	public Inject getInject() {
		return inject;
	}

	@Override
	public String toString() {
		return "Actions{" + "to='" + to + '\'' + ", overwrite=" + overwrite + ", console=" + console + ", exec=" + exec
				+ ", injectMavenPlugin=" + injectMavenPlugin + ", injectMavenDependency=" + injectMavenDependency
				+ ", injectProperties=" + injectProperties + ", inject=" + inject + '}';
	}

}
