#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform float Strength;
uniform float Lower;
uniform float Upper;

out vec4 fragColor;


void main() {


    float maxLuminance = 0.0;
    vec4 brightestColor = vec4(0.0);

    for(float r = -Strength; r <= Strength; r += 1.0) {
        for(float c = -Strength; c <= Strength; c += 1.0) {

            // samples in a spherical shape
            if (!(length(vec2(r, c)) <= Strength)) { continue; }

            vec4 color = texture(DiffuseSampler, texCoord + (oneTexel * vec2(r, c)));
            float luminance = dot(color.rgb, vec3(0.21, 0.72, 0.07));
            if(luminance > maxLuminance) {
                maxLuminance = luminance;
                brightestColor = color;
            }

        }
    }

    vec4 baseColor = texture(DiffuseSampler, texCoord);

    fragColor = mix(baseColor, brightestColor, smoothstep(Lower, Upper, maxLuminance));
    fragColor.a = 1.0;


}