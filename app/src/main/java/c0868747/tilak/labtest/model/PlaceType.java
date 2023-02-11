package c0868747.tilak.labtest.model;

public enum PlaceType {
    HOME("Home"),
    WORK("Work"),
    OTHER("Other");

    String type;

    PlaceType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return getType();
    }
}
