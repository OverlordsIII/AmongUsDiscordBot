package io.github.overlordsiii.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class PropertiesHandler {

	private final Path propertiesPath;

	private final Map<String, String> configValues = new HashMap<>();

	public PropertiesHandler(String filename) {
		this.propertiesPath = Paths.get("src", "main", "resources").resolve(filename);
	}

	public PropertiesHandler initialize() {
		try {
			load();
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	public void load() throws IOException {

		if (!Files.exists(propertiesPath)) {
			return;
		}

		Properties properties = new Properties();

		properties.load(Files.newInputStream(propertiesPath));

		properties.forEach((o, o2) -> configValues.put(o.toString(), o2.toString()));

	}

	public void save() throws IOException {

		if (!Files.exists(propertiesPath.getParent())) {
			throw new RuntimeException("Could not find resources dir!");
		}

		Properties properties = new Properties();

		configValues.forEach(properties::put);

		properties.store(Files.newOutputStream(propertiesPath), "This stores the configuration properties for the amongus bot");

	}

	public PropertiesHandler addConfigOption(String key, String defaultValue) {
		configValues.put(key, defaultValue);
		return this;
	}

	public <T> T getConfigOption(String key, Function<String, T> parser) {
		return parser.apply(configValues.get(key));
	}

}
