import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
class Main{
    public static void main(String[] args){

        Window window = new Window(1366, 768, "window", false, 2, 0.0f, 0.0f, 0.0f);
        window.create();
        Input.enable(window);

        ShaderProgram shader = new ShaderProgram("Batch");
        shader.use();

        OrthoCamera2D camera = new OrthoCamera2D(0, 0, window.getWidth(), window.getHeight(), shader.getID());
        camera.uploadProjectionUniform();

        TextureAtlas michael = new TextureAtlas("resources/download.png", 1, 1, true);
        michael.load(shader.getID());

        BatchedRenderer squareRenderer = BatchedRenderer.SquareRenderer();
        GameObject square = squareRenderer.create(window.getWidth() / 2.0f, window.getHeight() / 2.0f, 0, 200, 200, 0, michael);

        Animator animator = new Animator();
        double lastTime = glfwGetTime();

        while(!window.shouldClose()){
            glClear(GL_COLOR_BUFFER_BIT);
            double time = glfwGetTime();
            float delta = (float) (time - lastTime);
            animator.animate(time);

            if(Input.keyIsDown(GLFW_KEY_UP)){
                square.moveForward(10f);
            }

            if(Input.keyIsDown(GLFW_KEY_RIGHT)){
                square.rotate(5f);
            }

            if(Input.keyIsDown(GLFW_KEY_LEFT)){
                square.rotate(-5f);
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