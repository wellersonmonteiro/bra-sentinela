ALTER TABLE admin
    ADD CONSTRAINT unique_admin_username UNIQUE (username),
    ADD CONSTRAINT unique_admin_email UNIQUE (email);
