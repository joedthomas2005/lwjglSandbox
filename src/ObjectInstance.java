import org.joml.*;
public class ObjectInstance {
    public Matrix4f transform;
    public Vector3f color;
    public static final int length = 19;
    
    public ObjectInstance(Matrix4f transform, Vector3f color){
        this.transform = transform;
        this.color = color;
    }

    public float[] toFloatArray(){
        float[] transform = new float[16];
        this.transform.get(transform);
        
        float[] data = new float[19];
        for(int i = 0; i < transform.length; i++){
            data[i] = transform[i];
        }
        data[16] = color.x;
        data[17] = color.y;
        data[18] = color.z;

        return data;
    }
}
