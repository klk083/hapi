package Hapi.GUI.Subscription;

/**
 * Created by Knut on 21.04.2016.
 */
public class ListeElement2 {
    private String id;

    public ListeElement2(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return this.id;
    }

    public String getId() {
        return id;
    }

}