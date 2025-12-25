INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO users (username, password, status, fail_count)
VALUES ('user1', '$2a$10$xxxx...', 'ACTIVE', 0);
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

-- UPDATE users
-- SET password = '$2a$10$QKfubtrVLNTa00Rr9VUylulw1vBDOmnAqdtjxICGrkdwf3ugB8tn2'
-- WHERE username = 'user1';

