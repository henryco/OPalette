#version 100
precision mediump float;

uniform float u_halfLineSize;
uniform float u_cellW;
uniform float u_cellH;
uniform vec4 u_gridColor;
uniform vec2 u_dimension;
uniform int u_type;


vec4 colorize(vec2 position) {
    if (u_type == 1) return vec4(normalize(vec3(position.x, 1, position.y)), 1.);
    if (u_type == 2) {
        vec2 xy = position / u_dimension;
        return vec4(normalize(vec3(xy, 1. - length(xy) / sqrt(2.))), 1.);
    }   return u_gridColor;
}

void main() {

  vec2 pos = gl_FragCoord.xy;
  float nx = floor(pos.x / u_cellW);
  float ny = floor(pos.y / u_cellH);

  float left_x = (nx * u_cellW) + u_halfLineSize;
  float right_x = ((nx + 1.) * u_cellW) - u_halfLineSize;

  float bot_y = (ny * u_cellH) + u_halfLineSize;
  float top_y = ((ny + 1.) * u_cellH) - u_halfLineSize;

  if (pos.x <= left_x || pos.x >= right_x)
    gl_FragColor = colorize(pos);
  else if (pos.y <= bot_y || pos.y >= top_y)
    gl_FragColor = colorize(pos);
  else gl_FragColor = vec4(0.);
}
