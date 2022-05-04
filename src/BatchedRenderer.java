import java.util.ArrayList;
import static org.lwjgl.opengl.GL33.*;

public class BatchedRenderer {
    private final int VERTEXLENGTH = 5;
    private final int VERTSPEROBJECT;
    private VAO vao;
    private TextureAtlas textureSheet;
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private final float[] vertices;
    private ArrayList<Float> VBOdata = new ArrayList<Float>();
    private ArrayList<Integer> EBOdata = new ArrayList<Integer>();
    private final int[] indices;
    private boolean objectsUpdated;

    public BatchedRenderer(float[] vertices, int[] indices){
        objectsUpdated = false;
        this.vertices = vertices;
        this.indices = indices;
        this.VERTSPEROBJECT = vertices.length / VERTEXLENGTH;
        this.vao = new VAO(new float[]{}, new int[]{}, 0);
        vao.use();
        vao.uploadVertexData(GL_DYNAMIC_DRAW);
        vao.addVertexArrayAttribute(0, 2, VERTEXLENGTH, 0); //coordinates
        vao.addVertexArrayAttribute(1, 2, VERTEXLENGTH, 2); //texture coordinates
        vao.addVertexArrayAttribute(2, 1, VERTEXLENGTH, 4); //texture map
        vao.uploadIndexData(GL_DYNAMIC_DRAW);
        
        int err = glGetError();
        if(err != 0){
            System.err.println("FAILED TO CREATE VAO: " + err);
        }
    }

    public static BatchedRenderer SquareRenderer(){
        return new BatchedRenderer(new float[]{
            0.5f, 0.5f, 1.0f, 1.0f, 0,
            0.5f, -0.5f, 1.0f, 0.0f, 0,
            -0.5f, -0.5f, 0.0f, 0.0f, 0,
            -0.5f, 0.5f, 0.0f, 1.0f, 0
        }, new int[]{
            0, 1, 3,
            3, 2, 1
        });
    }

    public GameObject create(float x, float y, float rot, float width, float height, int texture, TextureAtlas atlas){
        GameObject object = new GameObject(x, y, rot, width, height, texture, atlas);
        objects.add(object);
        objectsUpdated = true;
        return object;
    }

    private void updateVertexData(){
        boolean updated = false;
        for(GameObject object : objects){
            updated = object.updated || updated;
            if(object.updated){
                object.generateMatrix();
            }
        }

        if(updated){
            VBOdata.clear();
            for(GameObject object : objects){

                ArrayList<float[]> vertices = new ArrayList<float[]>();
                for(int i = 0; i < VERTSPEROBJECT; i++){
                    float[] vertexData = new float[VERTEXLENGTH];
                    for(int j = 0; j < VERTEXLENGTH; j++){
                        vertexData[j] = this.vertices[i * VERTEXLENGTH + j];
                    }
                    vertices.add(vertexData);
                }

                ArrayList<Vector> transformVectors = new ArrayList<Vector>();
                ArrayList<Vector> textureVectors = new ArrayList<Vector>();

                for(float[] vertex : vertices){
                    transformVectors.add(Vector.Vec2(vertex[0], vertex[1]));
                    textureVectors.add(Vector.Vec2(vertex[2], vertex[3]));
                }

                for(int v = 0; v < transformVectors.size(); v++){
                    transformVectors.set(v, object.getTransform().multiply(transformVectors.get(v)));
                    System.out.println(transformVectors.get(v));
                }

                for(int v = 0; v < textureVectors.size(); v++){
                    textureVectors.set(v, object.getTextureAtlas().getMatrix(object.getTexture()).multiply(textureVectors.get(v)));
                }

                for(int i = 0; i < VERTSPEROBJECT; i++){
                    VBOdata.add(transformVectors.get(i).getX());
                    VBOdata.add(transformVectors.get(i).getY());
                    VBOdata.add(textureVectors.get(i).getX());
                    VBOdata.add(textureVectors.get(i).getY());

                    VBOdata.add((float)object.getTextureAtlas().getID());
                }
                
                // for(int i = 1; i < vertices.length; i+=2){
                //     vectors.add(Vector.Vec2(vertices[i-1], vertices[i]));
                // }
                // for(int i = 0; i < vectors.size(); i++){
                //     Vector transformedVector;
                //     if(i % 2 == 0){
                //         transformedVector = object.getTransform().multiply(vectors.get(i));
                //     }
                //     else{
                //         transformedVector = textureSheet.getMatrix(object.getTexture()).multiply(vectors.get(i));
                //     }
                //     vectors.set(i, transformedVector);
                // }

                // for(Vector v : vectors){
                //     VBOdata.add(v.getX());
                //     VBOdata.add(v.getY());
                // }
                object.updated = false;
            }
            // for(float f : VBOdata){
            //     System.out.print(f + ", ");
            // }
            System.out.println("\n");
            uploadVertexData();
        }
    }

    private void updateIndexData(){
        if(objectsUpdated){
            this.EBOdata.clear();
            int objectCount = this.objects.size();
            for(int i = 0; i < objectCount; i++){
                int offset = i * VERTSPEROBJECT;
                for(int index : this.indices){
                    EBOdata.add(index + offset);
                }
            }
            objectsUpdated = false;
            uploadIndexData();
        }
    }

    private void uploadIndexData(){
        int[] EBOarray = new int[EBOdata.size()];
        for(int i = 0; i < EBOdata.size(); i++){
            EBOarray[i] = EBOdata.get(i);
        }
        vao.setIndexArrayData(EBOarray);
        vao.uploadIndexData(GL_DYNAMIC_DRAW);
    }

    private void uploadVertexData(){
        float[] VBOarray = new float[VBOdata.size()];
        for(int i = 0; i < VBOdata.size(); i++){
            VBOarray[i] = VBOdata.get(i);
        }
        vao.setVertexArrayData(VBOarray);
        vao.uploadVertexData(GL_DYNAMIC_DRAW);
    }

    public void drawAll(){
        updateVertexData();
        updateIndexData();
        vao.drawIndexed();
    }

}
