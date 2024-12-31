#version 150

//Vanilla uniforms
uniform sampler2D DiffuseSampler;
uniform vec2 InSize;

//Custom uniforms
uniform sampler2D OtherDiffuseSampler;
uniform float BlendAmount;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

void main() {
    vec4 colBase = texture(DiffuseSampler, texCoord);
    vec4 colOther = texture(OtherDiffuseSampler, texCoord);
    fragColor = mix(colBase, colOther, BlendAmount);
}