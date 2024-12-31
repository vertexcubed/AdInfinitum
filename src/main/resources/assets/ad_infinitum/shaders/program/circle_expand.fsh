#version 150

#moj_import <lodestone:common_math.glsl>

//Vanilla uniforms
uniform sampler2D DiffuseSampler;
uniform mat4 ProjMat;
uniform vec2 InSize;

//Lodestone uniform
uniform float time;
uniform vec3 cameraPos;
uniform mat4 invProjMat;
uniform mat4 invViewMat;
uniform sampler2D MainDepthSampler;

//Custom uniforms
uniform float Thickness;
uniform float Speed;
uniform vec3 WorldCenter;


in vec2 texCoord;
out vec4 fragColor;

float sdfCircle(vec3 point, float radius) {
    return length(point) - radius;
}

void main() {


    vec3 fragWorldPos = getWorldPos(MainDepthSampler, texCoord, invProjMat, invViewMat, cameraPos);

    float circle = sdfCircle(fragWorldPos - WorldCenter, time * Speed);
    circle = step(0.0 - Thickness / 2.0, circle) * (1.0 - step(Thickness / 2.0, circle));

    fragColor = max(texture(DiffuseSampler, texCoord), vec4(circle, circle, circle, 1.0));

}