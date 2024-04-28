package helper_classes_and_methods;

public interface Subject {
    public void attach(Observer observer);
    public void detach(Observer observer);
    public void notifyAllObservers();
    public  void notifyMaintainer();

}
