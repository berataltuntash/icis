package com.icis.demo.Entity;

public class OnlineUser {
        private String jwtToken;
        private String username;
        private String email;

        public OnlineUser(String jwtToken, String username, String email) {
            this.jwtToken = jwtToken;
            this.username = username;
            this.email = email;
        }

        public OnlineUser() {
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
}
