import java.util.ArrayList;

import org.w3c.dom.Text;

import static org.lwjgl.opengl.GL33.*;

/**
 * This renderer draws a set of vertices and indices using openGL instancing, meaning that 
 * instead of data being passed per vertex or per draw call, one draw call is made and an array 
 * of data for all the instances is sent to the shader program as a VBO. 
 * The vertices and indices are then drawn a specified number of times using the data sent through the VBO.
 * This renderer specifies a transform matrix and color array for each instance which is taken from ObjectInstance 
 * objects.
 */
public class InstancedRenderer {
    private final VAO vao;
    private final ArrayList<ObjectInstance> instances = new ArrayList<>(); //This contains the data for the attrib arrays for each object
    private final ArrayList<GameObject> objects = new ArrayList<>();           //This contains references to the actual objects themselves

    public InstancedRenderer(float[] vertices, int[] indices){

        System.out.println("SETTING UP NEW VAO");
        vao = new VAO(vertices, indices, 1);
        //Bind VAO
        vao.use();
        //Upload VBO and set attributes
        vao.uploadVertexData(GL_STATIC_DRAW);
        vao.addVertexArrayAttribute(0, 2, 4, 0); //x, y unit vertices
        vao.addVertexArrayAttribute(1, 2, 4, 2); //x, y tex coords
        //Upload instance array and set attributes
        vao.uploadInstanceData(0);
        //Transform matrix
        vao.addInstanceArrayAttribute(0, 2, 4, 33, 0);
        vao.addInstanceArrayAttribute(0, 3, 4, 33, 4);
        vao.addInstanceArrayAttribute(0, 4, 4, 33, 8);
        vao.addInstanceArrayAttribute(0, 5, 4, 33, 12);
        //Texture transform matrix
        vao.addInstanceArrayAttribute(0, 6, 4, 33, 16);
        vao.addInstanceArrayAttribute(0, 7, 4, 33, 20);
        vao.addInstanceArrayAttribute(0, 8, 4, 33, 24);
        vao.addInstanceArrayAttribute(0, 9, 4, 33, 28);
        //Texture Atlas
        vao.addInstanceArrayAttribute(0, 10, 1, 33, 32);
        //Upload index array (EBO)
        vao.uploadIndexData(GL_STATIC_DRAW);

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
    public static InstancedRenderer SquareRenderer(){
        return new InstancedRenderer(new float[]{
            0.5f, 0.5f, 1.0f, 1.0f,
            0.5f, -0.5f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.0f, 1.0f
        }, new int[]{
            0, 1, 3,
            3, 2, 1
        });
    }

    /**
     * Construct a new gameObject and add it to the renderers list of objects to draw.
     * @param x the object's x position
     * @param y the object's y position
     * @param rot the object's clockwise rotation
     * @param width the object's width (x scale)
     * @param height the object's height (y scale)
     * @param texture the object's texture index within the atlas
     * @param atlas the atlas to get the object's texture from
     * @return the created GameObject
     */
    public GameObject create(float x, float y, float rot, float width, float height, int texture, TextureAtlas atlas){
        GameObject object = new GameObject(x, y, rot, width, height, texture, atlas);
        objects.add(object);
        return object;
    }

    public void remove(GameObject object){
        objects.remove(object);
    }

    public void remove(int object){
        objects.remove(object);
    }

    public void clear(){
        objects.clear();
    }

    /**
     * Update all objects' transform matrices if they have been updated since the last draw call, then 
     * if any of them have been updated, regenerate the array of objectInstances with the new data. 
     */
    private void updateInstanceData(){
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
                this.instances.add(new ObjectInstance(object));
                object.updated = false;
            }
            uploadInstanceData();
        }
    }

    /**
     * Send the stored instance data to the instance VBO target. 
     */
    private void uploadInstanceData(){
        float[] bufferData = new float[instances.size() * ObjectInstance.length];

        for(int instance = 0; instance < instances.size(); instance++){
            float[] instanceData = instances.get(instance).toFloatArray();
            for(int f = 0; f < ObjectInstance.length; f++){
                bufferData[instance * ObjectInstance.length + f] = instanceData[f];
            }
        }

        vao.setInstanceArrayData(0, bufferData);
        vao.uploadInstanceData(0);
    }

    /**
     * Instanced draw all objects created by this renderer.
     * @param rebind whether we need to rebind this VAO (do this if you have multiple renderers).
     */
    public void drawAll(boolean rebind){
        if(rebind) {
            vao.use();
        }
        updateInstanceData();
        vao.drawInstanced(instances.size());
    }
}

