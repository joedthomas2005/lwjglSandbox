import static org.lwjgl.opengl.GL33.*;

public class VAO {
    
    private final int ID;
    private final int VBO;
    private final int EBO;
    private final int INSTANCEBUFFER;

    private float[] vertices;
    private float[] instanceData;
    private int[] indices;

    public VAO(float[] vertices, int[] indices, float[] instanceData){
        
        this.vertices = vertices;
        this.instanceData = instanceData;
        this.indices = indices;

        this.ID = glGenVertexArrays();        
        this.VBO = glGenBuffers();
        this.EBO = glGenBuffers();
        this.INSTANCEBUFFER = glGenBuffers();

    }

    public void use(){
        glBindVertexArray(this.ID);
    }

    public void uploadVertexData(){        
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_STATIC_DRAW);
    }

    public void uploadInstanceData(){
        
        glBindBuffer(GL_ARRAY_BUFFER, this.INSTANCEBUFFER);
        glBufferData(GL_ARRAY_BUFFER, this.instanceData, GL_DYNAMIC_DRAW);
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

    public void addInstanceArrayAttribute(int index, int size, int stride, int start){
        glBindBuffer(GL_ARRAY_BUFFER, this.INSTANCEBUFFER);
        glEnableVertexAttribArray(index);
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES, start * Float.BYTES);
        glVertexAttribDivisor(index, 1);
    }

}
