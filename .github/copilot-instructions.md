# Copilot Instructions

## Repository Overview

This repository contains the source code for a Minecraft plugin that tracks various player statistics and provides an API to access this data. The plugin is designed to be used with Minecraft servers running.

Inside the source code folder, there is a resources folder containg HTML, CSS and JavaScript files for a web interface that allows users to view and interact with the tracked statistics.

The plugin is written in Java and uses the Bukkit API to interact with the Minecraft server.

## API Endpoints

These are current API endpoints for the StatsMC plugin:

`/api` - Base endpoint for the API. Returning basic information about the API eg. Version, server status, worlds, players online, ...

### Planned Endpoints

`/api/getPlayers` - Returns a list of all players on the server along with their UUIDs.
`/api/getPlayer` - Returns detailed information about a specific player, including their statistics.

`/api/getBlocks` - Returns information about the available blocks in the game.
`/api/getEntities` - Returns information about the entities present in the game.
`/api/getAdvancements` - Returns information about the advancements available in the game.

`/api/leaderboard/statistic?=statistic_name` - Returns a leaderboard for a specific statistic across all players.

## Code Quality

The codebase follows standard Java coding conventions and best practices. It is organized into packages based on functionality, making it easy to navigate and understand. The code is well-documented with comments explaining the purpose of classes and methods.

### Frontend

The Frontend is currently a work in progress, and will be written in HTML, CSS, and JavaScript. It will provide a user-friendly interface for viewing player statistics and interacting with the API. The Frontend wont support Internet Explorer. Only the latest versions of modern browsers like Chrome, Firefox, Edge, and Safari will be supported.

### Mobile Support

The web interface will be designed to be responsive and mobile-friendly, ensuring a seamless experience across different devices and screen sizes.
