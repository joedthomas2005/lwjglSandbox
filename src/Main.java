import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;



class Main{
    public static void main(String[] args) throws IOException{
        
        glfwInit();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Window window = new Window(900, 800, "window", false, 1);
        window.create();

        Renderer squareRenderer = Renderer.SquareRenderer();
        ArrayList<GameObject> squares = new ArrayList<GameObject>();

        ShaderProgram shader = new ShaderProgram("vertShader.vert", "fragShader.frag");
        shader.create();
        shader.use();
        OrthoCamera2D camera = new OrthoCamera2D(0, 0, 900, 800, shader.getID());
        
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        Random r = new Random();

        GameObject square = squareRenderer.create(900/2, 800/2, 0, 500, 500, 1, 0, 0);
        final float GRAVITY = -9.8f;
        float time = (float)glfwGetTime();
        float lastTime = (float)glfwGetTime();
        float delta = 0f;

        while(!window.shouldClose()){
            time = (float)glfwGetTime();
            delta = time - lastTime;
            glClear(GL_COLOR_BUFFER_BIT);   
            //Game logic goes here:


            //Update gl buffers and uniforms
            camera.updateShaderData();
            squareRenderer.updateInstanceData();
            //Draw calls
            squareRenderer.drawAll();
            //Flip window buffers
            window.update();
            lastTime = time;
        }
    }
}