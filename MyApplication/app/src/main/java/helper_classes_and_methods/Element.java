package helper_classes_and_methods;

class Element {
    private String key;
    private Dwelling value;

    public Element(String key, Dwelling value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Dwelling getValue() {
        return value;
    }

    public void setValue(Dwelling value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key + ": " + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        if (key != null ? !key.equals(element.key) : element.key != null) return false;
        return value != null ? value.equals(element.value) : element.value == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}