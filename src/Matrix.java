import java.util.ArrayList;

public class Matrix {
    private float[][] matrix;
    private int rows;
    private int columns;
    public Matrix(float[]... values){
        this.matrix = values;
        this.rows = values.length;
        this.columns = values[0].length;
    }

    @Override
    public String toString(){
        String string = "";
        for(float[] row : matrix){
            for(float column : row){
                string += column + " ";
            }
            string += "\n";
        }
        return string;
    }

    public Matrix add(Matrix other){
        float[][] result = new float[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; i++){
                result[i][j] = this.matrix[i][j] + other.matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    public Matrix multiply(Matrix other){
        float[][] result = new float[rows][columns];
        for(int row = 0; row < rows; row++){
            for(int column = 0; column < columns; column++){
                float sum = 0;
                for(int i = 0; i < columns; i++){
                    sum += matrix[row][i] * other.matrix[i][column];
                }
                result[row][column] = sum;
            }
        }
        return new Matrix(result);
    }

    public static Matrix Identity(int rows){

        float[][] result = new float[rows][rows];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < rows; j++){
                result[i][j] = 0;
                if(i == j){
                    result[i][j] = 1;
                }
            }
        }
        return new Matrix(result);
    }
    
    public static Matrix Translation(float x, float y, float z){
        Matrix result = Identity(4);
        result.matrix[0][3] = x;
        result.matrix[1][3] = y;
        result.matrix[2][3] = z;
        return result;
    }

    public static Matrix Scaling(float x, float y, float z){
        Matrix result = Identity(4);
        result.matrix[0][0] = x;
        result.matrix[1][1] = y;
        result.matrix[2][2] = z;
        return result;
    }

    public static Matrix Rotation(float pitch, float yaw, float roll){
        
        float pitchCosine = (float) Math.cos(Math.toRadians(pitch));
        float pitchSine = (float) Math.sin(Math.toRadians(pitch));
        float yawCosine = (float) Math.cos(Math.toRadians(yaw));
        float yawSine = (float) Math.sin(Math.toRadians(yaw));
        float rollCosine = (float) Math.cos(Math.toRadians(roll));
        float rollSine = (float) Math.sin(Math.toRadians(roll));

        Matrix pitchMatrix = new Matrix(
            new float[]{1, 0, 0, 0},
            new float[]{0, pitchCosine, -pitchSine, 0},
            new float[]{0, pitchSine, pitchCosine, 0},
            new float[]{0, 0, 0, 1}
        );

        Matrix yawMatrix = new Matrix(
            new float[]{yawCosine, 0, yawSine, 0},
            new float[]{0, 1, 0, 0},
            new float[]{-yawSine, 0, yawCosine, 0},
            new float[]{0, 0, 0, 1}
        );

        Matrix rollMatrix = new Matrix(
            new float[]{rollCosine, -rollSine, 0, 0},
            new float[]{rollSine, rollCosine, 0, 0},
            new float[]{0, 0, 1, 0},
            new float[]{0, 0, 0, 1}
        );

        return pitchMatrix.multiply(yawMatrix).multiply(rollMatrix);
    }

    public static Matrix Ortho(float left, float right, float bottom, float top, float near, float far){
        return Matrix.Identity(4).translate(
            -((right + left)/(right - left)),
            -((top + bottom)/(top - bottom)),
            -((far + near)/(far - near))
        ).scale(
            2/(right - left),
            2/(top - bottom),
            -2/(far - near)
        );

    }
    public Matrix transpose(){
        float[][] result = new float[columns][rows];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                result[j][i] = matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    public Matrix translate(float x, float y, float z){
        return this.multiply(Matrix.Translation(x, y, z));
    }

    public Matrix rotate(float pitch, float yaw, float roll){
        return this.multiply(Matrix.Rotation(pitch, yaw, roll));
    }

    public Matrix scale(float x, float y, float z){
        return this.multiply(Matrix.Scaling(x, y, z));
    }

    public float[] toArray(){
        ArrayList<Float> data = new ArrayList<Float>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                data.add(matrix[i][j]);
            }
        }
        float[] result = new float[data.size()];
        for(int i = 0; i < data.size(); i++){
            result[i] = data.get(i);
        }

        return result;
    }

}
