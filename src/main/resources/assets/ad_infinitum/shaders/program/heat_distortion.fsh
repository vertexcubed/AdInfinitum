#version 150

#moj_import <ad_infinitum:fast_noise_lite.glsl>

//Vanilla uniforms
uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
//Custom uniforms
uniform float Intensity;
uniform float Scale;
//Passed in by Lodestone. The vanilla "Time" only goes from 0-1.
uniform float time;

in vec2 texCoord;
out vec4 fragColor;

void main() {

    //We're using Fast noise lite to create morphing noise.
    fnl_state state = fnlCreateState(69);
    state.noise_type = FNL_NOISE_OPENSIMPLEX2;
    state.fractal_type = FNL_FRACTAL_FBM;
    state.octaves = 4;

    float noise = fnlGetNoise3D(state, texCoord.x * 1000.0 * Scale, time * 10.f, (texCoord.y * 1000.0 * Scale) - (time * 10.0f));

    vec2 distortedUv = vec2(texCoord.x + ((noise / 1000.0) * Intensity), texCoord.y);

    vec4 texColor = texture(DiffuseSampler, distortedUv);


    // Output to screen
    //fragColor = vec4(noise,noise,noise,1.0);

    fragColor = texColor;
}