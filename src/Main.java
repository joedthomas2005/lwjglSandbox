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
            
        float[] VBO = {
            0.5f, 0.5f, 1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.0f, 0.0f, 1.0f,
            -0.5f, 0.5f, 1.0f, 1.0f, 1.0f
        };

        int[] EBO = {
            0, 1, 3,
            3, 2, 1
        };

        float[] IVBO = {
            0.5f, 0.5f,
            -0.5f, 0.5f,
            0.5f, -0.5f,
            -0.5f, -0.5f
        };

        VAO vao = createVao(VBO, EBO, IVBO);

        bindShaders("vertShader.vert", "fragShader.frag");

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        while(!window.shouldClose()){
            glClear(GL_COLOR_BUFFER_BIT);
            
            vao.use();
            glDrawElementsInstanced(GL_TRIANGLES, EBO.length, GL_UNSIGNED_INT, 0, 5);
            
            window.update();
        }
    }

    static VAO createVao(float[] vertices, int[] indices, float[] instanceData){
        VAO vao = new VAO(vertices, indices, instanceData);
        vao.use();
        vao.uploadIndexData();

        vao.uploadVertexData();
        vao.addVertexArrayAttribute(0, 2, 5, 0);
        vao.addVertexArrayAttribute(1, 3, 5, 2);
        
        vao.uploadInstanceData();
        vao.addInstanceArrayAttribute(2, 2, 2, 0);

        return vao;
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