package br.com.lucasmteixeira.playground.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import br.com.lucasmteixeira.playground.GameApp;
import br.com.lucasmteixeira.playground.adventure.AdventureRegistry;
import br.com.lucasmteixeira.playground.model.AdventureData;

public class WorldMapScreen implements Screen {
    private final GameApp game;
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture mapTexture;
    private AdventureRegistry registry;
    private int selectedAct = 1;

    public WorldMapScreen(GameApp game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        batch = new SpriteBatch();
        registry = new AdventureRegistry();
        Gdx.input.setInputProcessor(stage);

        // Procedural map background
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(0.12f, 0.18f, 0.12f, 1f);
        pm.fill();
        pm.setColor(0.08f, 0.14f, 0.08f, 1f);
        pm.fillRectangle(0, 0, w / 3, h / 2);
        pm.setColor(0.16f, 0.24f, 0.16f, 1f);
        pm.fillCircle(w * 2 / 3, h / 3, 140);
        pm.setColor(0.1f, 0.16f, 0.1f, 1f);
        pm.fillCircle(w / 4, h * 2 / 3, 100);
        mapTexture = new Texture(pm);
        pm.dispose();

        buildUI();
    }

    private void buildUI() {
        stage.clear();

        List<Integer> acts = registry.getAvailableActs();
        List<AdventureData> adventures = registry.getAvailableFor(game.getCharacter(), selectedAct);

        // Top bar
        Table topBar = new Table(skin);
        topBar.setFillParent(true);
        topBar.top().pad(12);

        Label charLabel = new Label(game.getCharacter().name + "  •  Lv." + game.getCharacter().level, skin);
        charLabel.setColor(Color.LIGHT_GRAY);

        Label titleLabel = new Label("World Map", skin);
        titleLabel.setFontScale(1.3f);
        titleLabel.setAlignment(Align.center);

        topBar.add(charLabel).left().expandX();
        topBar.add(titleLabel).center().expandX();

        // Act navigation
        int currentIndex = acts.indexOf(selectedAct);
        Table actNav = new Table();
        TextButton prevBtn = new TextButton("<", skin);
        TextButton nextBtn = new TextButton(">", skin);
        Label actLabel = new Label("Act " + selectedAct, skin);
        actLabel.setAlignment(Align.center);

        prevBtn.setDisabled(currentIndex <= 0);
        nextBtn.setDisabled(currentIndex >= acts.size() - 1);

        prevBtn.addListener(e -> {
            if (prevBtn.isPressed() && !prevBtn.isDisabled()) {
                selectedAct = acts.get(currentIndex - 1);
                buildUI();
            }
            return false;
        });
        nextBtn.addListener(e -> {
            if (nextBtn.isPressed() && !nextBtn.isDisabled()) {
                selectedAct = acts.get(currentIndex + 1);
                buildUI();
            }
            return false;
        });

        actNav.add(prevBtn).width(32).height(32);
        actNav.add(actLabel).width(80);
        actNav.add(nextBtn).width(32).height(32);
        topBar.add(actNav).right().expandX();
        stage.addActor(topBar);

        // Adventure markers on map
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        for (AdventureData adv : adventures) {
            Table marker = new Table(skin);
            marker.pad(8);

            Label nameLabel = new Label(adv.name, skin);
            nameLabel.setFontScale(0.9f);
            Label descLabel = new Label(adv.description, skin);
            descLabel.setColor(Color.LIGHT_GRAY);
            descLabel.setFontScale(0.75f);
            descLabel.setWrap(true);

            TextButton playBtn = new TextButton("Enter", skin);
            playBtn.addListener(e -> {
                if (playBtn.isPressed()) {
                    game.setSelectedAdventure(adv);
                    game.setScreen(new LobbyScreen(game));
                }
                return false;
            });

            marker.add(nameLabel).left().row();
            marker.add(descLabel).width(160).left().padBottom(6).row();
            marker.add(playBtn).expandX().fillX().height(30);

            marker.setPosition(adv.mapX * sw, adv.mapY * sh);
            marker.setSize(180, 100);
            stage.addActor(marker);
        }

        // Back button
        Table bottomBar = new Table();
        bottomBar.setFillParent(true);
        bottomBar.bottom().pad(12);
        TextButton backBtn = new TextButton("Change Character", skin);
        backBtn.addListener(e -> {
            if (backBtn.isPressed()) game.setScreen(new CharacterScreen(game));
            return false;
        });
        bottomBar.add(backBtn).width(180).height(36);
        stage.addActor(bottomBar);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(mapTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { dispose(); }
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();
        if (mapTexture != null) mapTexture.dispose();
    }
}
