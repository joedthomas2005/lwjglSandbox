import static org.lwjgl.opengl.GL33.*;

public class OrthoCamera2D {
    private Matrix view;
    private Matrix projection;
    private int viewLocation;
    private int projectionLocation;
    private float x, y;
    private boolean updated;

    public OrthoCamera2D(float x, float y, float width, float height, int shaderProg){
        this.view = Matrix.Translation(-x, -y, 0);
        this.projection = Matrix.Ortho(0, width, 0, height, -1, 1);
        this.viewLocation = glGetUniformLocation(shaderProg, "view");
        this.projectionLocation = glGetUniformLocation(shaderProg, "projection");
        this.updated = true;
    }

    public void move(float x, float y){
        this.x -= x;
        this.y -= y;
        this.view = Matrix.Translation(x, y, 0);
        this.updated = true;
    }

    public Matrix getView(){
        return this.view;
    }

    public Matrix getProjection(){
        return this.projection;
    }

    public void updateShaderData(){
        if(updated){
            System.out.println("Updating Matrix Uniforms");
            glUniformMatrix4fv(viewLocation, true, view.toArray());
            glUniformMatrix4fv(projectionLocation, true, projection.toArray());
            updated = false;
        }
    }
}
