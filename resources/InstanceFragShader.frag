#version 330 core
uniform sampler2D aTexture[8];

in mat4 texTransform;
in vec2 texCoord;
in float textureMap;

out vec4 FragColor;


void main(){

    vec4 transformedTexCoord = texTransform * vec4(texCoord, 0, 1);
    FragColor = texture(aTexture[int(textureMap)], vec2(transformedTexCoord.x, transformedTexCoord.y));
}