package pojo;

public enum UserRole {
    USER(1, "User"),
    ADMIN(2, "Admin"),
    GUEST(3, "Guest");

    private int id;
    private String name;

    UserRole(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
