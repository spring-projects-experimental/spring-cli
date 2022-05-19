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

package org.springframework.cli.runtime.engine.frontmatter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.lang.Nullable;

/**
 * The actions to undergo with the template file.
 *
 * @author Mark Pollack
 */
public class Actions {

	// These are the three general purpose actions

	/**
	 * Where to generate the template, relative to the {@code destinationDirectory}. May
	 * contain template placeholders. If this is null, the file is not rendered.
	 */
	@Nullable
	private String generate;

	@Nullable
	private final Inject inject;

	@Nullable
	private Exec exec;


	/**
	 * If set to false, generation of the template is skipped if the {@link #generate
	 * destination file} already exists.
	 */
	private boolean overwrite; // TODO Perhaps this should be nested inside the generate and inject actions as it only pertains to them.

	/**
	 * If set to true, the results of the text template engine is printed to the console.
	 */
	private boolean console;  // TODO consider renaming to 'debug' or something?


	// TODO These actions are 'recipes' and should be extracted into a more formal recipe section
	//      They were implemented before the likes of open-rewrite had this functionality, it is
	//		expected that we will migrate this functionality to be based on open-rewrite

	@Nullable
	private final InjectMavenPlugin injectMavenPlugin;

	@Nullable
	private final InjectMavenDependency injectMavenDependency;

	@Nullable
	private final InjectProperties injectProperties;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	Actions(@JsonProperty("generate") @Nullable String generate, @JsonProperty("overwrite") boolean overwrite,
			@JsonProperty("console") boolean console, @JsonProperty("exec") @Nullable Exec exec,
			@JsonProperty("injectMavenPlugin") @Nullable InjectMavenPlugin injectMavenPlugin,
			@JsonProperty("injectMavenDependency") @Nullable InjectMavenDependency injectMavenDependency,
			@JsonProperty("injectProperties") @Nullable InjectProperties injectProperties,
			@JsonProperty("inject") @Nullable Inject inject) {
		this.generate = generate;
		this.overwrite = overwrite;
		this.console = console;
		this.exec = exec;
		this.injectMavenPlugin = injectMavenPlugin;
		this.injectMavenDependency = injectMavenDependency;
		this.injectProperties = injectProperties;
		this.inject = inject;
	}

	@Nullable
	public String getGenerate() {
		return generate;
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
		return "Actions{" + "to='" + generate + '\'' + ", overwrite=" + overwrite + ", console=" + console + ", exec=" + exec
				+ ", injectMavenPlugin=" + injectMavenPlugin + ", injectMavenDependency=" + injectMavenDependency
				+ ", injectProperties=" + injectProperties + ", inject=" + inject + '}';
	}

}
