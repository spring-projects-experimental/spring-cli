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
package org.springframework.cli.command;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cli.support.SpringCliUserConfig;
import org.springframework.cli.support.SpringCliUserConfig.ProjectRepositories;
import org.springframework.cli.support.SpringCliUserConfig.ProjectRepository;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.util.ObjectUtils;

@ShellComponent
public class ProjectCommands {

	private final SpringCliUserConfig upCliUserConfig;

	@Autowired
	public ProjectCommands(SpringCliUserConfig upCliUserConfig) {
		this.upCliUserConfig = upCliUserConfig;
	}

	@ShellMethod(key = "project add", value = "Add a project to use with 'boot new' and 'boot add' commands")
	public void projectAdd(
		@ShellOption(help = "Project name") String name,
		@ShellOption(help = "Project url") String url,
		@ShellOption(help = "Project description", defaultValue = ShellOption.NULL) String description,
		@ShellOption(help = "Project tags", defaultValue = ShellOption.NULL) List<String> tags
	) {
		List<ProjectRepository> projectRepositories = upCliUserConfig.getProjectRepositories().getProjectRepositories();
		projectRepositories.add(ProjectRepository.of(name, description, url, tags));
		ProjectRepositories projectRepositoriesConfig = new ProjectRepositories();
		projectRepositoriesConfig.setProjectRepositories(projectRepositories);
		upCliUserConfig.setProjectRepositories(projectRepositoriesConfig);
	}

	@ShellMethod(key = "project list", value = "List projects available for use with 'boot new' and 'boot add' commands")
	public Table projectList() {
		Stream<String[]> header = Stream.<String[]>of(new String[] { "Name", "Description", "URL" });
		Collection<ProjectRepository> projectRepositories = upCliUserConfig.getProjectRepositories().getProjectRepositories();
		Stream<String[]> rows = null;
		if (projectRepositories != null) {
			rows = projectRepositories.stream()
				.map(tr -> new String[] { tr.getName(), tr.getDescription(), tr.getUrl() });
		}
		else {
			rows = Stream.empty();
		}
		String[][] data = Stream.concat(header, rows).toArray(String[][]::new);
		TableModel model = new ArrayTableModel(data);
		TableBuilder tableBuilder = new TableBuilder(model);
		return tableBuilder.addFullBorder(BorderStyle.fancy_light).build();
	}

	@ShellMethod(key = "project remove", value = "Remove project")
	public void projectRemove(
		@ShellOption(help = "Project name") String name
	) {
		List<ProjectRepository> projectRepositories = upCliUserConfig.getProjectRepositories().getProjectRepositories();
		projectRepositories = projectRepositories.stream()
			.filter(tc -> !ObjectUtils.nullSafeEquals(tc.getName(), name))
			.collect(Collectors.toList());
		ProjectRepositories projectRepositoriesConfig = new ProjectRepositories();
		projectRepositoriesConfig.setProjectRepositories(projectRepositories);
		upCliUserConfig.setProjectRepositories(projectRepositoriesConfig);
	}
}
