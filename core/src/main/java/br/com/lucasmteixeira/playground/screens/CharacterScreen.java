package br.com.lucasmteixeira.playground.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import br.com.lucasmteixeira.playground.GameApp;
import br.com.lucasmteixeira.playground.model.CharacterData;
import br.com.lucasmteixeira.playground.persistence.LocalSave;

public class CharacterScreen implements Screen {
    private final GameApp game;
    private Stage stage;
    private Skin skin;
    private boolean isGameMaster = false;

    public CharacterScreen(GameApp game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.pad(40);
        stage.addActor(root);

        // Title
        Label title = new Label("TABLETOP RPG", skin);
        title.setFontScale(1.8f);
        title.setAlignment(Align.center);
        title.setColor(Color.WHITE);
        root.add(title).expandX().padBottom(8).row();

        Label subtitle = new Label("Choose your character", skin);
        subtitle.setAlignment(Align.center);
        subtitle.setColor(Color.LIGHT_GRAY);
        root.add(subtitle).expandX().padBottom(40).row();

        // Create new character card
        Table createCard = new Table(skin);
        createCard.pad(20);

        Label newLabel = new Label("New Character", skin);
        newLabel.setFontScale(1.1f);
        createCard.add(newLabel).left().colspan(2).padBottom(12).row();

        TextField nameField = new TextField("", skin);
        nameField.setMessageText("Enter name...");
        createCard.add(nameField).expandX().fillX().height(36).padBottom(10).colspan(2).row();

        CheckBox gmCheck = new CheckBox("  Game Master", skin);
        gmCheck.addListener(e -> { isGameMaster = gmCheck.isChecked(); return false; });
        createCard.add(gmCheck).left().padBottom(16).row();

        TextButton createBtn = new TextButton("Create & Play", skin);
        createBtn.addListener(e -> {
            if (createBtn.isPressed()) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    CharacterData character = new CharacterData(name);
                    LocalSave.addCharacter(character);
                    game.setCharacter(character, isGameMaster);
                    game.setScreen(new WorldMapScreen(game));
                }
            }
            return false;
        });
        createCard.add(createBtn).expandX().fillX().height(40).colspan(2).row();

        root.add(createCard).width(380).padBottom(30).row();

        // Saved characters
        List<CharacterData> saved = LocalSave.loadCharacters();
        if (!saved.isEmpty()) {
            Label savedLabel = new Label("Saved Characters", skin);
            savedLabel.setColor(Color.LIGHT_GRAY);
            root.add(savedLabel).left().padBottom(8).row();

            Table charList = new Table();
            ScrollPane scroll = new ScrollPane(charList, skin);
            scroll.setFadeScrollBars(true);

            for (CharacterData c : saved) {
                Table row = new Table(skin);
                row.pad(8);

                Label nameLabel = new Label(c.name, skin);
                nameLabel.setFontScale(1.05f);
                Label levelLabel = new Label("Level " + c.level, skin);
                levelLabel.setColor(Color.LIGHT_GRAY);

                Table info = new Table();
                info.add(nameLabel).left().row();
                info.add(levelLabel).left();

                TextButton selectBtn = new TextButton("Play", skin);
                selectBtn.addListener(e -> {
                    if (selectBtn.isPressed()) {
                        game.setCharacter(c, isGameMaster);
                        game.setScreen(new WorldMapScreen(game));
                    }
                    return false;
                });

                row.add(info).expandX().left();
                row.add(selectBtn).width(80).height(32);
                charList.add(row).expandX().fillX().padBottom(4).row();
            }

            root.add(scroll).width(380).maxHeight(250).row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.06f, 0.06f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}
