import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;
import java.time.Year;

class Main{
    public static void main(String[] args) throws IOException{
        
        glfwInit();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Window window = new Window(800, 800, "window", false, 0, 1.0f, 1.0f, 1.0f);
        window.create();
        Input.enable(window);

        ShaderProgram shader = new ShaderProgram("BatchedVertShader.vert", "BatchedFragShader.frag");
        shader.create();
        shader.use();
        
        OrthoCamera2D camera = new OrthoCamera2D(0, 0, window.getWidth(), window.getHeight(), shader.getID());
        camera.uploadProjectionUniform();

        TextureAtlas michael = new TextureAtlas("resources/download.png", 1, 1, 0);
        TextureAtlas chidi = new TextureAtlas("resources/chidi.png", 2, 2, 0);
        michael.load(shader.getID());
        chidi.load(shader.getID());
        michael.bind();
        chidi.bind();

        BatchedRenderer squareRenderer = BatchedRenderer.SquareRenderer();
        
        GameObject square = squareRenderer.create(
            window.getWidth()/2, window.getHeight()/2, 
            0, 200, 200, 0, michael);

        double time = glfwGetTime();
        double lastTime = glfwGetTime();

        int direction = 0;
        int currentTexture = 0;

        while(!window.shouldClose()){
            time = glfwGetTime();
            float delta = (float) (time - lastTime);
            glClear(GL_COLOR_BUFFER_BIT);   
            //Game logic goes here:
            
            
            if(Input.keyPressedDown(GLFW_KEY_ESCAPE)){
                window.close();
            }
            //Update gl buffers and uniforms
            camera.uploadViewUniform();
            //Draw calls
            squareRenderer.drawAll();
            //Flip window buffers and poll input
            window.update();
            lastTime = time;

            int err = glGetError();
            if(err != 0){
                System.out.println("ERROR: " + err);
            }
        }
        window.end();
    }
}