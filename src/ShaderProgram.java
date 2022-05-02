import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;

import static org.lwjgl.opengl.GL33.*;

/**
 * A shader program consists of a vertex and fragment shader. This class creates an 
 * openGL shader program from a vertex and fragment shader path. 
 */
public class ShaderProgram {
    
    private String vertexPath;
    private String fragPath;
    private String vertexSource = //Default fallback vertex shader 
    """
        #version 330 core
        layout(location = 0) in vec2 aPosition;
        layout(location = 1) in mat4 aTransform;
        layout(location = 5) in vec3 aColor;
        
        uniform mat4 view;
        uniform mat4 projection;
        
        out vec3 color;
        
        void main()
        {
            gl_Position = projection * view * aTransform * vec4(aPosition, 0.0, 1.0);
            color = aColor;
        }
            """;

    private String fragSource = //Default fallback fragment shader
    """
        #version 330 core
        in vec3 color;
        
        out vec4 FragColor;
        
        void main(){
            FragColor = vec4(color, 1.0);
        }
            """;

    private int programID = 0;

    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath){
        this.vertexPath = vertexShaderPath;
        this.fragPath = fragmentShaderPath;
    }

    /**
     * Create an openGL shader program and return its program ID.
     * @return the created program ID.
     */
    public int create(){
        System.out.println("STARTED SHADER BINDING: " + glGetError());
        System.out.println("LOADING SHADER SOURCE");
        
        try{
            vertexSource = Files.readString(Path.of("resources/" + vertexPath));
            fragSource = Files.readString(Path.of("resources/" + fragPath));
        }
        catch(IOException exception){
            System.err.println("COULDN'T LOAD SHADER SOURCE. Loading Built-In Fallback.\nError is " + exception);
        }

        int vShader = glCreateShader(GL_VERTEX_SHADER);
        int fShader = glCreateShader(GL_FRAGMENT_SHADER);
        System.out.println("SHADERS CREATED: " + glGetError());

        glShaderSource(vShader, vertexSource);
        glShaderSource(fShader, fragSource);
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

        this.programID = program;
        return program;        
    }

    /**
     * Enable this shader program in the openGL context. 
     */
    public void use(){
        glUseProgram(programID);
    }

    /**
     * @return this shader program's openGL ID.
     */
    public int getID(){
        return this.programID;
    }
}
