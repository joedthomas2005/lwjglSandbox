import java.util.ArrayList;

public class VBO {
    ArrayList<Float> data = new ArrayList<>();
    public VBO(float... data){
        for(float value : data){
            this.data.add(value);
        }
    }
}
