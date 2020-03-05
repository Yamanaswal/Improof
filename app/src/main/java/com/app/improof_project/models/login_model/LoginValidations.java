package com.app.improof_project.models.login_model;

public class LoginValidations {

        public static boolean isEmail(String emailAddress){
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(emailAddress.matches(emailPattern) && emailAddress.length() > 0){
                return true;
            }
            return false;
        }

        public static boolean isPassword(String password){

            int passwordLength = password.length();
            if(passwordLength > 5){
                return  true;
            }
            return false;
        }

        public boolean isEmailFilled(String emailAddress){
            if(!emailAddress.isEmpty())
            {
                return true;
            }
            return false;
        }

        public boolean isPasswordFilled(String emailAddress){
            if(!emailAddress.isEmpty() && emailAddress.length() > 6)
            {
                return true;
            }

            return false;
        }




}
