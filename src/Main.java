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

        Window window = new Window(1920, 1080, "window", false, 0, 1.0f, 1.0f, 1.0f);
        window.create();
        Input.enable(window);

        ShaderProgram shader = new ShaderProgram("Batch");
        shader.create();
        shader.use();
        ShaderProgram instanceShader = new ShaderProgram("Instance");
        OrthoCamera2D camera = new OrthoCamera2D(0, 0, window.getWidth(), window.getHeight(), shader.getID(), instanceShader.getID());
        camera.uploadProjectionUniform();

        TextureAtlas michael = new TextureAtlas("resources/download.png", 1, 1, true);
        TextureAtlas chidi = new TextureAtlas("resources/chidi.png", 1, 1, true);
        michael.load(shader.getID());
        chidi.load(shader.getID());
        michael.bind();
        chidi.bind();

        BatchedRenderer squareRenderer = BatchedRenderer.SquareRenderer();
        InstancedRenderer instancedRenderer = InstancedRenderer.SquareRenderer();


        double time;
        double lastTime = glfwGetTime();
        boolean generating = false;
        int objCount = 0;
        while(!window.shouldClose()){
            time = glfwGetTime();
            float delta = (float) (time - lastTime);
            glClear(GL_COLOR_BUFFER_BIT);
            squareRenderer.create((float) (Math.random() * window.getWidth()), (float) (Math.random() * window.getHeight()), 0, 100, 100, 0, michael);
            instancedRenderer.create((float) (Math.random() * window.getWidth()), (float) (Math.random() * window.getHeight()), 0, 100, 100, 0, chidi);
            //GAME LOGIC
//            System.out.println(objCount + ": " + 1.0f/delta);
//            if(generating){
//                objCount++;
//                squareRenderer.create((float) (Math.random() * window.getWidth()), (float) (Math.random() * window.getHeight()), 0, 100, 100, 0, michael);
//            }
//
//            if(Input.keyPressedDown(GLFW_KEY_SPACE)){
//                generating = !generating;
//            }

            if(Input.keyPressedDown(GLFW_KEY_ESCAPE)){
                window.close();
            }
            //Update gl buffers and uniforms
            camera.uploadViewUniform();
            //Draw calls
            shader.use();
            squareRenderer.drawAll(false);
            instanceShader.use();
            instancedRenderer.drawAll(true);

            //Flip window buffers and poll input
            window.update();
            lastTime = time;
        }
        window.end();
    }
}