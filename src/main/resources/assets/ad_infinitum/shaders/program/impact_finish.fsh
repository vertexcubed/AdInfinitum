#version 150

#moj_import <ad_infinitum:fast_noise_lite.glsl>

#define TWO_PI 6.28318531


//Vanilla uniforms
uniform sampler2D DiffuseSampler;
uniform mat4 ProjMat;
uniform vec2 InSize;

//Lodestone uniform
uniform float time;
uniform vec3 cameraPos;

//Custom uniforms
uniform float Scale;
uniform float Speed;
uniform mat4 ViewMat;
uniform vec3 Center;


in vec2 texCoord;
out vec4 fragColor;

vec2 polar(vec2 cartesian) {
    float dist = length(cartesian);
    float angle = atan(cartesian.y, cartesian.x);
    return vec2(dist, angle / TWO_PI);
}


void main() {

    fnl_state state = fnlCreateState(1337);
    state.noise_type = FNL_NOISE_OPENSIMPLEX2;
    state.fractal_type = FNL_FRACTAL_FBM;
    //state.frequency = .01f;
    //state.octaves = 4;
    //state.lacunarity = 2.f;
    state.gain = .5f;

    vec4 color = texture(DiffuseSampler, texCoord);

    vec2 coords = polar(texCoord * 2.0 - 1.0);

    float angle = abs(coords.y) * Scale;

    float noiseAngle = fnlGetNoise2D(state, 50.0f + time * Speed, angle) / 2.f + 0.5f;

    noiseAngle = step(0.3, noiseAngle) * (1. - step(0.8, noiseAngle));


    fragColor = color * (1.0 - noiseAngle);

    fragColor.a = 1.0;

}