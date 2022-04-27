import static org.lwjgl.glfw.GLFW.*;


class Main{
    public static void main(String[] args){
        
        glfwInit();
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Window window = new Window(800, 600, "window", false, 1);
        window.create();

        while(!window.shouldClose()){
            window.update();
        }
    }
}