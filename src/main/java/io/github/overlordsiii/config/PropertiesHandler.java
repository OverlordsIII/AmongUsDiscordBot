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

	private final Map<String, String> configValues;

	private PropertiesHandler(String filename, Map<String, String> configValues) {
		this.propertiesPath = Paths.get("src", "main", "resources").resolve(filename);
		this.configValues = configValues;
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

	public static Builder builder() {
		return new Builder();
	}

	public <T> T getConfigOption(String key, Function<String, T> parser) {
		return parser.apply(configValues.get(key));
	}

	public static class Builder {

		private final Map<String, String> configValues = new HashMap<>();
		private String filename;

		private Builder() {}

		public Builder addConfigOption(String key, String defaultValue) {
			configValues.put(key, defaultValue);
			return this;
		}

		public Builder setFileName(String fileName) {
			this.filename = fileName;
			return this;
		}

		public PropertiesHandler build() {
			PropertiesHandler propertiesHandler = new PropertiesHandler(filename, configValues);
			propertiesHandler.initialize();
			return propertiesHandler;
		}
	}

}
