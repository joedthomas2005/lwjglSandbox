import java.util.ArrayList;
import static org.lwjgl.opengl.GL33.*;

public class GodOfTheSquares {

    private VAO vao;
    private final float[] vertices = {
        0.5f, 0.5f,
        0.5f, -0.5f,
        -0.5f, -0.5f,
        -0.5f, 0.5f
    };

    private final int[] indices = {
        0, 1, 3,
        3, 2, 1
    };

    private ArrayList<ObjectInstance> instances = new ArrayList<ObjectInstance>();
    private ArrayList<GameObject> rects = new ArrayList<GameObject>();

    public GodOfTheSquares(){
        glGetError();
        this.vao = new VAO(vertices, indices, 1); 
        System.err.println("VAO CREATED: " + glGetError());
        vao.use();
        System.err.println("VAO BOUND: " + glGetError());
        vao.uploadVertexData();
        System.err.println("VBO BOUND: " + glGetError());
        vao.addVertexArrayAttribute(0, 2, 2, 0);

        vao.uploadInstanceData(0);
        vao.addInstanceArrayAttribute(0, 1, 4, 19, 0); //Transform
        vao.addInstanceArrayAttribute(0, 2, 4, 19, 4);
        vao.addInstanceArrayAttribute(0, 3, 4, 19, 8);
        vao.addInstanceArrayAttribute(0, 4, 4, 19, 12);
        System.err.println("INSTANCE TRANSFORM ATTRIBUTE ENABLED " + glGetError());
        vao.addInstanceArrayAttribute(0, 5, 3, 19, 16); //Color
        System.err.println("INSTANCE COLOR ATTRIBUTE ENABLED " + glGetError());
        vao.uploadIndexData();
        System.err.println("EBO BOUND: " + glGetError());
    }

    public GameObject createSquare(float x, float y, float rot, float width, float height,
    float r, float g, float b){
        GameObject square = new GameObject(x, y, rot, width, height, r, g, b);
        rects.add(square);
        return square;
    }


    public void updateInstanceData(){
        boolean updated = false;
        for(GameObject rect: rects){
            updated = updated || rect.updated;
            if(rect.updated){
                rect.generateMatrix();
            }
        }

        if(updated){
            this.instances.clear();
            for(GameObject rect: rects){
                this.instances.add(new ObjectInstance(rect.getTransform(), rect.getColor()));
                rect.updated = false;
            }

            uploadInstanceData();
        }
    }

    private void uploadInstanceData(){
        ArrayList<Float> bufferData = new ArrayList<Float>();
        for(ObjectInstance instance : this.instances){
            for(float f : instance.toFloatArray()){
                bufferData.add(f);
            }
        }
        float[] bufferDataArray = new float[bufferData.size()];
        for(int i = 0; i < bufferData.size(); i++){
            bufferDataArray[i] = bufferData.get(i);
        }
        vao.setInstanceArrayData(0, bufferDataArray);
        vao.uploadInstanceData(0);
    }

    public void drawAll(){
        vao.draw(instances.size());
    }
}
