CREATE DATABASE IF NOT EXISTS rand;
USE rand;

-- 테이블이 존재하지 않을 경우에만 생성
CREATE TABLE IF NOT EXISTS MEMBERS (
    usr_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password CHAR(60) NOT NULL,
    email VARCHAR(50) NOT NULL,
    nick_name VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    pwd_wrong INT DEFAULT 0 NOT NULL,
    state ENUM('ACTIVE', 'INACTIVE', 'LOCKED', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    profile_img VARCHAR(500) NULL,
    sex ENUM('MAN', 'FEMAIL') NOT NULL,
    birth DATE NOT NULL,
    cr_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    up_date TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY username_uk(username),
    UNIQUE KEY email_uk(email),
    UNIQUE KEY nick_name_uk(nick_name)
);

CREATE TABLE IF NOT EXISTS CHAT_ROOM (
chat_room_id BIGINT auto_increment,
room_cr_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
room_state ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
room_up_date TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP ,
CONSTRAINT PRIMARY KEY chat_room_id_pk(chat_room_id)
);

CREATE TABLE IF NOT EXISTS CHAT_ROOM_MEMBERS (
usr_id INT NOT NULL,
chat_room_id BIGINT NOT NULL,
cr_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT PRIMARY KEY chat_room_mem_pk(usr_id,chat_room_id),
CONSTRAINT FOREIGN KEY usr_id_chat_room_mem_fk(usr_id) REFERENCES MEMBERS(usr_id) ON DELETE CASCADE ,
CONSTRAINT FOREIGN KEY chat_room_id_chat_room_mem_fk(chat_room_id) REFERENCES CHAT_ROOM(chat_room_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CHAT_ROOM_IMG(
img_list_id BIGINT NOT NULL auto_increment,
chat_room_id BIGINT NOT NULL,
usr_id INT NOT NULL,
img_url VARCHAR(200) NOT NULL,
cr_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT PRIMARY KEY img_list_pk(img_list_id),
CONSTRAINT FOREIGN KEY img_room_id_fk(chat_room_id) REFERENCES CHAT_ROOM(chat_room_id) ON DELETE CASCADE,
CONSTRAINT FOREIGN KEY img_usr_id_fk(usr_id) REFERENCES MEMBERS(usr_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CHAT_MESSAGE (
chat_id BIGINT NOT NULL auto_increment,
usr_id INT NOT NULL ,
chat_room_id BIGINT NOT NULL,
message VARCHAR(500) NOT NULL ,
msg_cr_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
msg_cr_date_ms TIMESTAMP(6) NOT NULL,
is_read BOOLEAN DEFAULT false,
chat_type ENUM('TEXT','IMG','VIDEO','LINK'),
CONSTRAINT PRIMARY KEY chat_msg_pk(chat_id),
CONSTRAINT FOREIGN KEY chat_msg_id_fk(usr_id) REFERENCES MEMBERS(usr_id) ON DELETE CASCADE ,
CONSTRAINT FOREIGN KEY chat_msg_room_id_fk(chat_room_id) REFERENCES CHAT_ROOM(chat_room_id) ON DELETE CASCADE
);

-- 인덱스 생성
CREATE INDEX idx_chat_message_room_date ON CHAT_MESSAGE (chat_room_id, msg_cr_date DESC);
CREATE INDEX idx_chat_message_unread ON CHAT_MESSAGE (usr_id, is_read);
CREATE INDEX idx_chat_room_members ON CHAT_ROOM_MEMBERS (chat_room_id, usr_id);