#version 150

in vec3 Position;

uniform mat4 ProjMat;
uniform vec2 InSize;
uniform vec2 OutSize;

out vec2 texCoord;

void main() {
    gl_Position = ProjMat * vec4(Position, 1.0);
    texCoord = Position.xy / OutSize;
}
