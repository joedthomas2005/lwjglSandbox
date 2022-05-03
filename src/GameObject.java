/**
 * An abstract thing with spacial position, rotation and size and a color value.
 * It contains a transform matrix.
 */
public class GameObject {
    
    private Matrix transform = Matrix.Identity(4);
    private int texture = 0;
    public boolean updated;
    private float x, y, rot, width, height;

    public GameObject(float x, float y, float rot, float width, float height, int texture){
    
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
     * Update the object's position and set its update flag to true. Does not regenerate the transform matrix.
     * @param x
     * @param y
     */
    public void move(float x, float y){
        this.x += x;
        this.y += y;
        this.updated = true;
    }

    public void move(Vector movement){
        this.x += movement.getX();
        this.y += movement.getY();
        this.updated = true;
    }
    /** 
     * Update the object's rotation and set its update flag to true. Does not regenerate the transform matrix.
     * @param rot
     */
    public void rotate(float rot){
        this.rot -= rot;
        this.updated = true;
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
  
    public float getHeight(){
        return this.height;
    }

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
