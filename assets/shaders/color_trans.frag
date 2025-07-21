#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec3 u_colorToReplace;
uniform float u_tolerance;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);
    float diff = distance(color.rgb, u_colorToReplace);
    
    if (diff <= u_tolerance) {
        gl_FragColor = vec4(0.0);
    } else {
        gl_FragColor = color * v_color;
    }
}