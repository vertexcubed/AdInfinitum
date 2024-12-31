#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

//Vanilla Uniforms
uniform vec2 InSize;
uniform mat4 ProjMat;

//Lodestone Uniforms
uniform vec3 cameraPos;

//Custom uniforms
uniform mat4 ViewMat;
uniform vec3 Center;
uniform float Radius;

out vec4 fragColor;


void main() {

    vec4 clipSpaceCenter = ProjMat * ViewMat * vec4(Center - cameraPos, 1.0);
    clipSpaceCenter /= clipSpaceCenter.w;

    vec2 screenCenter = vec2((clipSpaceCenter / 2.0 + 0.5).xy);


    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    float totalSamples = 0.0;

    vec2 centerDir = normalize(texCoord - vec2(0.5, 0.5));

    for(float r = -Radius; r <= Radius; r += 1.0) {
        vec4 sampleValue = texture(DiffuseSampler, texCoord + oneTexel * r * centerDir);

        // Accumulate average alpha
        totalAlpha = totalAlpha + sampleValue.a;
        totalSamples = totalSamples + 1.0;

        // Accumulate smoothed blur
        float strength = 1.0 - abs(r / Radius);
        totalStrength = totalStrength + strength;
        blurred = blurred + sampleValue;
    }
    fragColor = vec4(blurred.rgb / (Radius * 2.0 + 1.0), totalAlpha);
}