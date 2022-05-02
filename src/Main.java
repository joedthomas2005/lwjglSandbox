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

        Window window = new Window(900, 800, "window", false, 1, 1.0f, 1.0f, 1.0f);
        window.create();
        Input.enable(window);

        ShaderProgram shader = new ShaderProgram("vertShader.vert", "fragShader.frag");
        shader.create();
        shader.use();
        
        OrthoCamera2D camera = new OrthoCamera2D(0, 0, 900, 800, shader.getID());
        camera.uploadProjectionUniform();
        
        InstancedRenderer squareRenderer = InstancedRenderer.SquareRenderer();
        PhysicsObject squareA = new PhysicsObject(squareRenderer.create(200, 400, 0, 20, 20, 1, 0, 0), 1f);
        PhysicsObject squareB = new PhysicsObject(squareRenderer.create(700, 400, 0, 20, 20, 1, 0, 0), 1f);
        double time = glfwGetTime();
        double lastTime = glfwGetTime();

        
        while(!window.shouldClose()){
            time = glfwGetTime();
            float delta = (float) (time - lastTime);
            glClear(GL_COLOR_BUFFER_BIT);   
            //Game logic goes here:
            squareA.reset();
            squareB.reset();
            
            //Friction
            //squareA.xForce -= 1f * squareA.xVelocity * squareA.xVelocity * (squareA.xVelocity > 0 ? 1 : -1);
            //squareB.xForce -= 1f * squareB.xVelocity * squareB.xVelocity * (squareB.xVelocity > 0 ? 1 : -1); 
            
            if(Input.keyPressedDown(GLFW_KEY_RIGHT)){
                squareA.xForce += 200f;
            }
            if(Input.keyPressedDown(GLFW_KEY_LEFT)){
                squareA.xForce -= 200f;
            }
            
            if(squareA.isColliding(squareB)){
                squareB.xForce += 1/2 * squareA.xVelocity * squareA.mass;
                squareA.xForce -= 1/2 * squareA.xVelocity * squareA.mass;
            }

            System.out.println(squareA.xForce + ", " + squareA.xVelocity);
            squareA.move(delta);
            squareB.move(delta);

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