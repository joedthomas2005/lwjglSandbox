/**
 * This contains data for an individual instance of an object.
 * It has a transform matrix and color array. This can either be constructed 
 * with a transform matrix and RGB color array passed or (better) with a gameObject
 * object. Either way every instance should preferably correspond with an instance of 
 * an actual manipulatable gameObject.
 */
public class ObjectInstance {
    public Matrix transform;
    public Matrix textureTransform;
    public int textureAtlas;
    public static final int length = 33;
    
    public ObjectInstance(Matrix transform, Matrix textureTransform, int textureAtlas){
        this.transform = transform;
        this.textureTransform = textureTransform;
        this.textureAtlas = textureAtlas;
    }

    public ObjectInstance(GameObject object){
        this.transform = object.getTransform();
        this.textureTransform = object.getTextureAtlas().getMatrix(object.getTexture());
        this.textureAtlas = object.getTextureAtlas().getID();
    }

    
    /** 
     * Return this instance as an array of floats which can be added to the 
     * instance array for the shader program. This is laid out as 16 floats 
     * for the transform matrix followed by 3 for the color value.
     * @return float[]
     */
    public float[] toFloatArray(){
        float[] transformArray = transform.transpose().toArray();
        float[] textureArray = textureTransform.transpose().toArray();

        float[] data = new float[33];
        for(int i = 0; i < transformArray.length; i++){
            data[i] = transformArray[i];
        }
        for(int i = 0; i < textureArray.length; i++){
            data[i+16] = textureArray[i];
        }
        data[32] = textureAtlas;

        return data;
    }
}
