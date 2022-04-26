import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;

public class Window {

    private int width;
    private int height;
    private boolean fullscreen;
    private int swapInterval;
    private String title;
    private long windowHandle = 0L;
    private long monitorHandle = 0L;

    public Window(int width, int height, String title, boolean fullscreen, int swapInterval) {
    
        this.width = width;
        this.height = height;
        this.fullscreen = fullscreen;
        this.swapInterval = swapInterval;
        this.title = title;

        if (fullscreen) {
            this.monitorHandle = glfwGetPrimaryMonitor();
        }
    }

    public boolean create() {
        this.windowHandle = glfwCreateWindow(this.width, this.height, this.title, this.monitorHandle, 0);
        
        if (this.windowHandle == 0) {
            glfwTerminate();
            return false;
        }
        
        glfwMakeContextCurrent(this.windowHandle);
        GL.createCapabilities();
        glfwSwapInterval(this.swapInterval);
        glfwSetFramebufferSizeCallback(this.windowHandle, GLFWFramebufferSizeCallback.create((window, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            glViewport(0, 0, newWidth, newHeight);
        }));

        return true;
    }

    public void update(){
        glfwSwapBuffers(this.windowHandle);
        glfwPollEvents();
    }

    public long getWindow(){
        return this.windowHandle;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public int getSwapInterval(){
        return this.swapInterval;
    }

    public String getTitle(){
        return this.title;
    }

    public long getMonitor(){
        return this.monitorHandle;
    }
}
