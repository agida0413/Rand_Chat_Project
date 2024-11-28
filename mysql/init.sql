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
