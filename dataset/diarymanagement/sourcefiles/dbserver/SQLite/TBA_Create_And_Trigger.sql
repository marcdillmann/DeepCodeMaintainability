CREATE TABLE users(
        USER TEXT PRIMARY KEY ON CONFLICT FAIL,
        DISPLAYNAME NOT NULL,
        DEFAULTDIARYID,
        PWDHASH NOT NULL
);

CREATE TABLE passwordSalts(
        USER TEXT CONSTRAINT FK_passwordSalts_USER REFERENCES users(USER),
        SALT NOT NULL   
);

CREATE TABLE userSessions(
        USER TEXT CONSTRAINT FK_userSessions_USER REFERENCES users(USER),
        SESSIONID 
);

CREATE TABLE diary(
        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        NAME NOT NULL,
        OWNER TEXT CONSTRAINT FK_diary_USER REFERENCES users(USER),
        REVISION NOT NULL
);

CREATE TABLE diaries(
        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        USER TEXT CONSTRAINT FK_diaries_USER REFERENCES users(USER),
        DIARYID INTEGER CONSTRAINT FK_diaries_DIARYID REFERENCES diary(ID),
        PERMISSIONS NOT NULL
);


CREATE TABLE entries(
        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
        DIARYID INTEGER CONSTRAINT FK_entries_DIARYID REFERENCES diary(ID),
        START NOT NULL,
        END NOT NULL,
        USER TEXT CONSTRAINT FK_entries_USER REFERENCES users(USER),
        LOCKED NOT NULL,
        TITLE NOT NULL,
        BODY,
        DELETED NOT NULL
);

-- Drop Trigger
DROP TRIGGER fki_passwordSalts_USER_users_USER;

-- Foreign Key Preventing insert
CREATE TRIGGER fki_passwordSalts_USER_users_USER
BEFORE INSERT ON [passwordSalts]
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'insert on table "passwordSalts" violates foreign key constraint "fki_passwordSalts_USER_users_USER"')
  WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fku_passwordSalts_USER_users_USER;

-- Foreign key preventing update
CREATE TRIGGER fku_passwordSalts_USER_users_USER
BEFORE UPDATE ON [passwordSalts] 
FOR EACH ROW BEGIN
    SELECT RAISE(ROLLBACK, 'update on table "passwordSalts" violates foreign key constraint "fku_passwordSalts_USER_users_USER"')
      WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fkd_passwordSalts_USER_users_USER;

-- Foreign key preventing delete
CREATE TRIGGER fkd_passwordSalts_USER_users_USER
BEFORE DELETE ON users
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'delete on table "users" violates foreign key constraint "fkd_passwordSalts_USER_users_USER"')
  WHERE (SELECT USER FROM passwordSalts WHERE USER = OLD.USER) IS NOT NULL;
END;

-- Drop Trigger
DROP TRIGGER fki_userSessions_USER_users_USER;

-- Foreign Key Preventing insert
CREATE TRIGGER fki_userSessions_USER_users_USER
BEFORE INSERT ON [userSessions]
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'insert on table "userSessions" violates foreign key constraint "fki_userSessions_USER_users_USER"')
  WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fku_userSessions_USER_users_USER;

-- Foreign key preventing update
CREATE TRIGGER fku_userSessions_USER_users_USER
BEFORE UPDATE ON [userSessions] 
FOR EACH ROW BEGIN
    SELECT RAISE(ROLLBACK, 'update on table "userSessions" violates foreign key constraint "fku_userSessions_USER_users_USER"')
      WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fkd_userSessions_USER_users_USER;

-- Foreign key preventing delete
CREATE TRIGGER fkd_userSessions_USER_users_USER
BEFORE DELETE ON users
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'delete on table "users" violates foreign key constraint "fkd_userSessions_USER_users_USER"')
  WHERE (SELECT USER FROM userSessions WHERE USER = OLD.USER) IS NOT NULL;
END;

-- Drop Trigger
DROP TRIGGER fki_diary_OWNER_users_USER;

-- Foreign Key Preventing insert
CREATE TRIGGER fki_diary_OWNER_users_USER
BEFORE INSERT ON [diary]
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'insert on table "diary" violates foreign key constraint "fki_diary_OWNER_users_USER"')
  WHERE NEW.OWNER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.OWNER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fku_diary_OWNER_users_USER;

-- Foreign key preventing update
CREATE TRIGGER fku_diary_OWNER_users_USER
BEFORE UPDATE ON [diary] 
FOR EACH ROW BEGIN
    SELECT RAISE(ROLLBACK, 'update on table "diary" violates foreign key constraint "fku_diary_OWNER_users_USER"')
      WHERE NEW.OWNER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.OWNER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fkd_diary_OWNER_users_USER;

-- Foreign key preventing delete
CREATE TRIGGER fkd_diary_OWNER_users_USER
BEFORE DELETE ON users
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'delete on table "users" violates foreign key constraint "fkd_diary_OWNER_users_USER"')
  WHERE (SELECT OWNER FROM diary WHERE OWNER = OLD.USER) IS NOT NULL;
END;

-- Drop Trigger
DROP TRIGGER fki_diaries_USER_users_USER;

-- Foreign Key Preventing insert
CREATE TRIGGER fki_diaries_USER_users_USER
BEFORE INSERT ON [diaries]
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'insert on table "diaries" violates foreign key constraint "fki_diaries_USER_users_USER"')
  WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fku_diaries_USER_users_USER;

-- Foreign key preventing update
CREATE TRIGGER fku_diaries_USER_users_USER
BEFORE UPDATE ON [diaries] 
FOR EACH ROW BEGIN
    SELECT RAISE(ROLLBACK, 'update on table "diaries" violates foreign key constraint "fku_diaries_USER_users_USER"')
      WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fkd_diaries_USER_users_USER;

-- Foreign key preventing delete
CREATE TRIGGER fkd_diaries_USER_users_USER
BEFORE DELETE ON users
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'delete on table "users" violates foreign key constraint "fkd_diaries_USER_users_USER"')
  WHERE (SELECT USER FROM diaries WHERE USER = OLD.USER) IS NOT NULL;
END;

-- Drop Trigger
DROP TRIGGER fki_diaries_DIARYID_diary_ID;

-- Foreign Key Preventing insert
CREATE TRIGGER fki_diaries_DIARYID_diary_ID
BEFORE INSERT ON [diaries]
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'insert on table "diaries" violates foreign key constraint "fki_diaries_DIARYID_diary_ID"')
  WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = NEW.DIARYID) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fku_diaries_DIARYID_diary_ID;

-- Foreign key preventing update
CREATE TRIGGER fku_diaries_DIARYID_diary_ID
BEFORE UPDATE ON [diaries] 
FOR EACH ROW BEGIN
    SELECT RAISE(ROLLBACK, 'update on table "diaries" violates foreign key constraint "fku_diaries_DIARYID_diary_ID"')
      WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = NEW.DIARYID) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fkd_diaries_DIARYID_diary_ID;

-- Foreign key preventing delete
CREATE TRIGGER fkd_diaries_DIARYID_diary_ID
BEFORE DELETE ON diary
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'delete on table "diary" violates foreign key constraint "fkd_diaries_DIARYID_diary_ID"')
  WHERE (SELECT DIARYID FROM diaries WHERE DIARYID = OLD.ID) IS NOT NULL;
END;

-- Drop Trigger
DROP TRIGGER fki_entries_DIARYID_diary_ID;

-- Foreign Key Preventing insert
CREATE TRIGGER fki_entries_DIARYID_diary_ID
BEFORE INSERT ON [entries]
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'insert on table "entries" violates foreign key constraint "fki_entries_DIARYID_diary_ID"')
  WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = NEW.DIARYID) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fku_entries_DIARYID_diary_ID;

-- Foreign key preventing update
CREATE TRIGGER fku_entries_DIARYID_diary_ID
BEFORE UPDATE ON [entries] 
FOR EACH ROW BEGIN
    SELECT RAISE(ROLLBACK, 'update on table "entries" violates foreign key constraint "fku_entries_DIARYID_diary_ID"')
      WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = NEW.DIARYID) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fkd_entries_DIARYID_diary_ID;

-- Foreign key preventing delete
CREATE TRIGGER fkd_entries_DIARYID_diary_ID
BEFORE DELETE ON diary
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'delete on table "diary" violates foreign key constraint "fkd_entries_DIARYID_diary_ID"')
  WHERE (SELECT DIARYID FROM entries WHERE DIARYID = OLD.ID) IS NOT NULL;
END;

-- Drop Trigger
DROP TRIGGER fki_entries_USER_users_USER;

-- Foreign Key Preventing insert
CREATE TRIGGER fki_entries_USER_users_USER
BEFORE INSERT ON [entries]
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'insert on table "entries" violates foreign key constraint "fki_entries_USER_users_USER"')
  WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fku_entries_USER_users_USER;

-- Foreign key preventing update
CREATE TRIGGER fku_entries_USER_users_USER
BEFORE UPDATE ON [entries] 
FOR EACH ROW BEGIN
    SELECT RAISE(ROLLBACK, 'update on table "entries" violates foreign key constraint "fku_entries_USER_users_USER"')
      WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;
END;

-- Drop Trigger
DROP TRIGGER fkd_entries_USER_users_USER;

-- Foreign key preventing delete
CREATE TRIGGER fkd_entries_USER_users_USER
BEFORE DELETE ON users
FOR EACH ROW BEGIN
  SELECT RAISE(ROLLBACK, 'delete on table "users" violates foreign key constraint "fkd_entries_USER_users_USER"')
  WHERE (SELECT USER FROM entries WHERE USER = OLD.USER) IS NOT NULL;
END;