#version 330 core
layout(location = 0) in vec2 aPosition;
layout(location = 1) in mat4 aTransform;
layout(location = 5) in vec3 aColor;

uniform mat4 view;
uniform mat4 projection;

out vec3 color;

void main()
{
    gl_Position = projection * view * aTransform * vec4(aPosition, 0.0, 1.0);
    color = aColor;
}
