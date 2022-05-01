import java.util.ArrayList;
import static org.lwjgl.opengl.GL33.glGetError;

public class Renderer {
    private VAO vao;
    private final float[] vertices;
    private final int[] indices;

    private ArrayList<ObjectInstance> instances = new ArrayList<ObjectInstance>(); //This contains the data for the attrib arrays for each object
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();           //This contains references to the actual objects themselves

    private Renderer(float[] vertices, int[] indices){

        this.vertices = vertices;
        this.indices = indices;
        System.out.println("SETTING UP NEW VAO");
        vao = new VAO(vertices, indices, 1);
        //Bind VAO
        vao.use();
        //Upload VBO and set attributes
        vao.uploadVertexData();
        vao.addVertexArrayAttribute(0, 2, 2, 0); //x, y unit vertices
        //Upload instance array and set attributes
        vao.uploadInstanceData(0);
        //Transform matrix
        vao.addInstanceArrayAttribute(0, 1, 4, 19, 0); 
        vao.addInstanceArrayAttribute(0, 2, 4, 19, 4);
        vao.addInstanceArrayAttribute(0, 3, 4, 19, 8);
        vao.addInstanceArrayAttribute(0, 4, 4, 19, 12);
        //Color vector
        vao.addInstanceArrayAttribute(0, 5, 3, 19, 16);
        //Upload index array (EBO)
        vao.uploadIndexData();

        int err = glGetError();
        if(err != 0){
            System.err.println("FAILED TO SET UP VAO: " + err);
        }
    }

    //Static constructors
    public static Renderer SquareRenderer(){
        return new Renderer(new float[]{
            0.5f, 0.5f,
            0.5f, -0.5f,
            -0.5f, -0.5f,
            -0.5f, 0.5f
        }, new int[]{
            0, 1, 3,
            3, 2, 1
        });
    }

    public GameObject create(float x, float y, float rot, float width, float height, float r, float g, float b){
        GameObject object = new GameObject(x, y, rot, width, height, r, g, b);
        objects.add(object);
        return object;
    }

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
                this.instances.add(new ObjectInstance(object));
                object.updated = false;
            }
            uploadInstanceData();
        }
    }

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

    public void drawAll(){
        vao.draw(instances.size());
    }
}

