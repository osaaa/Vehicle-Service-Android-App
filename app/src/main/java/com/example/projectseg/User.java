package com.example.projectseg;

public class User {
    String _id;
    String username;
    String password;
    String email;
    public enum Role {Admin, Employee, Customer};
    Role role;

    public User(){
    }

    public User(String id, String username, String password, String email, Role role){
        this._id=id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role= role;

    }
    public void setId(String id) {
        _id = id;
    }
    public String getId() {
        return _id;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    public Role getRole(){
        return role;
    }
}
