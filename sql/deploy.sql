drop DATABASE if exists magnet;
create DATABASE magnet;
use magnet;

drop table if exists users;

-- Number Index for internal testing purpose:
-- 1 to 99 = user_id
-- 1xx = plot_id
-- 2xx = seed_id
-- 3xx = gift_id 

CREATE TABLE users(
    user_id INT NOT NULL AUTO_INCREMENT,
    username varchar(40) NOT NULL, 
    password varchar(40) NOT NULL,
    name varchar(40) NOT NULL,
    PRIMARY KEY (user_id)
);

INSERT INTO users (user_id, username, password, name)
VALUES 
(1, "admin", "admin", "admin"),
(2, "user1", "123456", "ahbeng"),
(3, "user2", "654321", "ahlian"),
(4, "user3", "applepear", "muthu"),
(5, "user4", "orange", "James LIM"),
(6, "user5", "durian", "John LEE"),
(7, "user6", "lychee", "Mark TAN")
;


drop table if exists friends;
create table friends(
    uid INT NOT NULL, 
    fid INT NOT NULL,
    PRIMARY KEY (uid, fid),
    FOREIGN KEY (uid) REFERENCES users(user_id),
    FOREIGN KEY (fid) REFERENCES users(user_id)
);

INSERT INTO friends (uid, fid)
VALUES 
(1,2), 
(1,3),
(2,1), 
(2,4), 
(3,1), 
(3,2), 
(4,2);

DROP TABLE IF EXISTS farmer;

CREATE TABLE farmer (
    user_id INT NOT NULL PRIMARY KEY,
    experience INT NOT NULL,
    gold INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

INSERT INTO farmer (user_id, experience, gold) 
VALUES 
(1, 2000, 400), 
(2, 5000, 888),
(3, 12500, 291);

DROP TABLE IF EXISTS plot;

CREATE TABLE plot (
    plot_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    plot_position INT(1) NOT NULL,
    crop_name varchar(40) NOT NULL,
    time_to_maturity float(20) NOT NULL,
    start_time BIGINT NOT NULL,
    max_yield INT NOT NULL,
    stolen_yield INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES farmer(user_id),
    UNIQUE KEY (user_id, plot_position)
);

INSERT INTO plot (plot_id, user_id, plot_position, crop_name, time_to_maturity, start_time, max_yield, stolen_yield) 
VALUES 

(101, 1, 1, "Papaya", 30, 1583306342467, 100, 10 ),
(103, 1, 2, "Pumpkin", 60, 1583306353234, 180, 0 ),
(106, 2, 2, "Sunflower", 120, 1583306361214, 20, 0),
(104, 3, 1, "Watermelon", 240, 1583306365280, 700, 100 );

DROP TABLE IF EXISTS seed;

CREATE TABLE seed (
    seed_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    seed_type VARCHAR(20) NOT NULL,
    amount INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    UNIQUE KEY (user_id, seed_type)
);

INSERT INTO seed (seed_id, user_id, seed_type, amount)
VALUES 
(201, 1, "Papaya", 20),
(202, 1, "Pumpkin", 50),
(203, 2, "Sunflower", 5),
(204, 3, "Watermelon", 105);

DROP TABLE IF EXISTS gift;

CREATE TABLE gift (
    gift_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_ID INT NOT NULL,
    friend_id INT NOT NULL,
    seed_type VARCHAR(20) NOT NULL,
    status INT NOT NULL,
    timestamp BIGINT NOT NULL, 
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

INSERT INTO gift (gift_id, user_id, friend_id, seed_type, status, timestamp)
VALUES 
(0, 1, 1, "null", 1, 1),
(301, 1, 2, "Papaya", 0, 1583306342467),
(302, 1, 3, "Pumpkin", 1, 1583306353234),
(303, 2, 1, "Sunflower", 0, 1583306361214),
(304, 3, 1, "Watermelon", 1, 1583306365280);

DROP TABLE IF EXISTS posts;

CREATE TABLE IF NOT EXISTS posts ( 
    post_id int NOT NULL PRIMARY KEY AUTO_INCREMENT, 
    poster int NOT NULL, 
    wall int NOT NULL, 
    msg varchar(500) NOT NULL, 
    likes int NOT NULL, 
    dislikes int NOT NULL,
    post_time BIGINT NOT NULL,
    gift_id INT NOT NULL,
    FOREIGN KEY (gift_id) REFERENCES gift(gift_id)
);

INSERT INTO posts VALUES 
    (1, 2, 1, 'This is a test', 6, 1, 1001, 0),
    (2, 3, 1, "Nice haircut", 6, 9, 1002, 0),
    (3, 4, 1, "Nice day right?", 9, 6, 1003, 0),
    (4, 4, 1, "This post should not be seen", 9, 6, 10, 0),
    (5, 2, 1, "Here is a bag of sunflower seeds for you. – City Farmers", 0, 0, 1004, 303),
    (6, 2, 2, "My resolution for 2020 is 1920 x 1080", 0, 0, 1001, 0),
    (7, 3, 2, "420 Blaze it", 0, 0, 1002, 0),
    (8, 2, 2, "This is my first post", 0, 0, 1000, 0)
;

DROP TABLE IF EXISTS requests;

CREATE TABLE IF NOT EXISTS requests ( 
    user_id int NOT NULL, 
    friend_id int NOT NULL, 
    PRIMARY KEY(user_id, friend_id) 
);

INSERT INTO requests VALUES 
    (1, 5),
    (1, 4)
;

DROP TABLE IF EXISTS tagged;

CREATE TABLE IF NOT EXISTS tagged ( 
    post_id int NOT NULL, 
    user_id int NOT NULL, 
    PRIMARY KEY (post_id, user_id) 
);

DROP TABLE IF EXISTS comments;

CREATE TABLE IF NOT EXISTS comments (
    post_id int NOT NULL,
    user_id int NOT NULL,
    msg varchar(500),
    comment_time BIGINT NOT NULL,
    PRIMARY KEY (post_id, user_id, msg),
    FOREIGN KEY (post_id) REFERENCES posts(post_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE 
);

INSERT INTO comments 
    VALUES
    (1, 3, "Can I be part of the test as well?", 1584070981823),
    (1, 4, "Can I too?", 1584070981824),
    (1, 1, "Who are you?????", 1584070981825),
    (2, 1, "Naise", 1584070981830),
    (3, 1, "chirs m8", 1584070981835),
    (4, 1, "1337 420 blaze it", 1584070981840)
    ;

CREATE TABLE IF NOT EXISTS likes (
    post_id int NOT NULL,
    user_id int NOT NULL,
    status int NOT NULL,
    PRIMARY KEY (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id) 
        ON DELETE CASCADE
        ON UPDATE CASCADE 
);

INSERT INTO likes 
    VALUES 
    (1, 1, 1),
    (1, 2, 0),
    (1, 3, 1),
    (1, 4, 1),
    (1, 5, 1),
    (1, 6, 0),
    (2, 2, 1),
    (2, 3, 1),
    (2, 4, 1),
    (2, 6, 0),
    (3, 4, 0),
    (3, 5, 0),
    (3, 6, 0),
    (3, 2, 1),
    (4, 2, 1)
    ;