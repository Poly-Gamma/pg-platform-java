# pg-platform-java

## About

Module providing operating system, processor architecture, and data model (ABI) definitions. Along
with defining common platform details, the platform JVM is executing on is also provided.

## License

Apache License, Version 2.0

## Building

To build the JAR files, invoke the `jar` task:

```
./gradlew jar
```

To publish to local maven repository, invoke the `publishToMavenLocal` task:

```
./gradlew publishToMavenLocal
```

## Dependency

### Java 9+

Including in Java 9 or above, where JPMS is being used, `module-info.java` must require
`org.polygamma.platform`:

```java
module myproject {
    requires org.polygamma.platform;
}
```

### Maven

```xml
<dependency>
    <groupId>org.poly-gamma</groupId>
    <artifactId>pg-platform</groupId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
dependencies {
    api('org.poly-gamma:pg-platform:1.0.0')
}
```

## Example

```java
import org.polygamma.platform.OperatingSystem;
import org.polygamma.platform.OperatingSystemVersion;
import org.polygamma.platform.Architecture;
import org.polygamma.platform.DataModel;
import org.polygamma.platform.Platform;

public class Main {
    public static void main(String[] args) {
    	// Retrieve platform JVM is executing on:
        Platform current = Platform.current();
        // Determine operating system JVM is executing on:
	String os = switch (current.os()) {
	    case LINUX -> "Linux";
	    case NETBSD -> "NetBSD";
	    case OPENBSD -> "OpenBSD";
	    case FREEBSD -> "FreeBSD";
	    case MACOS -> "macOS";
	    case WINDOWS -> "Windows";
	    default -> "unknown";
	};
	// Determine operating system version JVM is executing on:
	String osv = String.format("%d.%d.%d", current.osVersion().major(),
	                           current.osVersion().minor(), current.osVersion().patch());
	// Determine processor architecture JVM is executing on:
	String arch = switch (current.architecture()) {
	    case X86 -> "x86";
	    case X86_64 -> "x86-64";
	    case ARM -> "arm";
	    case ARM64 -> "arm64";
	    case RISCV64 -> "risc-v64";
	    case PPC64 -> "powerpc64";
	    case S390X -> "s390x";
	    default -> "unknown";
	};
	// Determine size of C language types for platform JVM is executing on:
	int sizeOfInt = current.dataModel().intModel().bytes();
	int sizeOfLong = current.dataModel().longModel().bytes();
	int sizeOfLongLong = current.dataModel().llongModel().bytes();
	int sizeOfPointer = current.dataModel().pointerModel().bytes();
    }
}
```
