#version 330 core
in mat4 texTransform;
in vec2 texCoord;

out vec4 FragColor;
uniform sampler2D textureSheet;

void main(){

    vec4 transformedTexCoord = texTransform * vec4(texCoord, 0, 1);
    FragColor = texture(textureSheet, vec2(transformedTexCoord.x, transformedTexCoord.y));
}