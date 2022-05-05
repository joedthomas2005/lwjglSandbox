import org.w3c.dom.Text;

/**
 * An abstract thing with spacial position, rotation and size and a texture from an atlas.
 * It contains a transform matrix.
 */
public class GameObject {
    
    private Matrix transform;
    private int texture;
    private TextureAtlas textureAtlas;
    public boolean updated;
    private float x, y, rot, width, height;

    public GameObject(float x, float y, float rot, float width, float height, int texture, TextureAtlas textureAtlas){
    
        this.x = x;
        this.y = y;
        this.rot = rot;
        this.width = width;
        this.height = height;
        this.updated = true;
        this.transform = Matrix.Identity(4)
            .translate(x, y, 0)
            .rotate(0, 0, rot)
            .scale(width, height, 1);
        this.texture = texture;
        this.textureAtlas = textureAtlas;
    }

    
    /** 
     * @return this object's current transform matrix (an instance of Matrix)
     */
    public Matrix getTransform(){
        return this.transform;
    }
    
    /** 
     * @return this object's texture (an index on a texture sheet)
     */
    public int getTexture(){
        return this.texture;
    }

    /**
     * Set the object's current texture index on its bound atlas.
     * @param texture the texture index for the atlas
     */
    public void setTexture(int texture){
        this.texture = texture;
        this.updated = true;
    }

    /**
     * @return the current texture atlas (spritesheet) which this object's texture is loaded from.
     */
    public TextureAtlas getTextureAtlas(){
        return this.textureAtlas;
    }

    public void setTextureAtlas(TextureAtlas atlas){
        this.textureAtlas = atlas;
        this.updated = true;
    }
    /** 
     * Update the object's position and set its update flag to true. Does not regenerate the transform matrix.
     * @param x the change in the x coordinate
     * @param y the change in the y coordinate
     */
    public void move(float x, float y){
        this.x += x;
        this.y += y;
        this.updated = true;
    }

    /**
     * Apply a vector translation to this object's position and set its update flag to true.
     * Does not regenerate the transform matrix.
     * @param movement the vector 2 translation to apply
     */
    public void move(Vector movement){
        this.x += movement.getX();
        this.y += movement.getY();
        this.updated = true;
    }
    /** 
     * Update the object's rotation and set its update flag to true. Does not regenerate the transform matrix.
     * @param rot the rotation in degrees to apply
     */
    public void rotate(float rot){
        this.rot -= rot;
        this.updated = true;
    }

    /**
     * Set the object's width
     * @param width the new width
     */
    public void setWidth(float width){
        this.width = width;
    }

    /**
     * Set the object's height
     * @param height the new height
     */
    public void setHeight(float height){
        this.height = height;
    }

    /**
     * Set the object's X coordinate
     * @param x the new X position
     */
    public void setX(float x){
        this.x = x;
    }

    /**
     * Set the object's Y coordinate
     * @param y the new Y position
     */
    public void setY(float y){
        this.y = y;
    }

    /**
     * Set the object's rotation
     * @param rot the new rotation in degrees
     */
    public void setRot(float rot){
        this.rot = rot;
    }
    
    /** 
     * @return the object's current y position (float)
     */
    public float getY(){
        return this.y;
    }
    
    /** 
     * @return the object's current x position (float)
     */
    public float getX(){
        return this.x;
    }

    /**
     * @return the object's current height (float)
     */
    public float getHeight(){
        return this.height;
    }

    /**
     * @return the object's current width (float)
     */
    public float getWidth(){
        return this.width;
    }

    /**
     * Generate this object's internal transform matrix.
     */
    public void generateMatrix(){
        this.transform = Matrix.Identity(4)
        .translate(x, y, 0)
        .rotate(0, 0, rot)
        .scale(width, height, 1);
    }
    
}
