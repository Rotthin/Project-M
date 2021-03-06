#type vert
#version 330 core

layout(location=0) in vec2 aPos;
layout(location=1) in vec3 aColor;

out vec3 fColor;

uniform mat4 uView;
uniform mat4 uProj;

void main() {
    fColor = aColor;
    gl_Position = uProj * uView * vec4(aPos, -10, 1.0);
}

#type frag
#version 330 core

in vec3 fColor;

out vec4 color;

void main() {
    color = vec4(fColor, 1.0);
}
