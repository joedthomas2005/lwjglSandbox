import java.math.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;
import java.util.ArrayList;

class Main{
    public static void main(String[] args){
        
        glfwInit();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Window window = new Window(1366, 768, "window", false, 2, 1.0f, 1.0f, 1.0f);
        window.create();
        Input.enable(window);

        ShaderProgram shader = new ShaderProgram("Batch");
        shader.use();

        OrthoCamera2D camera = new OrthoCamera2D(0, 0, window.getWidth(), window.getHeight(), shader.getID());
        camera.uploadProjectionUniform();

        TextureAtlas texAtlas = new TextureAtlas("resources/numbers.png", 2, 2, true);
        TextureAtlas michael = new TextureAtlas("resources/download.png", 2, 2, true);
        texAtlas.load(shader.getID());
        michael.load(shader.getID());

        BatchedRenderer squareRenderer = BatchedRenderer.SquareRenderer();
        GameObject square = squareRenderer.create(window.getWidth() / 2.0f, window.getHeight() / 2.0f, 0, 200, 200, 0, texAtlas);

        Animator animator = new Animator();
        animator.addAnimation(new Animation(square, 1, 0, 2), "count12");
        animator.addAnimation(new Animation(square, 1, 2, 4), "count34");
        double time;
        double lastTime = glfwGetTime();
        boolean generating = false;
        int objCount = 0;
        while(!window.shouldClose()){
            glClear(GL_COLOR_BUFFER_BIT);
            time = glfwGetTime();
            float delta = (float) (time - lastTime);
            animator.animate(time);

            if(Input.keyPressedDown(GLFW_KEY_SPACE)){
                if(animator.isPlaying("count34")){
                    animator.startAnimation("count12");
                }
                else {
                    animator.startAnimation("count34");
                }
            }
            if(Input.keyPressedDown(GLFW_KEY_LEFT_SHIFT)){
                square.setTextureAtlas(michael);
            }

            if(Input.keyPressedDown(GLFW_KEY_P)){
                if(animator.isPlaying("count12")){
                    animator.pauseAnimation("count12");
                }
                else{
                    animator.resumeAnimation("count12");
                }
            }
            if(Input.keyPressedDown(GLFW_KEY_ESCAPE)){
                window.close();
            }
            //Update gl buffers and uniforms
            camera.uploadViewUniform();
            //Draw calls
            squareRenderer.drawAll(false);
            //Flip window buffers and poll input
            window.update();
            lastTime = time;
        }

        window.end();
    }
}