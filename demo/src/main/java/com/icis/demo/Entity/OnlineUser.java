package com.icis.demo.Entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "onlineuser")
public class OnlineUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;
        @Column(name = "jwt_token")
        private String jwtToken;
        @Column(name = "username")
        private String username;
        @Column(name = "email")
        private String email;

        public OnlineUser(String jwtToken, String username, String email) {
            this.jwtToken = jwtToken;
            this.username = username;
            this.email = email;
        }

        public OnlineUser() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJwtToken() {
            return jwtToken;
        }

        public void setJwtToken(String jwtToken) {
            this.jwtToken = jwtToken;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "OnlineUser{" +
                    "id=" + id +
                    ", jwtToken='" + jwtToken + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
}
