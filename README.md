# Campfires
A Minecraft server plugin for Minecraft 1.20+ which enables users to place a campfire ontop of a gold block giving them a teleport point accessible within a short period of time after dying.

## Features
- Provides an alternative approach to giving users `/home` and `/sethome` to discourage gameplay-altering teleporting such as after mining or when engaging in PVP with other players.
- Allows players to teleport to one of their campfires within 3 minutes after spawning to reduce frustration after dying
- Intends for campfires to be destroyable by all players
- Requires a small grind (9 gold bars) per campfire

## Developing
### Foreword
I am a software engineer but with no professional experience in Java so the quality / style of this repo may not match expectations.
That said, I have implemented unit tests and tried to apply best practices but I am aware of issues such as how I am handling files (e.g. opening file handles on every function call) but this is very much a "get something working" project at this stage.

### Automated tests
#### Unit tests
Run `mvn test` to run the unit tests.

#### Formatting checks
Formatting of files is checked as part of the CI build using `mvn fmt:check`. You can automatically format files using: `mvn fmt:format`.

#### Linting
There are no automated linting rules at the moment. Code cleanup like removing unused imports relies on the developer.

### Building a development server
You need to make a server JAR to run your server.

- Make a temporary directory for the build and a directory for your development server:
  - `mkdir spigot-build`
  - `mkdir mc-server-dev`
  - `cd spigot-build`
- Obtain `BuildTools.jar` from the Spigot website (https://www.spigotmc.org/wiki/buildtools/) and place it in this folder
- Run `java -jar BuildTools.jar`
- A `spigot-{version}.jar` file will be made. Copy this into your development server folder `cp spigot-{version}.jar ../mc-server-dev`
- Run your server: `cd mc-server-dev` then `java -jar spigot-{version}.jar`.
- After accepting the EULA and running a second time, a `plugins` folder now exists.

### Installing the plugin during development
- Write code as needed
- Run `cd /path/to/Campfires/ && mvn package && cd /path/to/mc-server-dev/ && cp /path/to/Campfires/target/Campfires-{version}.jar plugins/ && java -jar {serverjar}.jar nogui` - for the version to expect, see `pom.xml`
