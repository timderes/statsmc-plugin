# StatsMC - Minecraft Statistics Plugin

**This plugin creates a API and web interface that returns player statistics for your Minecraft server.**

> Plugin is currently work-in-progress. Some features may not work as expected. Please report any issues you encounter. **Web interface is planned as a future update!**

## How to use

1. Download the `StatsMC1-x.jar` file from the releases page.
2. Place the file in your Minecraft server's `plugins` directory.
3. Start your server. The plugin will automatically create a configuration file.
4. You can view the statistics by accessing the API endpoint. The default URL is `http://your-server-ip:11111/mcstats/`.

## Configuration

The plugin creates a configuration file named `config.yml` in the `plugins/StatsMC` directory. You can modify the following settings:

| Setting | Description                         | Default Value |
| ------- | ----------------------------------- | ------------- |
| `port`  | The port on which the API will run. | `11111`       |

## Supported Minecraft Versions

Currently the plugin only officially supports Minecraft 1.21.x. However, it may work with other versions as well. If you encounter any issues, please open an issue on the GitHub repository.

| StatsMC Version | Supported Minecraft Versions |
| --------------- | ---------------------------- |
| 1.0             | 1.21.x                       |

## API Endpoints

| Endpoint | Description                                                                                          | Arguments | Added in Version |
| -------- | ---------------------------------------------------------------------------------------------------- | --------- | ---------------- |
| `/api`   | Base endpoint for the API. Returns basic info like the API version, server status, and player count. | `None`    | 1.0              |

## Contributing

**This is the first time I am creating a plugin for Minecraft. So, if you see any mistakes in the code or have any suggestions for improvements, please feel free to contribute! :)**

## Building from Source

Anything in `src/main/java` is the backend plugin code, and anything in `src/web` is the frontend web interface code.

### Prerequisites

- [Java Development Kit](https://www.oracle.com/java/technologies/downloads/)
- [Apache Maven](https://maven.apache.org/)
- (Optional) [Crafty Controller](https://craftycontrol.com/) (For testing the plugin on a local server)

To build the plugin from source

```bash
# Build both parts and bundle it into a JAR file
npm run build

# Or you can just build the JAR file (With the current frontend build)
npm run build:jar

# Or just build the frontend
npm run build:web
```

## Active the plugin on your server

1. Copy the generated `StatsMC1-x.jar` file from the `target` directory to your server's `plugins` folder.
2. Start or restart your Minecraft server.

## Disclaimer

This plugin is not affiliated with Mojang Studios or Microsoft. It is an independent project created by the community. The plugin is provided "as-is" without any warranties or guarantees. Use it at your own risk.

**Created following the [Minecraft Usage Guidelines](https://www.minecraft.net/en-us/terms#usage-guidelines).**

## Credits

This project started as a fork of [StatsMC-plugin](https://github.com/afmigoo/StatsMC-plugin), which was created by [afmigoo](https://github.com/afmigoo/).

## License

This project is licensed under the MIT License.
