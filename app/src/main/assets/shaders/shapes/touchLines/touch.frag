#version 100

precision mediump float;

uniform vec2 u_dimension;
uniform vec3 u_line[2]; // Ax + By + C = 0
uniform float u_dx;
uniform float u_transparency;
uniform float u_lines_a;

float line(float n, float x) {
  return (u_lines_a * (x - (u_dx * n)));
}

void main() {
  
  vec2 pos = vec2(gl_FragCoord.x, u_dimension.y - gl_FragCoord.y);
  float py1 = (-1.) * ((u_line[0].x * pos.x) + u_line[0].z) / u_line[0].y;
  float py2 = (-1.) * ((u_line[1].x * pos.x) + u_line[1].z) / u_line[1].y;

  if (!((pos.y > py1 && pos.y < py2) || (pos.y > py2 && pos.y < py1))) {
    vec3 color = vec3(normalize(vec3(pos.x, 1, pos.y)));
    float n1 = ceil(pos.x / u_dx);
    float dy = line(1.,0.);
    float n2 = ceil(pos.y / dy);
    float n = (n1 + n2) - 1.;
    float rest1 = mod(n1, 2.);
    float rest2 = mod(n2, 2.);
    float y_n = line(n, pos.x);

    if (((rest1 != 0. && rest2 != 0.) || (rest1 == 0. && rest2 == 0.)) && (pos.y > y_n))
        gl_FragColor = vec4(color, u_transparency);
    else if (((rest1 != 0. && rest2 == 0.) || (rest1 == 0. && rest2 != 0.)) && (pos.y < y_n))
        gl_FragColor = vec4(color, u_transparency);
  }
  else gl_FragColor = vec4(0.);
}