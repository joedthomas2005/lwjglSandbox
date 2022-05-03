import java.nio.ByteBuffer;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class TextureAtlas {
    
    private String path;
    private int columns;
    private int rows;
    private int width = 0;
    private int height = 0;
    private int channels = 0;
    private float columnWidth = 0;
    private float rowHeight = 0;
    private int ID = 0; 

    public TextureAtlas(String path, int numberOfColumns, int numberOfRows){
        this.path = path;
        this.columns = numberOfColumns;
        this.rows = numberOfRows;

        this.columnWidth = 1.0f / columns;
        this.rowHeight = 1.0f / rows;
    }

    public int load(){

        int[] widthB = {0}, heightB = {0}, channelsB = {0};
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer data = stbi_load(this.path, widthB, heightB, channelsB, 4);
        data.flip();
        this.width = widthB[0];
        this.height = heightB[0];
        this.channels = channelsB[0];

        ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, ID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(data);
        data.clear();

        return ID;
    }

    public Matrix getMatrix(int texture){
        
        int textureX = texture;
        int textureY = 0;
        while(textureX > columns - 1){
            textureX -= columns;
            textureY++;  
        }

        return Matrix.Translation(textureX * columnWidth, textureY * rowHeight, 0).scale(columnWidth, rowHeight, 1);
    }

}
