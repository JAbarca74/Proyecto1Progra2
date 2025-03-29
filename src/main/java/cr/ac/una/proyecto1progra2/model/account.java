package cr.ac.una.proyecto1progra2.model;

public class account {

    private String name;
    private String id;
    private int age;
    private String username;
    private String password;

    public account(String name, String id, int age, String username, String password) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validatePassword(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }
}
