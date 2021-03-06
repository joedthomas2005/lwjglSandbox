import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWKeyCallbackI;

public final class Input {
    private static final boolean[] keysHeld = new boolean[GLFW_KEY_LAST];
    private static final boolean[] keysFirstPressed = new boolean[GLFW_KEY_LAST];
    private static final boolean[] keysReleased = new boolean[GLFW_KEY_LAST];

    public static void enable(Window window){

        glfwSetKeyCallback(window.getWindow(), (long windowHandle, int key, int scancode, int action, int mods) -> {
            if(action == GLFW_PRESS){
                Input.keysHeld[key] = true;
                Input.keysFirstPressed[key] = true;
            }
            else if(action == GLFW_RELEASE){
                Input.keysHeld[key] = false;
                Input.keysReleased[key] = true;
            }
        });
    }

    public static void resetEvents(){
        for(int i = 0; i < GLFW_KEY_LAST; i++){
            keysFirstPressed[i] = false;
            keysReleased[i] = false;
        }
    }
    public static boolean keyIsDown(int key){
        return keysHeld[key];
    }
    public static boolean keyPressedDown(int key){
        return keysFirstPressed[key];
    }
    public static boolean keyReleased(int key){
        return keysReleased[key];
    }

}
