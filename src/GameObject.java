import org.joml.*;
public class GameObject {
    
    private Matrix4f transform = new Matrix4f();
    private Vector3f color = new Vector3f();
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
        this.transform = new Matrix4f()
            .translate(x, y, 0)
            .rotateXYZ(0, 0, rot)
            .scale(width, height, 1);
        this.color = new Vector3f(r, g, b);
        this.velocity = 0f;
    }

    public Matrix4f getTransform(){
        return this.transform;
    }

    public float getVelocity(){
        return this.velocity;
    }

    public Vector3f getColor(){
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
        this.transform = new Matrix4f()
        .translate(x, y, 0)
        .rotateXYZ(0, 0, rot)
        .scale(width, height, 1);
    }
    
}
