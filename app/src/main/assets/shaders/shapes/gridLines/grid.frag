#version 100
precision mediump float;

uniform float u_halfLineSize;
uniform float u_cellW;
uniform float u_cellH;
uniform vec4 u_gridColor;

void main() {

  vec2 pos = gl_FragCoord.xy;

  float nx = floor(pos.x / u_cellW);
  float ny = floor(pos.y / u_cellH);

  float left_x = (nx * u_cellW) + u_halfLineSize;
  float right_x = ((nx + 1.) * u_cellW) - u_halfLineSize;

  float bot_y = (ny * u_cellH) + u_halfLineSize;
  float top_y = ((ny + 1.) * u_cellH) - u_halfLineSize;

  if (pos.x <= left_x || pos.x >= right_x)
    gl_FragColor = u_gridColor;
  else if (pos.y <= bot_y || pos.y >= top_y)
    gl_FragColor = u_gridColor;
  else gl_FragColor = vec4(0.);

}
