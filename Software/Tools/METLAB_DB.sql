CREATE TABLE organisation
(
  id   BIGINT UNSIGNED AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(50) DEFAULT '''NULL''' NOT NULL,
  CONSTRAINT organisation_name_uindex
  UNIQUE (name)
)
  ENGINE = InnoDB;

CREATE TABLE subscriber
(
  id                          BIGINT UNSIGNED AUTO_INCREMENT
    PRIMARY KEY,
  email                       VARCHAR(50) DEFAULT ''''''     NOT NULL,
  firstName                   VARCHAR(50) DEFAULT 'NULL'     NULL,
  lastName                    VARCHAR(50) DEFAULT '''NULL''' NULL,
  password                    VARCHAR(50) DEFAULT 'NULL'     NULL,
  organisationId              BIGINT UNSIGNED                NOT NULL,
  isOrganisationAdministrator INT(1) UNSIGNED DEFAULT '0'    NOT NULL,
  verificationPending         INT(1)                         NOT NULL,
  CONSTRAINT subscriber_email_uindex
  UNIQUE (email),
  CONSTRAINT organisationId
  FOREIGN KEY (organisationId) REFERENCES organisation (id)
)
  ENGINE = InnoDB;

CREATE INDEX organisationId
  ON subscriber (organisationId);

  
CREATE TABLE systemadministrator
(
  id        BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  firstName VARCHAR(50) DEFAULT 'NULL' NULL,
  lastName  VARCHAR(50) DEFAULT 'NULL' NULL,
  password  VARCHAR(50) DEFAULT 'NULL' NULL,
  email     VARCHAR(50)                NOT NULL,
  CONSTRAINT systemadministrator_email_uindex
  UNIQUE (email)
)
  ENGINE = InnoDB;

