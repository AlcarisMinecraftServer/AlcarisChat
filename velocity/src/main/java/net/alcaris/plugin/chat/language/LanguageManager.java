package net.alcaris.plugin.chat.language;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public final class LanguageManager {

    private static final String LOCALE_FILE = "ja_jp.properties";
    private static final Locale DEFAULT_LOCALE = Locale.JAPAN;

    private final Path dataDirectory;
    private final Logger logger;
    private TranslationRegistry registry;

    public LanguageManager(Path dataDirectory, Logger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    public void loadAndRegister() {
        if (registry != null) GlobalTranslator.translator().removeSource(registry);

        Path defaultPath = dataDirectory.resolve("langs/default/" + LOCALE_FILE);
        extractResource("langs/default/" + LOCALE_FILE, defaultPath);

        try {
            Files.createDirectories(dataDirectory.resolve("langs/custom"));
        } catch (IOException e) {
            logger.warn("Failed to create langs/custom: {}", e.getMessage());
        }

        Properties merged = loadProperties(defaultPath);

        Path customPath = dataDirectory.resolve("langs/custom/" + LOCALE_FILE);
        if (Files.exists(customPath)) {
            Properties custom = loadProperties(customPath);
            for (Map.Entry<Object, Object> entry : custom.entrySet()) merged.put(entry.getKey(), entry.getValue());
        }

        registry = TranslationRegistry.create(Key.key("alcaris_chat", "main"));
        registry.defaultLocale(DEFAULT_LOCALE);
        for (Map.Entry<Object, Object> entry : merged.entrySet()) {
            try {
                registry.register((String) entry.getKey(), DEFAULT_LOCALE,
                        new MessageFormat((String) entry.getValue(), DEFAULT_LOCALE));
            } catch (IllegalArgumentException e) {
                logger.warn("Failed to register translation key: {}", entry.getKey());
            }
        }

        GlobalTranslator.translator().addSource(registry);
        logger.info("Loaded {} translation keys", merged.size());
    }

    private void extractResource(String resourcePath, Path targetPath) {
        try {
            Files.createDirectories(targetPath.getParent());
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (is != null) Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
                else logger.warn("Resource not found in JAR: {}", resourcePath);
            }
        } catch (IOException e) {
            logger.error("Failed to extract resource: {}", resourcePath, e);
        }
    }

    private Properties loadProperties(Path path) {
        Properties props = new Properties();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (IOException e) {
            logger.error("Failed to load properties: {}", path, e);
        }
        return props;
    }
}
