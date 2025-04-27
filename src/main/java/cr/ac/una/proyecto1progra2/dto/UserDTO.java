package cr.ac.una.proyecto1progra2.dto;

public class UserDTO {
    private Long id;
    private String username;
    private int roleId;
    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
