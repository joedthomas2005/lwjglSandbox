import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;

public class VAO {
    
    private final int ID;
    private final int VBO;
    private final int EBO;
    private final int[] INSTANCEBUFFERS;

    private float[] vertices;
    private int[] indices;
    
    private ArrayList<float[]> instanceData = new ArrayList<float[]>();
    
    public VAO(float[] vertices, int[] indices, int instanceArrayCount){
        
        this.vertices = vertices;
        this.indices = indices;

        this.ID = glGenVertexArrays();        
        this.VBO = glGenBuffers();
        this.EBO = glGenBuffers();

        this.INSTANCEBUFFERS = new int[instanceArrayCount];
        for(int i = 0; i < instanceArrayCount; i++){
            this.INSTANCEBUFFERS[i] = glGenBuffers();
            this.instanceData.add(i, new float[]{});
        }
    }
    
    public void use(){
        glBindVertexArray(this.ID);
    }

    public void setInstanceArrayData(int array, float[] data){
        this.instanceData.set(array, data);
    }
    
    public void uploadVertexData(){        
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_STATIC_DRAW);
    }

    public void uploadAllInstanceData(){
        
        for(int i = 0; i < INSTANCEBUFFERS.length; i++){
            glBindBuffer(GL_ARRAY_BUFFER, this.INSTANCEBUFFERS[i]);
            glBufferData(GL_ARRAY_BUFFER, this.instanceData.get(i), GL_DYNAMIC_DRAW);
        }
    }

    public void uploadInstanceData(int array){
        glBindBuffer(GL_ARRAY_BUFFER, this.INSTANCEBUFFERS[array]);
        glBufferData(GL_ARRAY_BUFFER, this.instanceData.get(array), GL_DYNAMIC_DRAW);
    }

    public void uploadIndexData(){
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_STATIC_DRAW);
    }

    public void addVertexArrayAttribute(int index, int size, int stride, int start){
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES, start * Float.BYTES);
    }

    public void addInstanceArrayAttribute(int buffer, int index, int size, int stride, int start){
        glBindBuffer(GL_ARRAY_BUFFER, this.INSTANCEBUFFERS[buffer]);
        System.err.println("BOUND BUFFER " + buffer + ": " + glGetError());
        glEnableVertexAttribArray(index);
        System.err.println(index + " INSTANCE ARRAY ATTRIBUTE ENABLED: " + glGetError());
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES, start * Float.BYTES);
        System.err.println("VERTEX ATTRIB POINTER ENABLED: " + glGetError());
        glVertexAttribDivisor(index, 1);
        System.err.println("VERTEX ATTRIB DIVISOR SET: " + glGetError());
    }

    public void draw(int count){
        glDrawElementsInstanced(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0, count);
    }

}
