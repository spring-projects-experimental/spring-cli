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

package org.springframework.cli.runtime.engine.frontmatter;

/**
 * A file that describes the general type of actions that a command can perform.
 * The file contains a front matter section demarcated by three dashes and below that
 * a text section that contains template text used by the action.
 *
 * Template text contains literal text, which will be copied directly to the result text
 * and expressions, that will be evaluated to obtain a value to place in the result text.
 *
 *
 * <p>
 * For example <pre>
 * ---
 * actions:
 *   to: hello.txt
 * ---
 * Hello World @ {{now}}.
 * </pre>
 *
 * </p>
 *
 * @author Mark Pollack
 */
public class CommandActionFileContents {

	private final FrontMatter frontMatter;

	private final String text;

	/* package */ CommandActionFileContents(FrontMatter frontMatter, String text) {
		this.frontMatter = frontMatter;
		this.text = text;
	}

	public FrontMatter getMetadata() {
		return frontMatter;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Template{");
		sb.append("frontMatter=").append(frontMatter);
		sb.append(", text='").append(text).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
