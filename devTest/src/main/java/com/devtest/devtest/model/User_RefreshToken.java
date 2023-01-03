package com.devtest.devtest.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Users")
@Data
public class User_RefreshToken implements Serializable {

        @Id
        String Refresh_token;

        String Email; // person:<id> person:aaa person:kim


        public String getEmail() {
                return Email;
        }

        public void setEmail(String email) {
                Email = email;
        }

        public String getRefresh_token() {
                return Refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
                Refresh_token = refresh_token;
        }
}
