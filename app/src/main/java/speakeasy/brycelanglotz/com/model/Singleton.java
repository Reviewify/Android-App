package speakeasy.brycelanglotz.com.model;

/**
 * Created with IntelliJ IDEA.
 * Date: 13/05/13
 * Time: 10:36
 */
public class Singleton {
    private static Singleton mInstance = null;

    private Meals mScannedMeal;

    private Singleton(){
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public Meals getScannedMeal(){
        return this.mScannedMeal;
    }

    public void setScannedMeal(Meals value){
        mScannedMeal = value;
    }
}