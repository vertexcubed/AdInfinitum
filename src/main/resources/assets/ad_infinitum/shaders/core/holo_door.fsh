#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform float LumiTransparency;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform sampler2D SceneDepthBuffer;
uniform mat4 InvProjMat;
uniform mat4 ModelViewMat;
uniform vec2 ScreenSize;
uniform float nearPlaneDistance;
uniform float farPlaneDistance;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

vec4 transformColor(vec4 initialColor, float lumiTransparent) {
    initialColor = lumiTransparent == 1. ? vec4(initialColor.xyz, (0.21 * initialColor.r + 0.71 * initialColor.g + 0.07 * initialColor.b)) : initialColor;
    return initialColor * vertexColor * ColorModulator;
}

vec4 applyFog(vec4 initialColor, float fogStart, float fogEnd, vec4 fogColor, float vertexDistance) {
    return linear_fog(vec4(initialColor.rgb, initialColor.a*linear_fog_fade(vertexDistance, fogStart, fogEnd)), vertexDistance, fogStart, fogEnd, vec4(fogColor.rgb, initialColor.r));
}

float linearize_depth(float d) {
    float z_n = 2.0 * d - 1.0;
    float z_eye = 2.0 * nearPlaneDistance * farPlaneDistance / (farPlaneDistance + nearPlaneDistance - z_n * (farPlaneDistance - nearPlaneDistance));
    return (z_eye - nearPlaneDistance) / (farPlaneDistance - nearPlaneDistance);
}

//p: point, b: box dimensions
float sdBox(vec2 p, vec2 b) {
    vec2 d = abs(p)-b;
    return length(max(d,0.0)) + min(max(d.x,d.y),0.0);
}

void main() {
    vec2 uv = texCoord0;
//    vec4 texColor = texture(Sampler0, uv);
    vec4 texColor = vec4(0.0375);
    vec4 edgeColor = vec4(0.5);
//    vec4 edgeColor = vec4(1);

//    vec4 color = transformColor(texColor, LumiTransparency);



    vec2 screenUV = gl_FragCoord.xy / ScreenSize;

    float sceneDepth = linearize_depth(texture(SceneDepthBuffer, screenUV).r);
    float pixelDepth = linearize_depth(gl_FragCoord.z);
    float difference = abs(pixelDepth - sceneDepth);

    float threshold = 0.0005;
    float brightness = 0;
    if (difference > 0) {
        brightness = 1. - (smoothstep(0.00001, threshold, difference) + (1.0 - smoothstep(0.0, 0.00001, difference)));
    }
    float edge = sdBox(uv - vec2(0.5, 0.5), vec2(0.475));
    edge = smoothstep(0.0, 0.025, edge);
    brightness = max(brightness, edge);

    float scanlineDomain = fract(uv.y * 25.0);
    float scan = (smoothstep(0.4, 0.5, scanlineDomain) - smoothstep(0.5, 0.6, scanlineDomain)) * 0.75;
    brightness = max(brightness, scan);

//    color = mix(color, edgeColor, pow(brightness, 4));
    vec4 color = transformColor(mix(texColor, edgeColor, pow(brightness, 3)), LumiTransparency);
    color.a = 1;
    fragColor = applyFog(color, FogStart, FogEnd, FogColor, vertexDistance);



}