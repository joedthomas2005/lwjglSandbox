import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;

class Main{
    public static void main(String[] args) throws IOException{
        
        glfwInit();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Window window = new Window(900, 800, "window", false, 1, 1.0f, 1.0f, 1.0f);
        window.create();
        Input.enable(window);

        ShaderProgram shader = new ShaderProgram("vertShader.vert", "fragShader.frag");
        shader.create();
        shader.use();
        
        OrthoCamera2D camera = new OrthoCamera2D(0, 0, 900, 800, shader.getID());
        camera.uploadProjectionUniform();
        
        InstancedRenderer squareRenderer = InstancedRenderer.SquareRenderer();
        double time = glfwGetTime();
        double lastTime = glfwGetTime();

        
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
            squareRenderer.updateInstanceData();
            //Draw calls
            squareRenderer.drawAll();
            //Flip window buffers and poll input
            window.update();
            lastTime = time;
        }
        window.end();
    }
}