import static org.lwjgl.opengl.GL33.*;

/**
 * An orthographic 2D camera which maps points in world space directly to screen space. 
 * No transformation is done based on depth or perspective so this is ideal for 
 * 2D rendering. An openGL shader program ID must be passed to the constructor as the 
 * view and perspective matrices are constrolled by this and therefore the camera must know 
 * the program where the uniforms are stored.
 */
public class OrthoCamera2D {
    private Matrix view;
    private final Matrix projection;
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

    
    /** 
     * Move the camera. (translate everything else by the inverse)
     * @param x
     * @param y
     */
    public void move(float x, float y){
        this.x -= x;
        this.y -= y;
        this.view = Matrix.Translation(this.x, this.y, 0);
        this.updated = true;
    }

    
    /** 
     * Return the view matrix of the camera.
     * @return Matrix
     */
    public Matrix getView(){
        return this.view;
    }

    
    /** 
     * Return the projection matrix of the camera. This will not change. 
     * @return Matrix
     */
    public Matrix getProjection(){
        return this.projection;
    }

    /**
     * Send the view matrix of this camera to the shader if it has changed.
     * If the camera has not moved then nothing will be done.
     */
    public void uploadViewUniform(){
        if(updated){
            System.out.println("Updating Matrix Uniforms");
            glUniformMatrix4fv(viewLocation, true, view.toArray());
            updated = false;
        }
    }

    /**
     * Send the projection matrix of this camera to the shader.
     * You should only have to do this once. 
     */
    public void uploadProjectionUniform(){
        glUniformMatrix4fv(projectionLocation, true, projection.toArray());
    }
}
