package br.com.lucasmteixeira.playground.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.lucasmteixeira.playground.model.CharacterData;

public class LocalSave {
    private static final Gson GSON = new Gson();
    private static final Path SAVE_FILE = Paths.get(
        System.getProperty("user.home"), ".rpg-game", "characters.json");

    public static List<CharacterData> loadCharacters() {
        if (!Files.exists(SAVE_FILE)) return new ArrayList<>();
        try {
            String json = Files.readString(SAVE_FILE);
            return GSON.fromJson(json, new TypeToken<List<CharacterData>>(){}.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void saveCharacters(List<CharacterData> characters) {
        try {
            Files.createDirectories(SAVE_FILE.getParent());
            Files.writeString(SAVE_FILE, GSON.toJson(characters));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCharacter(CharacterData character) {
        List<CharacterData> list = loadCharacters();
        list.add(character);
        saveCharacters(list);
    }
}
