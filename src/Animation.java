import java.util.ArrayList;

public class Animation {
    private final GameObject object;
    private final ArrayList<Integer> frames = new ArrayList<>();
    private final double frameInterval;
    private boolean playing;
    private double startTime;
    private int currentFrame;

    public Animation(GameObject object, double interval, int... frames){
        for(int frame : frames) this.frames.add(frame);
        this.frameInterval = interval;
        this.object = object;
        this.playing = false;
        this.currentFrame = 0;
        this.startTime = 0;
    }
    public Animation(GameObject object, double interval, int start, int end){
        for(int i = start; i < end; i++) this.frames.add(i);
        this.frameInterval = interval;
        this.object = object;
        this.playing = false;
        this.currentFrame = 0;
        this.startTime = 0;
    }

    public void start(double time){
        this.playing = true;
        this.startTime = time;
    }

    public void update(double time){
        if(playing) {
            currentFrame = (int) Math.floor((time - startTime) / frameInterval); //How many times has the frame interval occurred
            currentFrame %= frames.size();//Loop animation
            this.object.setTexture(frames.get(currentFrame));
        }
    }

    public void stop(){
        this.playing = false;
    }
    public boolean isPlaying(){
        return this.playing;
    }
    public GameObject getObject(){
        return this.object;
    }
}
