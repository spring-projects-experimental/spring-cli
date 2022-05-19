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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A {@link FileVisitor} that walks a directory structure, omitting some well known
 * directory and filename patterns and collects {@link Path}s of regular files.
 *
 * <p>
 * Subclasses may override {@link #rejectFile(Path, BasicFileAttributes)} to further
 * restrict the set of files matched.
 * </p>
 *
 * @author Eric Bottard
 */
public class PathGatheringFileVisitor extends SimpleFileVisitor<Path> {

	protected final List<Path> matches = new ArrayList<>();

	private final Set<Pattern> forbiddenDirectoryPatterns;

	private final Set<Pattern> forbiddenFilenamePatterns;

	public PathGatheringFileVisitor() {
		this(Collections.singleton("\\Q.git\\E"), Collections.singleton(".*~"));
	}

	public PathGatheringFileVisitor(Set<String> forbiddenDirectoryPatterns, Set<String> forbiddenFilenamePatterns) {
		this.forbiddenDirectoryPatterns = forbiddenDirectoryPatterns.stream().map(Pattern::compile)
				.collect(Collectors.toSet());
		this.forbiddenFilenamePatterns = forbiddenFilenamePatterns.stream().map(Pattern::compile)
				.collect(Collectors.toSet());
	}

	public List<Path> getMatches() {
		return matches;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
		String dirName = dir.getFileName().toString();
		return forbiddenDirectoryPatterns.stream().noneMatch((p) -> p.matcher(dirName).matches())
				? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if (!rejectFile(file, attrs)) {
			matches.add(file);
		}
		return super.visitFile(file, attrs);
	}

	protected boolean rejectFile(Path file, BasicFileAttributes attrs) {
		String filename = file.getFileName().toString();
		return forbiddenFilenamePatterns.stream().anyMatch((p) -> p.matcher(filename).matches());
	}

}
