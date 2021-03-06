/*
 * Copyright 2022 the original author or authors.
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
package org.springframework.cli.config;

import java.util.Base64;

import com.sun.jna.CallbackReference;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHLicense;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHObject;
import org.kohsuke.github.GHPerson;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GHVerification;
import org.kohsuke.github.GitUser;

import org.springframework.cli.initializr.model.ArtifactId;
import org.springframework.cli.initializr.model.BootVersion;
import org.springframework.cli.initializr.model.Dependencies;
import org.springframework.cli.initializr.model.Dependency;
import org.springframework.cli.initializr.model.DependencyCategory;
import org.springframework.cli.initializr.model.Description;
import org.springframework.cli.initializr.model.GroupId;
import org.springframework.cli.initializr.model.IdName;
import org.springframework.cli.initializr.model.JavaVersion;
import org.springframework.cli.initializr.model.Language;
import org.springframework.cli.initializr.model.Metadata;
import org.springframework.cli.initializr.model.Name;
import org.springframework.cli.initializr.model.PackageName;
import org.springframework.cli.initializr.model.Packaging;
import org.springframework.cli.initializr.model.ProjectType;
import org.springframework.cli.initializr.model.Version;
import org.springframework.cli.initializr.model.JavaVersion.JavaVersionValues;
import org.springframework.cli.initializr.model.Language.LanguageValues;
import org.springframework.cli.initializr.model.Packaging.PackagingValues;
import org.springframework.cli.initializr.model.ProjectType.ProjectTypeValue;
import org.springframework.cli.runtime.command.Command;
import org.springframework.cli.runtime.command.CommandFileContents;
import org.springframework.cli.runtime.command.CommandOption;
import org.springframework.cli.runtime.command.DynamicCommand;
import org.springframework.cli.runtime.engine.frontmatter.Action;
import org.springframework.cli.runtime.engine.frontmatter.Conditional;
import org.springframework.cli.runtime.engine.frontmatter.Exec;
import org.springframework.cli.runtime.engine.frontmatter.FrontMatter;
import org.springframework.cli.runtime.engine.frontmatter.Inject;
import org.springframework.cli.runtime.engine.frontmatter.InjectMavenDependency;
import org.springframework.cli.runtime.engine.frontmatter.InjectMavenPlugin;
import org.springframework.cli.runtime.engine.frontmatter.InjectProperties;
import org.springframework.cli.support.SpringCliUserConfig.CommandDefault;
import org.springframework.cli.support.SpringCliUserConfig.CommandDefaults;
import org.springframework.cli.support.SpringCliUserConfig.Host;
import org.springframework.cli.support.SpringCliUserConfig.Hosts;
import org.springframework.cli.support.SpringCliUserConfig.Initializr;
import org.springframework.cli.support.SpringCliUserConfig.Initializrs;
import org.springframework.cli.support.SpringCliUserConfig.Option;
import org.springframework.nativex.hint.FieldHint;
import org.springframework.nativex.hint.JdkProxyHint;
import org.springframework.nativex.hint.MethodHint;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.nativex.type.NativeConfiguration;

/**
 * {@code spring-native} configuration.
 *
 * We keep a lot of stuff here for now. When {@code spring-native} is more
 * mature and production ready {@code spring-shell} will have its own
 * native config.
 *
 * @author Janne Valkealahti
 */
@NativeHint(
	options = {
		// debug info flag
		// "-g",
		// https://github.com/rd-1-2022/spring-up/issues/14
		"-H:+AllowJRTFileSystem",
		// Attempt to make win not to fail fast
		"-H:-DeadlockWatchdogExitOnTimeout",
		"-H:DeadlockWatchdogInterval=0"
	},
	resources = {
		@ResourceHint(
			patterns = {
				"completion/.*",
				"template/.*.st",
				"org/springframework/shell/component/.*.stg",
				"com/sun/jna/win32-x86-64/jnidispatch.dll",
				"org/apache/tika/mime/tika-mimetypes.xml"
			}
		),
	},
	types = {
		@TypeHint(
			types = {
				ArtifactId.class, BootVersion.class, Dependencies.class, Dependency.class, DependencyCategory.class,
				Description.class, GroupId.class, IdName.class, JavaVersion.class, JavaVersionValues.class, Language.class,
				LanguageValues.class, Metadata.class, Name.class, PackageName.class, Packaging.class, PackagingValues.class,
				ProjectType.class, ProjectTypeValue.class, Version.class, CallbackReference.class, Native.class,
				NativeLong.class, PointerByReference.class, IntByReference.class, Base64.Decoder.class
			},
			typeNames = { "com.sun.jna.Klass" },
			access = {
				TypeAccess.PUBLIC_CLASSES, TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.PUBLIC_FIELDS,
				TypeAccess.PUBLIC_METHODS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			types = Structure.class,
			fields = {
				@FieldHint( name = "memory", allowWrite = true),
				@FieldHint( name = "typeInfo")
			},
			methods = {
				@MethodHint( name = "newInstance", parameterTypes = { Class.class, Pointer.class }),
				@MethodHint( name = "newInstance", parameterTypes = { Class.class, long.class }),
				@MethodHint( name = "newInstance", parameterTypes = { Class.class })
			},
			access = {
				TypeAccess.PUBLIC_CLASSES, TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.PUBLIC_FIELDS,
				TypeAccess.PUBLIC_METHODS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "com.sun.jna.Structure$FFIType",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "com.sun.jna.Structure$FFIType$size_t",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$CHAR_INFO",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$CONSOLE_CURSOR_INFO",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$CONSOLE_SCREEN_BUFFER_INFO",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$COORD",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$INPUT_RECORD",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$INPUT_RECORD$EventUnion",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$KEY_EVENT_RECORD",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$MOUSE_EVENT_RECORD",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$WINDOW_BUFFER_SIZE_RECORD",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$MENU_EVENT_RECORD",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$FOCUS_EVENT_RECORD",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$SMALL_RECT",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.jline.terminal.impl.jna.win.Kernel32$UnionChar",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			typeNames = "org.kohsuke.github.GitHubInteractiveObject",
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			types = {
				GHMyself.class, GHObject.class, GHPerson.class, GHUser.class, GHCommit.class, GHLicense.class,
				GHRepository.class, GHVerification.class, GitUser.class
			},
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			types = {
				CommandDefaults.class, CommandDefault.class, Option.class, CommandFileContents.class, Command.class,
				CommandOption.class, CommandOption.Builder.class, DynamicCommand.class, Hosts.class, Host.class,
				Initializrs.class, Initializr.class
			},
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		),
		@TypeHint(
			types = {
				Action.class, Conditional.class, Exec.class, FrontMatter.class, Inject.class, InjectMavenDependency.class,
				InjectMavenPlugin.class, InjectProperties.class
			},
			access = {
				TypeAccess.PUBLIC_CONSTRUCTORS, TypeAccess.DECLARED_CLASSES, TypeAccess.DECLARED_CONSTRUCTORS,
				TypeAccess.DECLARED_FIELDS, TypeAccess.DECLARED_METHODS
			}
		)
	},
	jdkProxies = {
		@JdkProxyHint( typeNames = { "com.sun.jna.Library" }),
		@JdkProxyHint( typeNames = { "com.sun.jna.Callback" }),
		@JdkProxyHint( typeNames = { "org.jline.terminal.impl.jna.win.Kernel32" }),
		@JdkProxyHint( typeNames = { "org.jline.terminal.impl.jna.linux.CLibrary" })
	}
)
public class SpringCliNativeConfiguration implements NativeConfiguration {
}
