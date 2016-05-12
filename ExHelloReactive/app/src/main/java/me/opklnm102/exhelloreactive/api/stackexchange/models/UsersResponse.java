package me.opklnm102.exhelloreactive.api.stackexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016-05-12.
 */
public class UsersResponse {

    @SerializedName("items")
    @Expose
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
