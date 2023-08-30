-- checks users constraint of USER Conflits
INSERT INTO users VALUES ("1","1","1","1");
INSERT INTO users VALUES ("1","1","1","1");


-- checks passwordSalts constraint of USER Conflits
INSERT INTO passwordSalts VALUES ("1","1");
INSERT INTO passwordSalts VALUES ("2","2");

-- checks userSession constraint of USER Conflits
INSERT INTO userSessions VALUES ("1","1");
INSERT INTO userSessions VALUES ("2","2");
