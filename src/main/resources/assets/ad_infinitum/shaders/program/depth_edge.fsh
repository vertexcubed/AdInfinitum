#version 150

//Vanilla uniforms
uniform sampler2D DiffuseSampler;
uniform vec2 InSize;

//Lodestone depth buffer
uniform sampler2D MainDepthSampler;

//Lodestone uniforms
uniform float nearPlaneDistance;
uniform float farPlaneDistance;

//Custom uniforms
uniform float Thickness;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;


float linearize_depth(float d, float near, float far) {
    float z_n = 2.0 * d - 1.0;
    float z_eye = 2.0 * near * far / (far + near - z_n * (far - near));
    return (z_eye - near) / (far - near);
}

void main() {

    float near = nearPlaneDistance;
    float far = farPlaneDistance;

    //Copy pasted from mojang's sobel edge detection shader
    float center = linearize_depth(texture(MainDepthSampler, texCoord).r, near, far);
//    float left   = linearize_depth(texture(MainDepthSampler, texCoord - vec2(oneTexel.x * Thickness, 0.0)).r, near, far);
//    float right  = linearize_depth(texture(MainDepthSampler, texCoord + vec2(oneTexel.x * Thickness, 0.0)).r, near, far);
//    float up     = linearize_depth(texture(MainDepthSampler, texCoord - vec2(0.0, oneTexel.y * Thickness)).r, near, far);
//    float down   = linearize_depth(texture(MainDepthSampler, texCoord + vec2(0.0, oneTexel.y * Thickness)).r, near, far);

    float half_width_f = floor(Thickness * 0.5);
    float half_width_c = ceil(Thickness * 0.5);


    float left   = linearize_depth(texture(MainDepthSampler, texCoord + oneTexel * vec2(half_width_f, half_width_c) * vec2(-1.0, 1.0)).r, near, far);
    float right  = linearize_depth(texture(MainDepthSampler, texCoord + oneTexel * vec2(half_width_c, half_width_c) * vec2(1.0, 1.0)).r, near, far);
    float up     = linearize_depth(texture(MainDepthSampler, texCoord + oneTexel * vec2(half_width_f, half_width_f) * vec2(-1.0, -1.0)).r, near, far);
    float down   = linearize_depth(texture(MainDepthSampler, texCoord + oneTexel * vec2(half_width_c, half_width_f) * vec2(1.0, -1.0)).r, near, far);



    float leftDiff  = center - left;
    float rightDiff = center - right;
    float upDiff    = center - up;
    float downDiff  = center - down;
    float edge = clamp(leftDiff + rightDiff + upDiff + downDiff, 0.0, 1.0);

    edge = step(0.0001, edge);

    fragColor = vec4(edge, edge, edge, 1.0f);
}