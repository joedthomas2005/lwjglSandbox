import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;



class Main{
    public static void main(String[] args) throws IOException{
        
        glfwInit();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        Window window = new Window(800, 600, "window", false, 1);
        window.create();

        GodOfTheSquares Euclid = new GodOfTheSquares();
        GameObject square = Euclid.createSquare(0f, 0f, 0f, 0.5f, 0.5f,
         1f, 0f, 0f);
         
        Euclid.updateInstanceData();
        Euclid.uploadInstanceData();

        bindShaders("vertShader.vert", "fragShader.frag");

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        while(!window.shouldClose()){
            glClear(GL_COLOR_BUFFER_BIT);
            
            Euclid.draw();
            int err = glGetError();
            if(err != 0){
                System.err.println(err);
            } 
            window.update();
        }
    }

    static int bindShaders(String vertexShader, String fragmentShader) throws IOException{

        System.out.println("STARTED SHADER BINDING: " + glGetError());
        String vsSource = Files.readString(Path.of("resources/" + vertexShader));
        String fsSource = Files.readString(Path.of("resources/" + fragmentShader));
        int vShader = glCreateShader(GL_VERTEX_SHADER);
        int fShader = glCreateShader(GL_FRAGMENT_SHADER);
        System.out.println("SHADERS CREATED: " + glGetError());

        glShaderSource(vShader, vsSource);
        glShaderSource(fShader, fsSource);
        System.out.println("SHADER SOURCE CODE LOADED: " + glGetError());
        
        glCompileShader(vShader);
        if(glGetShaderi(vShader, GL_COMPILE_STATUS) != GL_TRUE){
            System.err.println("ERROR COMPILING VERTEX SHADER: " + glGetShaderInfoLog(vShader));
        }

        glCompileShader(fShader);
        if(glGetShaderi(fShader, GL_COMPILE_STATUS) != GL_TRUE){
            System.err.println("ERROR COMPILING FRAGMENT SHADER: " + glGetShaderInfoLog(fShader));
        }
        System.out.println("SHADER SOURCE CODE COMPILED: " + glGetError());

        int program = glCreateProgram();
        System.out.println("SHADER PROGRAM CREATED: " + glGetError());

        glAttachShader(program, vShader);
        glAttachShader(program, fShader);
        System.out.println("SHADERS ATTACHED: " + glGetError());

        glLinkProgram(program);
        System.out.println("SHADER PROGRAM LINKED: " + glGetError());

        glDeleteShader(vShader);
        glDeleteShader(fShader);
        System.out.println("SHADERS DELETED: " + glGetError());

        glUseProgram(program);
        System.out.println("PROGRAM STARTED: " + glGetError());
        return program;
    }
}