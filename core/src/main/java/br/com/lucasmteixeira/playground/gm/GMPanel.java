package br.com.lucasmteixeira.playground.gm;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class GMPanel {
    public interface GMActionListener {
        void onCommand(GMCommand command);
    }

    public enum SelectionType { NONE, NPC, OBJECT, EVENT }

    private final Table panel;
    private final Skin skin;
    private final GMActionListener listener;

    private SelectionType selectionType = SelectionType.NONE;
    private List<String> selectedIds = new ArrayList<>();

    public GMPanel(Skin skin, GMActionListener listener) {
        this.skin = skin;
        this.listener = listener;
        this.panel = new Table(skin);
        panel.setBackground("default-pane");
        panel.top().left().pad(8);
    }

    public void setSelection(SelectionType type, List<String> ids) {
        this.selectionType = type;
        this.selectedIds = ids;
        rebuild();
    }

    private void rebuild() {
        panel.clear();
        panel.add(new Label("GM Controls", skin)).padBottom(8).row();

        switch (selectionType) {
            case NPC:
                addNpcControls();
                break;
            case OBJECT:
                addObjectControls();
                break;
            case EVENT:
                addEventControls();
                break;
            default:
                panel.add(new Label("Select an NPC, object or event", skin)).row();
        }
    }

    private void addNpcControls() {
        panel.add(new Label("NPC: " + selectedIds.size() + " selected", skin)).padBottom(6).row();

        addBtn("Incorporate (take control)", GMCommand.Type.NPC_INCORPORATE);
        addBtn("Die", GMCommand.Type.NPC_DIE);
        addBtn("Increase Power", GMCommand.Type.NPC_INCREASE_POWER);
        addBtn("Aggro Players", GMCommand.Type.NPC_AGGRO_PLAYERS);
        addBtn("Surrender", GMCommand.Type.NPC_SURRENDER);
        addBtn("Start Conversation", GMCommand.Type.NPC_START_CONVERSATION);
    }

    private void addObjectControls() {
        panel.add(new Label("Object selected", skin)).padBottom(6).row();
        addBtn("Activate", GMCommand.Type.OBJECT_ACTIVATE);
        addBtn("Move", GMCommand.Type.OBJECT_MOVE);
        addBtn("Give to Player", GMCommand.Type.OBJECT_GIVE_TO_PLAYER);
    }

    private void addEventControls() {
        panel.add(new Label("Event", skin)).padBottom(6).row();
        addBtn("Trigger Event", GMCommand.Type.TRIGGER_EVENT);
        addBtn("Trigger Dialog/Clip", GMCommand.Type.TRIGGER_DIALOG);
        addBtn("Trigger Mini-game", GMCommand.Type.TRIGGER_MINIGAME);
    }

    private void addBtn(String label, GMCommand.Type type) {
        TextButton btn = new TextButton(label, skin);
        btn.addListener(e -> {
            if (btn.isPressed()) {
                for (String id : selectedIds) listener.onCommand(new GMCommand(type, id));
            }
            return false;
        });
        panel.add(btn).fillX().padBottom(4).row();
    }

    public Table getTable() { return panel; }
}
