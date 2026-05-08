package br.com.lucasmteixeira.playground.gm;

public class GMCommand {
    public enum Type {
        // NPC commands
        NPC_DIE, NPC_INCREASE_POWER, NPC_AGGRO_PLAYERS, NPC_AGGRO_FACTION,
        NPC_SURRENDER, NPC_START_CONVERSATION, NPC_INTERACT_OBJECT,
        NPC_INCORPORATE, // GM takes direct control of NPC
        NPC_RELEASE,     // GM releases NPC control

        // Event commands
        TRIGGER_EVENT, TRIGGER_MINIGAME, TRIGGER_DIALOG, TRIGGER_CLIP,

        // Object commands
        OBJECT_ACTIVATE, OBJECT_MOVE, OBJECT_GIVE_TO_PLAYER,

        // Scene commands
        CHANGE_SCENE_TYPE // ISO <-> 2D
    }

    public Type type;
    public String targetId;   // NPC id, object id, or event id
    public String factionId;  // for aggro faction commands
    public String payload;    // extra JSON data if needed

    public GMCommand() {}

    public GMCommand(Type type, String targetId) {
        this.type = type;
        this.targetId = targetId;
    }
}
