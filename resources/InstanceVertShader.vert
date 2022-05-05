#version 330 core
layout(location = 0) in vec2 aPosition;
layout(location = 1) in vec2 aTexCoord;
layout(location = 2) in mat4 aTransform;
layout(location = 6) in mat4 aTexTransform;
layout(location = 10) in float aTexAtlas;

uniform mat4 view;
uniform mat4 projection;

out mat4 texTransform;
out vec2 texCoord;
out float textureMap;
void main()
{
    gl_Position = projection * view * aTransform * vec4(aPosition, 0.0, 1.0);
    texTransform = aTexTransform;
    texCoord = aTexCoord;
    textureMap = aTexAtlas;
}
