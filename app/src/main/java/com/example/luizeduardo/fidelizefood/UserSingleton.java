package com.example.luizeduardo.fidelizefood;

/**
 * Created by luiz on 15/04/2018.
 */

public class UserSingleton {

    private static UserSingleton userSingleton;

    private User user;

    public User getUser(){
        return this.user;
    }

    public static void create(User user){

        if(userSingleton == null){
            userSingleton = new UserSingleton();
        }

        userSingleton.user = user;
    }

    public static UserSingleton getInstance() {

       // if(userSingleton == null) throw new Exception("O método create nao foi chamado");


        return  userSingleton;

    }


    private void UserSingleton(){

    }

}
