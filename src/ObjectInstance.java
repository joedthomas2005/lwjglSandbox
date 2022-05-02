/**
 * This contains data for an individual instance of an object.
 * It has a transform matrix and color array. This can either be constructed 
 * with a transform matrix and RGB color array passed or (better) with a gameObject
 * object. Either way every instance should preferably correspond with an instance of 
 * an actual manipulatable gameObject.
 */
public class ObjectInstance {
    public Matrix transform;
    public float[] color;
    public static final int length = 19;
    
    public ObjectInstance(Matrix transform, float[] color){
        this.transform = transform;
        this.color = color;
    }

    public ObjectInstance(GameObject object){
        this.transform = object.getTransform();
        this.color = object.getColor();
    }

    
    /** 
     * Return this instance as an array of floats which can be added to the 
     * instance array for the shader program. This is laid out as 16 floats 
     * for the transform matrix followed by 3 for the color value.
     * @return float[]
     */
    public float[] toFloatArray(){
        float[] transformArray = transform.transpose().toArray();
        
        float[] data = new float[19];
        for(int i = 0; i < transformArray.length; i++){
            data[i] = transformArray[i];
        }
        data[16] = color[0];
        data[17] = color[1];
        data[18] = color[2];

        return data;
    }
}
