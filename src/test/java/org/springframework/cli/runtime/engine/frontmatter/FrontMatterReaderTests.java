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

import org.junit.jupiter.api.Test;

import org.springframework.cli.testutil.TestResourceUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mark Pollack
 */
public class FrontMatterReaderTests {

	@Test
	void readTests() {
		String templateYaml = TestResourceUtils.resourceContents(getClass(), "template1.yaml");
		CommandActionFileContents commandActionFileContents = FrontMatterReader.read(templateYaml).get();
		assertThat(commandActionFileContents.getMetadata().getActions().getGenerate()).isEqualTo("hello.yml");
	}

	@Test
	void readMavenDep() {
		String templateYaml = TestResourceUtils.resourceContents(getClass(), "template2.yaml");
		CommandActionFileContents commandActionFileContents = FrontMatterReader.read(templateYaml).get();
		Actions action = commandActionFileContents.getMetadata().getActions();
		assertThat(action.getInjectMavenDependency().getTo()).isEqualTo("pom.xml");
	}

}
