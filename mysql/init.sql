CREATE DATABASE IF NOT EXISTS rand;
USE rand;

-- 테이블이 존재하지 않을 경우에만 생성
CREATE TABLE IF NOT EXISTS MEMBERS(
    usr_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password CHAR(60) NOT NULL,
    email VARCHAR(50) NOT NULL,
    nick_name VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    pwd_wrong INT DEFAULT 0 NOT NULL,
    state ENUM('ACTIVE', 'INACTIVE', 'LOCKED', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    profile_img VARCHAR(500) NULL,
    locale_lat DECIMAL(9, 6) NULL,
    locale_lon DECIMAL(9, 6) NULL,
    sex ENUM('MAN', 'FEMAIL') NOT NULL,
    birth DATE NOT NULL,
    UNIQUE KEY username_uk(username),
    UNIQUE KEY email_uk(email),
    UNIQUE KEY nick_name_uk(nick_name)
);


CREATE TABLE  IF NOT EXISTS CHAT_ROOM(
chat_room_id BIGINT auto_increment,
room_cr_date DATETIME DEFAULT CURRENT_TIMESTAMP,
room_state ENUM("ACTIVE","INACTIVE") NOT NULL DEFAULT "ACTIVE",
room_mem1 INT NOT NULL,
room_mem2 INT NOT NULL,
room_up_date DATETIME NULL,
exist_mem INT NULL,
CONSTRAINT PRIMARY KEY chat_room_pk(chat_room_id),
CONSTRAINT UNIQUE KEY chat_room_uk(room_mem1,room_mem2),
CONSTRAINT FOREIGN KEY chat_room_fk1(room_mem1) REFERENCES MEMBERS(usr_id),
CONSTRAINT FOREIGN KEY chat_room_fk2(room_mem2) REFERENCES MEMBERS(usr_id),
CONSTRAINT FOREIGN KEY chat_room_fk3(exist_mem) REFERENCES MEMBERS(usr_id)
)
