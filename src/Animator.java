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
//                System.out.println("WARNING: ANIMATION ALREADY PLAYING. INTERRUPTING.");
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

    public void pauseAnimation(String name){
        this.animations.get(name).pause();
    }

    public void resumeAnimation(String name){
        this.animations.get(name).resume(time);
    }

    public void pauseAnimations(){
        for(Animation anim : animations.values()){
            anim.pause();
        }
    }

    public void resumeAnimations(){
        for(Animation anim : animations.values()){
            anim.resume(time);
        }
    }

    public void pauseAnimations(String... names){
        for(String name : names) {
            animations.get(name).pause();
        }
    }

    public void resumeAnimations(String... names){
        for(String name : names){
            animations.get(name).resume(time);
        }
    }
    public void stopAnimations(){
        for(Animation anim : animations.values()){
            anim.stop();
        }
    }

    public void startAnimations(){
        for(Animation anim : animations.values()){
            anim.start(time);
        }
    }

    public void stopAnimations(String... names){
        for(String name : names){
            animations.get(name).stop();
        }
    }

    public void startAnimations(String... names){
        for(String name : names){
            animations.get(name).start(time);
        }
    }
    public void stopAnimation(String name){
        this.animations.get(name).stop();
    }

    public boolean isPlaying(String name){
        return this.animations.get(name).isPlaying();
    }
}
