create database if not exists redygest;
use redygest;
create table stories (id mediumint not null AUTO_INCREMENT PRIMARY KEY, story_json longtext NOT NULL, time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP);
