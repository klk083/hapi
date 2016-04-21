package Hapi.GUI.Order;

/**
 * Created by Knut on 21.04.2016.
 */
public class ListeElement {
    private String id;
    private String name;

    public ListeElement(String id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
