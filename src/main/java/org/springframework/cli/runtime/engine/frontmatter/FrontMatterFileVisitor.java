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
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link FileVisitor} that walks a directory structure looking for files that are to be
 * handled as {@link CommandActionFileContents} (omitting some well known directory and filename
 * patterns).
 *
 * @author Eric Bottard
 */
public class FrontMatterFileVisitor extends PathGatheringFileVisitor {

	private static final Logger logger = LoggerFactory.getLogger(FrontMatterFileVisitor.class);

	private final Tika tika = new Tika();

	@Override
	protected boolean rejectFile(Path file, BasicFileAttributes attrs) {
		return super.rejectFile(file, attrs) || !looksLikeText(file);
	}

	private boolean looksLikeText(Path path) {
		try {
			return (tika.detect(path).startsWith("text") || tika.detect(path).contains("xml"));
		}
		catch (IOException ex) {
			logger.warn("Error detecting mime type for {}", path);
			return false;
		}
	}

}
