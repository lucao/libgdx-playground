package br.com.lucasmteixeira.playground.model;

import java.util.ArrayList;
import java.util.List;

public class CharacterData {
    public String id;
    public String name;
    public int level = 1;
    public List<String> completedAdventures = new ArrayList<>();
    public List<String> outcomeFlags = new ArrayList<>();

    public CharacterData() {}

    public CharacterData(String name) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
    }
}
