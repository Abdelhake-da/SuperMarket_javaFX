package sample;

public class Category_Type {
    int ID;
    String Name;
    String Description;

    public Category_Type(int ID, String name, String description) {
        this.ID = ID;
        Name = name;
        Description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
