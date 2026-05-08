package br.com.lucasmteixeira.playground.adventure;

import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.lucasmteixeira.playground.model.AdventureData;
import br.com.lucasmteixeira.playground.model.CharacterData;

public class AdventureRegistry {
    private static final Gson GSON = new Gson();
    private final List<AdventureData> adventures;

    public AdventureRegistry() {
        try (InputStreamReader reader = new InputStreamReader(
                AdventureRegistry.class.getClassLoader().getResourceAsStream("adventures.json"))) {
            adventures = GSON.fromJson(reader, new TypeToken<List<AdventureData>>(){}.getType());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load adventures.json", e);
        }
    }

    public List<AdventureData> getAvailableFor(CharacterData character, int act) {
        return adventures.stream()
            .filter(a -> a.act == act)
            .filter(a -> character.outcomeFlags.containsAll(a.prerequisites))
            .collect(Collectors.toList());
    }

    public List<Integer> getAvailableActs() {
        return adventures.stream().map(a -> a.act).distinct().sorted().collect(Collectors.toList());
    }

    public AdventureData findById(String id) {
        return adventures.stream().filter(a -> a.id.equals(id)).findFirst().orElse(null);
    }
}
