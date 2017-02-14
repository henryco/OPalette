attribute vec4 a_Position;
attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information we will pass in.

uniform mat4 u_MVPMatrix;

varying vec4 v_Position;
varying vec4 v_WorldPos;
varying vec2 v_TexCoordinate;   // This will be passed into the fragment shader.


void main() {

    v_Position = a_Position;
    v_WorldPos = u_MVPMatrix * a_Position;
    v_TexCoordinate = a_TexCoordinate;
    gl_Position = v_WorldPos;
}
