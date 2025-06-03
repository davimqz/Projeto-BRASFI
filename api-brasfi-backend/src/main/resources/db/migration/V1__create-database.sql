CREATE TABLE Member (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255),
                        username VARCHAR(100) UNIQUE,
                        created_at DATETIME,
                        email VARCHAR(255) UNIQUE,
                        password_hash VARCHAR(255),
                        description TEXT
);

CREATE TABLE Chat (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      type VARCHAR(50),
                      last_activity DATETIME
);

CREATE TABLE Education (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           title VARCHAR(255),
                           institution VARCHAR(255),
                           type ENUM('ACADEMIC', 'PROFESSIONAL'),
                           start_year INT,
                           end_year INT
);

CREATE TABLE Member_Education (
                                  member_id INT,
                                  education_id INT,
                                  PRIMARY KEY (member_id, education_id),
                                  FOREIGN KEY (member_id) REFERENCES Member(id),
                                  FOREIGN KEY (education_id) REFERENCES Education(id)
);

CREATE TABLE Community (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255),
                           description TEXT,
                           chat_id INT,
                           creator_id INT,
                           FOREIGN KEY (chat_id) REFERENCES Chat(id),
                           FOREIGN KEY (creator_id) REFERENCES Member(id)
);

CREATE TABLE Community_Members (
                                   community_id INT,
                                   member_id INT,
                                   PRIMARY KEY (community_id, member_id),
                                   FOREIGN KEY (community_id) REFERENCES Community(id),
                                   FOREIGN KEY (member_id) REFERENCES Member(id)
);

CREATE TABLE Invitation (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            community_id INT,
                            invited_member_id INT,
                            invited_by INT,
                            sent_at DATETIME,
                            accepted BOOLEAN,
                            FOREIGN KEY (community_id) REFERENCES Community(id),
                            FOREIGN KEY (invited_member_id) REFERENCES Member(id),
                            FOREIGN KEY (invited_by) REFERENCES Member(id)
);

CREATE TABLE Post (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      content TEXT,
                      author_id INT,
                      created_at DATETIME,
                      media_file VARCHAR(255),
                      edited BOOLEAN,
                      edited_at DATETIME,
                      visible BOOLEAN,
                      FOREIGN KEY (author_id) REFERENCES Member(id)
);

CREATE TABLE Post_Likes (
                            post_id INT,
                            member_id INT,
                            PRIMARY KEY (post_id, member_id),
                            FOREIGN KEY (post_id) REFERENCES Post(id),
                            FOREIGN KEY (member_id) REFERENCES Member(id)
);

CREATE TABLE Comment (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         text_content TEXT,
                         type VARCHAR(50),
                         media_file VARCHAR(255),
                         author_id INT,
                         created_at DATETIME,
                         edited BOOLEAN,
                         edited_at DATETIME,
                         chat_id INT,
                         deleted BOOLEAN,
                         FOREIGN KEY (author_id) REFERENCES Member(id),
                         FOREIGN KEY (chat_id) REFERENCES Chat(id)
);


CREATE TABLE Chat_Participants (
                                   chat_id INT,
                                   member_id INT,
                                   PRIMARY KEY (chat_id, member_id),
                                   FOREIGN KEY (chat_id) REFERENCES Chat(id),
                                   FOREIGN KEY (member_id) REFERENCES Member(id)
);

CREATE TABLE Chat_Messages (
                               chat_id INT,
                               message_id INT,
                               PRIMARY KEY (chat_id, message_id),
                               FOREIGN KEY (chat_id) REFERENCES Chat(id),
                               FOREIGN KEY (message_id) REFERENCES Comment(id)
);

CREATE TABLE Member_Followers (
                                  member_id INT,
                                  follower_id INT,
                                  PRIMARY KEY (member_id, follower_id),
                                  FOREIGN KEY (member_id) REFERENCES Member(id),
                                  FOREIGN KEY (follower_id) REFERENCES Member(id)
);

CREATE TABLE Member_Blocks (
                               blocker_id INT,
                               blocked_id INT,
                               PRIMARY KEY (blocker_id, blocked_id),
                               FOREIGN KEY (blocker_id) REFERENCES Member(id),
                               FOREIGN KEY (blocked_id) REFERENCES Member(id)
);

CREATE TABLE Administrator (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               member_id INT UNIQUE,
                               FOREIGN KEY (member_id) REFERENCES Member(id)
);
