package helper_classes_and_methods;

class Element {
    private String key;
    private Dwelling value;


    /**
     * Author: Juliang Xiao u7757949
     * Constructs a new Element with the specified key and value.
     */
    public Element(String key, Dwelling value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Author: Juliang Xiao u7757949
     * Returns the key of the element.
     */
    public String getKey() {
        return key;
    }

    /**
     * Author: Juliang Xiao u7757949
     * Sets the key of the element.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Author: Juliang Xiao u7757949
     * Returns the value of the element.
     */
    public Dwelling getValue() {
        return value;
    }

    /**
     * Author: Juliang Xiao u7757949
     * Sets the value of the element.
     */
    public void setValue(Dwelling value) {
        this.value = value;
    }

    /**
     * Author: Juliang Xiao u7757949
     * Returns a string representation of the element.
     */
    @Override
    public String toString() {
        return key + ": " + value;
    }

    /**
     * Author: Juliang Xiao u7757949
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        if (key != null ? !key.equals(element.key) : element.key != null) return false;
        return value != null ? value.equals(element.value) : element.value == null;
    }

    /**
     * Author: Juliang Xiao u7757949
     * Returns a hash code value for the element.
     */
    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}