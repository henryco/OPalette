attribute vec4  a_Position;
attribute vec2  a_TexCoordinate;

varying vec4    v_Position;
varying vec4    v_WorldPos;
varying vec2    v_TexCoordinate[5];

uniform mat4    u_MVPMatrix;
uniform int     u_texNumb;
uniform vec2    u_Flip[5];

vec2 flip(vec2 f, vec2 tex) {

    float x = min(1., f.x + 1.) - f.x * tex.x;
    float y = min(1., f.y + 1.) - f.y * tex.y;
    return vec2(x, y);
}

void main() {

    v_Position = a_Position;
    v_WorldPos = u_MVPMatrix * a_Position;
    for (int i = 0; i < u_texNumb; i++)
        v_TexCoordinate[i] = flip(vec2(u_Flip[i].x, u_Flip[i].y), a_TexCoordinate);
    gl_Position = v_WorldPos;
}
