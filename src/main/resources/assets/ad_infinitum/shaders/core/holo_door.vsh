#version 150

#moj_import <fog.glsl>
#moj_import <ad_infinitum:fast_noise_lite.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat3 IViewRotMat;
uniform mat4 ProjMat;
uniform int FogShape;

uniform float Time;
uniform vec3 DistortionVector;

out vec4 vertexColor;
out float vertexDistance;
out vec2 texCoord0;
out vec2 texCoord2;

void main() {

    texCoord0 = UV0;
    texCoord2 = UV2;

//    fnl_state noise = fnlCreateState(69);
//    noise.noise_type = FNL_NOISE_PERLIN;
//
//    vec2 noiseCoord = texCoord0 * vec2(1920.0, 1080.0);
//    float noiseValue = fnlGetNoise3D(noise, noiseCoord.x + Time, Time, noiseCoord.y) * 0.125;

//    vec3 pos = Position + (DistortionVector * noiseValue);

    vec3 pos = Position;

    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
    vertexDistance = fog_distance(ModelViewMat, IViewRotMat * pos, FogShape);


}