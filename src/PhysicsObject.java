public class PhysicsObject {
    GameObject object;
    float xVelocity;
    float yVelocity;
    float xAcceleration;
    float yAcceleration;
    float xForce;
    float yForce;

    float mass;

    public PhysicsObject(GameObject object, float mass){
        this.xVelocity = 0f;
        this.yVelocity = 0f;
        this.xAcceleration = 0f;
        this.yAcceleration = 0f;
        this.object = object;
        this.mass = mass;
    }

    
    public float getXVelocity(){
        return this.xVelocity;
    }
    public float getYVelocity(){
        return this.yVelocity;
    }

    public boolean isColliding(PhysicsObject other){
        float myRHS = object.getX() + object.getWidth() / 2;
        float myLHS = object.getX() - object.getWidth() / 2;
        float myTop = object.getY() + object.getHeight() / 2;
        float myBottom = object.getY() - object.getHeight() / 2;
        float otherRHS = other.object.getX() + other.object.getWidth() / 2;
        float otherLHS = other.object.getX() - other.object.getWidth() / 2;
        float otherTop = other.object.getY() + other.object.getHeight() / 2;
        float otherBottom = other.object.getY() - other.object.getHeight() / 2;
        
        System.out.println("CENTER: " + object.getX() + ", " + object.getY());
        System.out.println("RIGHT HAND SIDE " + myRHS);
        System.out.println("LEFT HAND SIDE " + myLHS);
        System.out.println("TOP " + myTop);
        System.out.println("BOTTOM " + myBottom);
        System.out.println("OTHER CENTER: " + other.object.getX() + ", " + other.object.getY());
        System.out.println("OTHER RHS " + otherRHS);
        System.out.println("OTHER LHS " + otherLHS);
        System.out.println("OTHER TOP " + otherTop);
        System.out.println("OTHER BOTTOM " + otherBottom);


        if(myLHS < otherRHS && myRHS > otherLHS && myBottom < otherTop && myTop > otherBottom){
            return true;
        }
        return false;
    }
    public void move(float deltaTime){
        this.xAcceleration = xForce / mass;
        this.yAcceleration = yForce / mass;

        xVelocity = xVelocity + xAcceleration * deltaTime;
        yVelocity = yVelocity + yAcceleration * deltaTime;
        object.move(xVelocity, yVelocity);
    }

    public void reset(){
        this.xForce = 0;
        this.yForce = 0;
    }
}
