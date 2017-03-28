#version 100

attribute vec4 a_Position;
uniform mat4 u_MVPMatrix;

void main() {
    gl_Position = u_MVPMatrix * a_Position;
}