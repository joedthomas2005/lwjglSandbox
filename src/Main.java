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

        Window window = new Window(900, 800, "window", false, 1, 1.0f, 1.0f, 1.0f);
        window.create();
        Input.enable(window);

        ShaderProgram shader = new ShaderProgram("vertShader.vert", "fragShader.frag");
        shader.create();
        shader.use();
        
        OrthoCamera2D camera = new OrthoCamera2D(0, 0, 900, 800, shader.getID());
        camera.uploadProjectionUniform();

        TextureAtlas texture = new TextureAtlas("resources/download.png", 2, 2);
        texture.load();

        InstancedRenderer squareRenderer = InstancedRenderer.SquareRenderer(texture);
        GameObject[] squares = new GameObject[4];
        for(int i = 0; i < 4; i++){
            int xOffset = -1;
            int yOffset = -1; 
            
            if(i % 2 != 0){
                xOffset = 1;
            }
            if(i > 1){
                yOffset = 1;
            }

            squares[i] = squareRenderer.create(450 + 100 * xOffset, 400 + 100 * yOffset, 0, 200, 200, i);
        }
        double time = glfwGetTime();
        double lastTime = glfwGetTime();

        boolean playing = false;
        int direction = 0;

        while(!window.shouldClose()){
            time = glfwGetTime();
            float delta = (float) (time - lastTime);
            glClear(GL_COLOR_BUFFER_BIT);   
            //Game logic goes here:

            if(playing){
                squares[0].move(new Vector(225 + direction, 300).multiply(delta));
                squares[1].move(new Vector(315 + direction, 300).multiply(delta));
                squares[2].move(new Vector(135 + direction, 300).multiply(delta));
                squares[3].move(new Vector(45 + direction, 300).multiply(delta));
            }

            if(squares[0].getX() - squares[0].getWidth()/2 <= 0 || squares[0].getX() + squares[0].getWidth()/2 >= 450){
               direction += 180;
            }
            if(Input.keyPressedDown(GLFW_KEY_SPACE)){
                playing = true;
            }
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