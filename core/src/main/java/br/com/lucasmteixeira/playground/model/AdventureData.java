package br.com.lucasmteixeira.playground.model;

import java.util.List;

public class AdventureData {
    public String id;
    public String name;
    public String description;
    public int act;
    public List<String> prerequisites;
    public List<String> outcomes;
    public String sceneType; // "ISO" or "2D"
    public float mapX;
    public float mapY;
}
