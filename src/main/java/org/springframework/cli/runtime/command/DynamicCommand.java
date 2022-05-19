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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cli.SpringCliException;
import org.springframework.cli.runtime.engine.GeneratorResolver;
import org.springframework.cli.runtime.engine.frontmatter.Actions;
import org.springframework.cli.runtime.engine.frontmatter.CommandActionFileContents;
import org.springframework.cli.runtime.engine.frontmatter.FrontMatter;
import org.springframework.cli.runtime.engine.frontmatter.FrontMatterFileVisitor;
import org.springframework.cli.runtime.engine.frontmatter.FrontMatterReader;
import org.springframework.cli.runtime.engine.model.ModelPopulator;
import org.springframework.cli.runtime.engine.templating.HandlebarsTemplateEngine;
import org.springframework.cli.runtime.engine.templating.MustacheTemplateEngine;
import org.springframework.cli.runtime.engine.templating.TemplateEngine;
import org.springframework.shell.command.CommandContext;
import org.springframework.shell.command.CommandParser.CommandParserResult;
import org.springframework.util.StringUtils;

/**
 * Spring Shell command object that is executed for all dynamic commands discovered
 * at runtime.
 *
 * It uses the GeneratorResolver to read files contained in the command/subcommand directory
 * as specified by the arguments commandName and subCommandName
 *
 * Delegates to
 * and interprets files

 */
public class DynamicCommand {

	private static final Logger logger = LoggerFactory.getLogger(DynamicCommand.class);


	private GeneratorResolver generatorResolver;

	private String commandName;

	private String subCommandName;

	private Iterable<ModelPopulator> modelPopulators;

	public DynamicCommand(String commandName, String subCommandName, Iterable<ModelPopulator> modelPopulators) {
		this.commandName = commandName;
		this.subCommandName = subCommandName;
		this.modelPopulators = modelPopulators;
	}

	public void execute(CommandContext commandContext) throws IOException {


		System.out.println("Hello world from dynamic command " + commandName + " " + subCommandName);

		Map<String, Object> model = new HashMap<>();

		addMatchedOptions(model, commandContext);

		addUnmatchedOptions(model, commandContext);

		runCommand(commandContext, model);
	}

	private void addUnmatchedOptions(Map<String, Object> model, CommandContext commandContext) {
		// TODO investigate if spring-shell can support this... do we want it.
	}

	private void addMatchedOptions(Map<String, Object> model, CommandContext commandContext) {
		List<CommandParserResult> commandParserResults = commandContext.getParserResults().results();
		for (CommandParserResult commandParserResult : commandParserResults) {
			// TODO will value() be populated with defaultValue() if not passed in?
			model.put(commandParserResult.option().getLongNames()[0], commandParserResult.value());
		}
	}

	private void runCommand(CommandContext commandContext, Map<String, Object> model) throws IOException {
		Path cwd = Path.of("");
		Path dynamicSubCommandPath = Paths.get(cwd.toString(), ".spring", "commands")
				.resolve(this.commandName).resolve(this.subCommandName).toAbsolutePath();

		// Enrich the model with detected features of the project, e.g. maven artifact name
		if (this.modelPopulators != null) {
			for (ModelPopulator modelPopulator : modelPopulators) {
				modelPopulator.contributeToModel(cwd, model);
			}
		}

		// TODO normalize model keys to camelCase?

		Optional<CommandFileContents> commandFileContents = getCommandFileContents(dynamicSubCommandPath);

		final Map<Path, CommandActionFileContents> commandActionFiles = findCommandActionFiles(dynamicSubCommandPath);
		if (commandActionFiles.size() == 0) {
			throw new SpringCliException("No command action files found to process in directory " + dynamicSubCommandPath.toAbsolutePath());
		}

		processCommandActionFiles(commandActionFiles, cwd, model);


	}

	private void processCommandActionFiles(Map<Path, CommandActionFileContents> commandActionFiles, Path cwd, Map<String, Object> model) throws IOException {

		// Do all the work here ;)

		for (Entry<Path, CommandActionFileContents> kv : commandActionFiles.entrySet()) {
			Path path = kv.getKey();
			CommandActionFileContents commandActionFileContents = kv.getValue();
			Actions actions = commandActionFileContents.getMetadata().getActions();
			if (actions == null) {
				System.out.println("No actions to execute in " + path.toAbsolutePath());
				continue;
			}
			TemplateEngine templateEngine = getTemplateEngine(commandActionFileContents.getMetadata());


			String toFileName = actions.getGenerate();
			if (StringUtils.hasText(toFileName)) {
				generateFile(commandActionFileContents, templateEngine, toFileName, actions.isOverwrite(), model, cwd);
			}

		}


	}

	private void generateFile(CommandActionFileContents commandActionFileContents, TemplateEngine templateEngine, String toFileName, boolean overwrite, Map<String, Object> model, Path cwd) throws IOException {
		Path pathToFile = cwd.resolve(toFileName).toAbsolutePath();
		if ((pathToFile.toFile().exists() && overwrite) || (!pathToFile.toFile().exists())) {
			writeFile(commandActionFileContents, templateEngine, model, pathToFile);
		}
		else {
			System.out.println("Skipping generation of " + pathToFile);
		}
	}

	private void writeFile(CommandActionFileContents commandActionFileContents, TemplateEngine templateEngine,
			Map<String, Object> model, Path pathToFile) throws IOException {
		Files.createDirectories(pathToFile.getParent());
		String result = templateEngine.process(commandActionFileContents.getText(), model);
		Files.write(pathToFile, result.getBytes());
		// TODO: keep log of action taken so can report later.
		System.out.println("Generated "+ pathToFile);
	}

	private TemplateEngine getTemplateEngine(FrontMatter frontMatter) {
		String engine = frontMatter.getEngine();
		TemplateEngine templateEngine;
		if (engine.equalsIgnoreCase("handlebars")) {
			templateEngine = new HandlebarsTemplateEngine();
		}
		else {
			templateEngine = new MustacheTemplateEngine();
		}
		return templateEngine;
	}

	private Optional<CommandFileContents> getCommandFileContents(Path dynamicSubCommandPath) {
		Path commandFilePath = dynamicSubCommandPath.resolve("command.yaml");
		if (!commandFilePath.toFile().exists()) {
			return Optional.empty();
		}
		try {
			return Optional.of(CommandFileReader.read(commandFilePath));
		}
		catch (IOException e) {
			logger.warn("Could not read command.yaml t path {}", commandFilePath, e);
			return Optional.empty();
		}
	}

	private Map<Path, CommandActionFileContents> findCommandActionFiles(Path dynamicSubCommandPath) {
		// Do a first pass to find all files that look parseable...
		final FrontMatterFileVisitor visitor = new FrontMatterFileVisitor();
		try {
			Files.walkFileTree(dynamicSubCommandPath, visitor);
		}
		catch (IOException e) {
			throw new SpringCliException("Error trying to detect actions files", e);
		}

		// Then actually parse, retaining only those paths that yielded a result
		return visitor.getMatches().stream() //
				.map((p) -> new SimpleImmutableEntry<>(p, FrontMatterReader.read(p))) //
				.filter((kv) -> kv.getValue().isPresent()) //
				.collect(toSortedMap(Entry::getKey, (e) -> e.getValue().get()));
	}

	private static <T, K, U> Collector<T, ?, Map<K, U>> toSortedMap(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper) {
		return Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> {
			throw new IllegalStateException(String.format("Duplicate key for values %s and %s", v1, v2));
		}, TreeMap::new);
	}

}
