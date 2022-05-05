import java.math.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;

class Main{
    public static void main(String[] args){
        
        glfwInit();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Window window = new Window(1366, 768, "window", true, 0, 1.0f, 1.0f, 1.0f);
        window.create();
        Input.enable(window);

        ShaderProgram shader = new ShaderProgram("Batched");
        shader.create();
        shader.use();

        OrthoCamera2D camera = new OrthoCamera2D(0, 0, 1920, 1080, shader.getID());
        camera.uploadProjectionUniform();

        TextureAtlas michael = new TextureAtlas("resources/download.png", 1, 1, true);
        michael.load(shader.getID());
        michael.bind();

        BatchedRenderer squareRenderer = BatchedRenderer.SquareRenderer();
        GameObject square = squareRenderer.create(200, 200, 0, 200, 200, 0, michael);
        double time;
        double lastTime = glfwGetTime();
        while(!window.shouldClose()){
            time = glfwGetTime();
            float delta = (float) (time - lastTime);
            glClear(GL_COLOR_BUFFER_BIT);
            //GAME LOGIC

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
        }
        window.end();
    }
}