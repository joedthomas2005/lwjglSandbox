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
