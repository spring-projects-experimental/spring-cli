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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.StringUtils;

/**
 * A map of simple strings used to represent types in a {@link CommandOption} object.
 *
 * @author Mark Pollack
 */
public abstract class JavaTypeConverter {

	private static final Map<String, Class> stringClassMap = new HashMap<>();

	static {
		stringClassMap.put("int", Integer.class);
		stringClassMap.put("integer", Integer.class);
		stringClassMap.put("bool", Boolean.class);
		stringClassMap.put("boolean", Boolean.class);
		stringClassMap.put("double", Double.class);
		stringClassMap.put("float", Float.class);
		stringClassMap.put("long", Long.class);
		stringClassMap.put("short", Short.class);
		stringClassMap.put("string", String.class);
	}

	public static Optional<Class> getJavaClass(String typeName) {
		if (StringUtils.hasText(typeName) && stringClassMap.containsKey(typeName.trim().toLowerCase())) {
			return Optional.of(stringClassMap.get(typeName));
		}
		return Optional.empty();
	}

}
