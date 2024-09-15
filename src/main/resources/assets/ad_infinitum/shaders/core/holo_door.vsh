#version 150

#moj_import <fog.glsl>
#moj_import <ad_infinitum:fast_noise_lite.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

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
out vec4 normal;


float sdCircle( in vec2 p, in float r )
{
    return length(p)-r;
}

void main() {

    texCoord0 = UV0;
    texCoord2 = UV2;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);

    fnl_state noise = fnlCreateState(69);
    noise.noise_type = FNL_NOISE_PERLIN;

    vec2 noiseCoord = texCoord0 * vec2(400.0, 400.0);
    float noiseValue = fnlGetNoise3D(noise, noiseCoord.x, Time, noiseCoord.y) * 0.5;
    float circle = 1. - smoothstep(0.0, 0.125, clamp(0.0, 1.0, sdCircle(texCoord0 - 0.5, 0.375)));
    noiseValue *= circle;
    vec3 pos = Position + (normal.xyz * noiseValue);

    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
    vertexDistance = fog_distance(ModelViewMat, IViewRotMat * pos, FogShape);


}