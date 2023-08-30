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