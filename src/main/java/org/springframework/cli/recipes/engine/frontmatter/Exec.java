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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.lang.Nullable;

/**
 * An action to run an external process, optionally sending its output to a file and/or
 * using the body of the template as its stdin.
 */
public class Exec {

	/**
	 * The args of the process to run. The first elements of this list is the executable
	 * to run and is hence required.
	 */
	private final List<String> args;

	/**
	 * If set, the relative path to which to redirect stdout of the running process.
	 */
	@Nullable
	private final String to;

	/**
	 * If set, the relative path to which to redirect stderr of the running process.
	 */
	@Nullable
	private final String errto;

	/**
	 * If set to {@code true}, use the rendered body of the template as stdin of the
	 * running process.
	 */
	private final boolean in;

	/**
	 * If set, the path (relative to the current engine working directory) to use as the
	 * {@link ProcessBuilder#directory(File) working directory} of the running process.
	 */
	private final String dir;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	Exec(@JsonProperty("args") List<String> args, @JsonProperty("to") String to, @JsonProperty("errto") String errto,
			@JsonProperty("in") boolean in, @JsonProperty("dir") String dir) {
		this.args = Objects.requireNonNullElse(args, new ArrayList<>());
		this.to = to;
		this.errto = errto;
		this.in = in;
		this.dir = Objects.requireNonNullElse(dir, "");
	}

	public List<String> getArgs() {
		return args;
	}

	@Nullable
	public String getTo() {
		return to;
	}

	@Nullable
	public String getErrto() {
		return errto;
	}

	public boolean isIn() {
		return in;
	}

	public String getDir() {
		return dir;
	}

	@Override
	public String toString() {
		return "Exec{" + "args=" + args + '}';
	}

}
