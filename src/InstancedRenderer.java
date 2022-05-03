import java.util.ArrayList;
import static org.lwjgl.opengl.GL33.glGetError;

/**
 * This renderer draws a set of vertices and indices using openGL instancing, meaning that 
 * instead of data being passed per vertex or per draw call, one draw call is made and an array 
 * of data for all the instances is sent to the shader program as a VBO. 
 * The vertices and indices are then drawn a specified number of times using the data sent through the VBO.
 * This renderer specifies a transform matrix and color array for each instance which is taken from ObjectInstance 
 * objects.
 */
public class InstancedRenderer {
    private VAO vao;
    private TextureAtlas textureSheet;
    private ArrayList<ObjectInstance> instances = new ArrayList<ObjectInstance>(); //This contains the data for the attrib arrays for each object
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();           //This contains references to the actual objects themselves

    public InstancedRenderer(float[] vertices, int[] indices, TextureAtlas textureSheet){
        
        this.textureSheet = textureSheet;
        System.out.println("SETTING UP NEW VAO");
        vao = new VAO(vertices, indices, 1);
        //Bind VAO
        vao.use();
        //Upload VBO and set attributes
        vao.uploadVertexData();
        vao.addVertexArrayAttribute(0, 2, 4, 0); //x, y unit vertices
        vao.addVertexArrayAttribute(1, 2, 4, 2); //x, y tex coords
        //Upload instance array and set attributes
        vao.uploadInstanceData(0);
        //Transform matrix
        vao.addInstanceArrayAttribute(0, 2, 4, 32, 0); 
        vao.addInstanceArrayAttribute(0, 3, 4, 32, 4);
        vao.addInstanceArrayAttribute(0, 4, 4, 32, 8);
        vao.addInstanceArrayAttribute(0, 5, 4, 32, 12);
        //Texture transform matrix
        vao.addInstanceArrayAttribute(0, 6, 4, 32, 16);
        vao.addInstanceArrayAttribute(0, 7, 4, 32, 20);
        vao.addInstanceArrayAttribute(0, 8, 4, 32, 24);
        vao.addInstanceArrayAttribute(0, 9, 4, 32, 28);
        //Upload index array (EBO)
        vao.uploadIndexData();

        int err = glGetError();
        if(err != 0){
            System.err.println("FAILED TO SET UP VAO: " + err);
        }
    }

    //Static constructors

    /**
     * Construct a premade InstancedRenderer with the vertex and index data for a square.
     * @return an InstancedRenderer
     */
    public static InstancedRenderer SquareRenderer(TextureAtlas textureSheet){
        return new InstancedRenderer(new float[]{
            0.5f, 0.5f, 1.0f, 1.0f,
            0.5f, -0.5f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.0f, 1.0f
        }, new int[]{
            0, 1, 3,
            3, 2, 1
        }, textureSheet);
    }

    /**
     * Construct a new gameObject and add it to the renderers list of objects to draw.
     * @param x
     * @param y
     * @param rot
     * @param width
     * @param height
     * @param r
     * @param g
     * @param b
     * @return the created GameObject
     */
    public GameObject create(float x, float y, float rot, float width, float height, int texture){
        GameObject object = new GameObject(x, y, rot, width, height, texture);
        objects.add(object);
        return object;
    }

    /**
     * Update all objects' transform matrices if they have been updated since the last draw call, then 
     * if any of them have been updated, regenerate the array of objectInstances with the new data. 
     */
    public void updateInstanceData(){
        boolean updated = false;
        for(GameObject object : objects){
            updated = object.updated || updated;
            if(object.updated){
                object.generateMatrix();
            }
        }

        if(updated){
            this.instances.clear();
            for(GameObject object : objects){
                this.instances.add(new ObjectInstance(object, textureSheet));
                object.updated = false;
            }
            uploadInstanceData();
        }
    }

    /**
     * Send the stored instance data to the instance VBO target. 
     */
    public void uploadInstanceData(){
        ArrayList<Float> bufferData = new ArrayList<Float>();
        for(ObjectInstance instance : instances){
            for(float f : instance.toFloatArray()){
                bufferData.add(f);
            }
        }

        float[] data = new float[bufferData.size()];
        for(int i = 0; i < bufferData.size(); i++){
            data[i] = bufferData.get(i);
        }
        vao.setInstanceArrayData(0, data);
        vao.uploadInstanceData(0);
    }

    /**
     * Instanced draw all objects created by this renderer.
     */
    public void drawAll(){
        vao.drawInstanced(instances.size());
    }
}

