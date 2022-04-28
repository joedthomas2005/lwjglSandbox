import org.joml.*;
public class GameObject {
    
    private Matrix4f transform = new Matrix4f();
    private Vector3f color = new Vector3f();
    public boolean updated;

    public GameObject(float x, float y, float rot, float width, float height, 
    float r, float g, float b){
        this.updated = true;
        this.transform = new Matrix4f()
            .translate(x, y, 0)
            .rotateXYZ(0, 0, rot)
            .scale(width, height, 1);
        this.color = new Vector3f(r, g, b);
    }

    public Matrix4f getTransform(){
        return this.transform;
    }

    public Vector3f getColor(){
        return this.color;
    }
    
}
