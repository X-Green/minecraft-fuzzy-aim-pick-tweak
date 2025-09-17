package dev.eas.config;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.eas.FuzzyAimPickTweak;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import dev.eas.Reference;

public class Configs implements IConfigHandler {
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    private static final String FEATURE_CONFIG_KEY = Reference.MOD_ID + ".config.toggles";

    public static class FeatureConfigs {
        public static final ConfigBoolean ENABLE_FUZZY_AIM = new ConfigBoolean("enableFuzzyAim", false).apply(FEATURE_CONFIG_KEY);
        public static final ConfigFloat FUZZY_AIM_RADIUS = new ConfigFloat("fuzzyAimRadius", 3.0f, 0.1f, 90).apply(FEATURE_CONFIG_KEY);
        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(ENABLE_FUZZY_AIM, FUZZY_AIM_RADIUS);
    }

    public static void loadFromFile() {
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(CONFIG_FILE_NAME);
        if (Files.exists(configFile) && Files.isReadable(configFile)) {
            JsonElement element = JsonUtils.parseJsonFileAsPath(configFile);
            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "Toggles", FeatureConfigs.OPTIONS);
            }
        } else {
            FuzzyAimPickTweak.LOGGER.error("loadFromFile(): Failed to load config file '{}'.", configFile.toAbsolutePath());
        }

    }

    public static void saveToFile() {
        Path dir = FileUtils.getConfigDirectoryAsPath();
        if (!Files.exists(dir)) {
            FileUtils.createDirectoriesIfMissing(dir);
        }
        if (Files.isDirectory(dir)) {
            JsonObject root = new JsonObject();
            ConfigUtils.writeConfigBase(root, "Toggles", FeatureConfigs.OPTIONS);
            JsonUtils.writeJsonToFileAsPath(root, dir.resolve(CONFIG_FILE_NAME));
        } else {
            FuzzyAimPickTweak.LOGGER.error("saveToFile(): Config Folder '{}' does not exist!", dir.toAbsolutePath());
        }
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }

    private static void getStrings(JsonObject obj, Set<String> outputSet, String arrayName) {
        outputSet.clear();
        if (JsonUtils.hasArray(obj, arrayName)) {
            JsonArray arr = obj.getAsJsonArray(arrayName);
            final int size = arr.size();
            for (int i = 0; i < size; i++) {
                outputSet.add(arr.get(i).getAsString());
            }
        }
    }

    private static void writeStrings(JsonObject obj, Set<String> inputSet, String arrayName) {
        if (!inputSet.isEmpty()) {
            JsonArray arr = new JsonArray();
            for (String str : inputSet) {
                arr.add(str);
            }
            obj.add(arrayName, arr);
        }
    }
}