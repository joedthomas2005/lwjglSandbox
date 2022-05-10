import java.util.ArrayList;
import java.util.HashMap;

public class Animator {
    private final HashMap<String, Animation> animations = new HashMap<>();
    private double time;
    public Animator(){
        this.time = 0;
    }

    public void addAnimation(Animation animation, String name){
        this.animations.put(name, animation);
    }

    public void startAnimation(String name){
        for(Animation anim : animations.values()){
            if(anim.isPlaying() && anim.getObject() == animations.get(name).getObject()){
                System.out.println("WARNING: ANIMATION ALREADY PLAYING. INTERRUPTING.");
                anim.stop();
                break;
            }
        }
        this.animations.get(name).start(time);
    }

    public void animate(double time){
        this.time = time;
        for(Animation anim : animations.values()){
            anim.update(time);
        }
    }

    public void stopAnimation(String name){
        this.animations.get(name).stop();
    }

    public boolean isPlaying(String name){
        return this.animations.get(name).isPlaying();
    }
}
