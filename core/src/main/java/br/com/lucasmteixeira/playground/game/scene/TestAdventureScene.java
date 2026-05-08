package br.com.lucasmteixeira.playground.game.scene;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class TestAdventureScene implements Scene {

    private static final float GRAVITY = -30f;
    private static final float JUMP_VEL = 12f;
    private static final float MOVE_SPEED = 8f;
    private static final float PATH_LENGTH = 40f;
    private static final float PATH_WIDTH = 6f;
    private static final float SPHERE_RADIUS = 0.5f;

    private static final Color[] PLAYER_COLORS = {
        Color.CYAN, Color.CORAL, Color.LIME, Color.VIOLET, Color.GOLD, Color.SKY
    };

    private final List<PlayerSphere> players = new ArrayList<>();
    private PlayerSphere localPlayer;

    private Environment environment;
    private Model groundModel;
    private Model sphereModel;
    private ModelInstance groundInstance;

    private final InputProcessor input;

    public TestAdventureScene(String localPlayerId) {
        // Lighting
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBuilder mb = new ModelBuilder();

        // Ground plane (path)
        groundModel = mb.createBox(PATH_LENGTH, 0.2f, PATH_WIDTH,
            new Material(ColorAttribute.createDiffuse(0.3f, 0.5f, 0.3f, 1f)),
            Usage.Position | Usage.Normal);
        groundInstance = new ModelInstance(groundModel);
        groundInstance.transform.setToTranslation(PATH_LENGTH / 2f, -0.1f, 0);

        // Shared sphere model
        sphereModel = mb.createSphere(SPHERE_RADIUS * 2, SPHERE_RADIUS * 2, SPHERE_RADIUS * 2, 16, 16,
            new Material(ColorAttribute.createDiffuse(Color.WHITE)),
            Usage.Position | Usage.Normal);

        // Local player
        localPlayer = new PlayerSphere(localPlayerId, PATH_LENGTH / 2f, 0f, PLAYER_COLORS[0]);
        players.add(localPlayer);

        input = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE && localPlayer.onGround) {
                    localPlayer.velY = JUMP_VEL;
                    localPlayer.onGround = false;
                    return true;
                }
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                // Handled by GameScreen for camera zoom
                return false;
            }
        };
    }

    public void addPlayer(String playerId) {
        if (players.stream().anyMatch(p -> p.id.equals(playerId))) return;
        Color color = PLAYER_COLORS[players.size() % PLAYER_COLORS.length];
        players.add(new PlayerSphere(playerId, PATH_LENGTH / 2f, 0f, color));
    }

    public void removePlayer(String playerId) {
        players.removeIf(p -> p.id.equals(playerId));
    }

    public void updateRemotePlayer(String playerId, float x, float y, float z) {
        players.stream().filter(p -> p.id.equals(playerId)).findFirst().ifPresent(p -> {
            p.x = x;
            p.y = y;
            p.z = z;
        });
    }

    public PlayerSphere getLocalPlayer() { return localPlayer; }

    @Override
    public void update(float delta) {
        // Movement input
        float dx = 0, dz = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) dz -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) dz += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        if (dx != 0 || dz != 0) {
            float len = (float) Math.sqrt(dx * dx + dz * dz);
            dx /= len; dz /= len;
            localPlayer.x += dx * MOVE_SPEED * delta;
            localPlayer.z += dz * MOVE_SPEED * delta;
            // Clamp to path
            localPlayer.x = Math.max(0, Math.min(PATH_LENGTH, localPlayer.x));
            localPlayer.z = Math.max(-PATH_WIDTH / 2f, Math.min(PATH_WIDTH / 2f, localPlayer.z));
        }

        // Gravity
        if (!localPlayer.onGround) {
            localPlayer.velY += GRAVITY * delta;
            localPlayer.y += localPlayer.velY * delta;
            if (localPlayer.y <= SPHERE_RADIUS) {
                localPlayer.y = SPHERE_RADIUS;
                localPlayer.velY = 0;
                localPlayer.onGround = true;
            }
        }
    }

    @Override
    public void render(ModelBatch modelBatch, GameCamera camera) {
        // Follow local player
        camera.lerp(localPlayer.x, 0, localPlayer.z, 0.08f);

        modelBatch.begin(camera.getCamera());

        // Ground
        modelBatch.render(groundInstance, environment);

        // Players
        for (PlayerSphere p : players) {
            ModelInstance instance = new ModelInstance(sphereModel);
            instance.transform.setToTranslation(p.x, p.y, p.z);
            instance.materials.get(0).set(ColorAttribute.createDiffuse(p.color));
            modelBatch.render(instance, environment);
        }

        modelBatch.end();
    }

    @Override
    public InputProcessor getInputProcessor() { return input; }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void dispose() {
        groundModel.dispose();
        sphereModel.dispose();
    }

    public static class PlayerSphere {
        public String id;
        public float x, y, z; // world position (y = up)
        public float velY;
        public boolean onGround = true;
        public Color color;

        public PlayerSphere(String id, float x, float z, Color color) {
            this.id = id;
            this.x = x;
            this.y = SPHERE_RADIUS;
            this.z = z;
            this.color = color;
        }
    }
}
