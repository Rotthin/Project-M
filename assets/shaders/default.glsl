#type vert
#version 330 core

layout(location=0) in vec3 aPos;
layout(location=1) in vec4 aColor;
layout(location=2) in vec2 aUvs;
layout(location=3) in float aTexID;

uniform mat4 uProj;
uniform mat4 uView;

out vec4 fColor;
out vec2 fUvs;
out float fTexID;

void main(){
    fUvs = aUvs;
    fTexID = aTexID;
    fColor = aColor;
    gl_Position = uProj * uView * vec4(aPos, 1.0);
}

#type frag
#version 330 core

in vec4 fColor;
in vec2 fUvs;
in float fTexID;

uniform sampler2D uTextures[8];

out vec4 color;

void main(){
    if(fTexID > 0){
        color = fColor * texture(uTextures[int(fTexID)], fUvs);
    }else color = fColor;
}