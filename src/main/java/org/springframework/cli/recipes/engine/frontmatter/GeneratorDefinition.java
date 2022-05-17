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

/**
 * Represents a text based generator that contains a frontmatter section and below that, a
 * text section.
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
public class GeneratorDefinition {

	private final FrontMatter frontMatter;

	private final String text;

	/* package */ GeneratorDefinition(FrontMatter frontMatter, String text) {
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
