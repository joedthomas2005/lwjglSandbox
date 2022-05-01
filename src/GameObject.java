public class GameObject {
    
    private Matrix transform = Matrix.Identity(4);
    private float[] color = new float[3];
    public boolean updated;
    private float x, y, rot, width, height;
    public float velocity;

    public GameObject(float x, float y, float rot, float width, float height, 
    float r, float g, float b){
        
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
        this.color = new float[]{r, g, b};
        this.velocity = 0f;
    }

    public Matrix getTransform(){
        return this.transform;
    }

    public float getVelocity(){
        return this.velocity;
    }

    public float[] getColor(){
        return this.color;
    }

    public void move(float x, float y){
        this.x += x;
        this.y += y;
        this.updated = true;
    }

    public void rotate(float rot){
        this.rot -= rot;
        this.updated = true;
    }

    public float getY(){
        return this.y;
    }

    public float getX(){
        return this.x;
    }

    public void generateMatrix(){
        this.transform = Matrix.Identity(4)
        .translate(x, y, 0)
        .rotate(0, 0, rot)
        .scale(width, height, 1);
    }
    
}
