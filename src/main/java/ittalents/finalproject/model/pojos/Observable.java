package ittalents.finalproject.model.pojos;

public interface Observable {

    void addObserver();
    void removeObserver();
    void notifyObserver();
}
