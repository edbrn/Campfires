# Campfires
A Minecraft server plugin for Minecraft 1.20+ which enables users to place a campfire ontop of a gold block giving them a teleport point accessible within a 3 minutes of dying.

## Features
- Provides an alternative approach to giving users `/home` and `/sethome` to discourage gameplay-altering teleporting such as after mining or when engaged in PVP with other players.
- Allows players to teleport to one of their campfires within 3 minutes after dying giving this player more opportunity to try to repel raiders but also giving raiders less safety.
- Intends for campfires to be destroyable by all players.
- Requires a small grind (9 gold bars) per campfire.
- Each player can have a maximum of 3 campfires.

## Installation
- Download the JAR file from the [releases](https://github.com/edbrn/Campfires/releases/) tab.
- Place the JAR file in your server's `plugins` folder and restart your server.

## Commands
### `/campfires list`
- Lists campfires for the player executing the command.
- Permission required: `campfires.list`

### `/campfires tp <number>`
- Teleports the player to the numbered campfire as shown in the list command.
- Permission required: `campfires.teleport`

## Files managed by Campfires
Only one: `campfires.json` file will be created in the root of the server directory. It contains a JSON object with one key called `campfires` where the child's key is the UUID of the player and the value is a list of objects containing the coordinates keyed `x`, `y` and `z` respectively.

## Developing
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
