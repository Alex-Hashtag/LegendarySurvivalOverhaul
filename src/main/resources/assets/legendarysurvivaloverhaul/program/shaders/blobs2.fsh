#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform float Radius;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 oneTexel = 1.0 / InSize;

    vec4 c = texture(DiffuseSampler, texCoord);
    vec4 maxVal = c;

    for (float u = 0.0; u <= Radius; u += 1.0) {
        for (float v = 0.0; v <= Radius; v += 1.0) {
            float dist = sqrt(u * u + v * v) / Radius;
            float weight = dist > 1.0 ? 0.0 : 1.0;

            vec2 offset = vec2(u, v) * oneTexel;

            vec4 s0 = texture(DiffuseSampler, texCoord + vec2(-offset.x, -offset.y));
            vec4 s1 = texture(DiffuseSampler, texCoord + vec2( offset.x,  offset.y));
            vec4 s2 = texture(DiffuseSampler, texCoord + vec2(-offset.x,  offset.y));
            vec4 s3 = texture(DiffuseSampler, texCoord + vec2( offset.x, -offset.y));

            vec4 tempMax = max(max(s0, s1), max(s2, s3));
            maxVal = mix(maxVal, max(maxVal, tempMax), weight);
        }
    }

    fragColor = vec4(maxVal.rgb, 1.0);
}
