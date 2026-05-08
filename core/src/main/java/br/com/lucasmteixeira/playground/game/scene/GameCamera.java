package br.com.lucasmteixeira.playground.game.scene;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class GameCamera {

    public enum Mode { ISOMETRIC, SIDE_VIEW }

    private final PerspectiveCamera cam;
    private Mode mode = Mode.ISOMETRIC;

    // Orbit parameters
    private float distance = 30f;
    private float yaw = 45f;   // horizontal angle (degrees)
    private float pitch = 35f; // vertical angle (degrees)

    // Target the camera looks at
    private final Vector3 target = new Vector3(0, 0, 0);

    // Zoom limits
    private static final float MIN_DIST = 10f;
    private static final float MAX_DIST = 80f;

    // Side-view parameters
    private static final float SIDE_PITCH = 0f;
    private static final float SIDE_YAW = 90f;

    public GameCamera(int viewportWidth, int viewportHeight) {
        cam = new PerspectiveCamera(60, viewportWidth, viewportHeight);
        cam.near = 0.1f;
        cam.far = 300f;
        updateCameraPosition();
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.SIDE_VIEW) {
            yaw = SIDE_YAW;
            pitch = SIDE_PITCH;
        } else {
            yaw = 45f;
            pitch = 35f;
        }
        updateCameraPosition();
    }

    public Mode getMode() { return mode; }

    public void zoom(float amount) {
        distance = MathUtils.clamp(distance + amount, MIN_DIST, MAX_DIST);
        updateCameraPosition();
    }

    public void rotate(float deltaYaw, float deltaPitch) {
        yaw += deltaYaw;
        pitch = MathUtils.clamp(pitch + deltaPitch, 5f, 85f);
        updateCameraPosition();
    }

    public void setTarget(float x, float y, float z) {
        target.set(x, y, z);
        updateCameraPosition();
    }

    public void lerp(float x, float y, float z, float alpha) {
        target.lerp(new Vector3(x, y, z), alpha);
        updateCameraPosition();
    }

    private void updateCameraPosition() {
        float yawRad = yaw * MathUtils.degreesToRadians;
        float pitchRad = pitch * MathUtils.degreesToRadians;

        float camX = target.x + distance * MathUtils.cos(pitchRad) * MathUtils.cos(yawRad);
        float camY = target.y + distance * MathUtils.sin(pitchRad);
        float camZ = target.z + distance * MathUtils.cos(pitchRad) * MathUtils.sin(yawRad);

        cam.position.set(camX, camY, camZ);
        cam.lookAt(target);
        cam.up.set(Vector3.Y);
        cam.update();
    }

    public void resize(int width, int height) {
        cam.viewportWidth = width;
        cam.viewportHeight = height;
        cam.update();
    }

    public PerspectiveCamera getCamera() { return cam; }
    public Vector3 getTarget() { return target; }
    public float getDistance() { return distance; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
}
