
CREATE TABLE IF NOT EXISTS totvs_authority (
    permission_name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS totvs_user (
                            id BIGSERIAL PRIMARY KEY,
                            email VARCHAR(250) NOT NULL UNIQUE,
                            password_hash VARCHAR(60) NOT NULL,
                            activated BOOLEAN NOT NULL DEFAULT false,
                            lang_key VARCHAR(10),
                            image_url VARCHAR(256),
                            created_by VARCHAR(50) NOT NULL DEFAULT 'totvs_developer',
                            created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                            last_modified_by VARCHAR(50),
                            last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                            person_id BIGINT
);

CREATE TABLE IF NOT EXISTS totvs_user_authority (
                             user_id BIGINT REFERENCES totvs_user(id),
                             authority_permission_name VARCHAR(50) REFERENCES totvs_authority(permission_name),
                             PRIMARY KEY(user_id, authority_permission_name)
);

CREATE TABLE IF NOT EXISTS totvs_person (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR NOT NULL,
                             tax_id VARCHAR NOT NULL,
                             person_type VARCHAR,
                             email VARCHAR(254) UNIQUE,
                             created_by VARCHAR(50) NOT NULL DEFAULT 'totvs_developer',
                             created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                             last_modified_by VARCHAR(50),
                             last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);


ALTER TABLE totvs_user ADD CONSTRAINT fk_totvs_user_person_id FOREIGN KEY (person_id) REFERENCES totvs_person(id);

CREATE TABLE IF NOT EXISTS totvs_phone (
                            id BIGSERIAL PRIMARY KEY,
                            number VARCHAR NOT NULL,
                            phone_type VARCHAR,
                            person_id BIGINT REFERENCES totvs_person(id),
                            created_by VARCHAR(50) NOT NULL DEFAULT 'totvs_developer',
                            created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                            last_modified_by VARCHAR(50),
                            last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS totvs_address (
                              id BIGSERIAL PRIMARY KEY,
                              street VARCHAR NOT NULL,
                              district VARCHAR NOT NULL,
                              city VARCHAR NOT NULL,
                              state VARCHAR NOT NULL,
                              country VARCHAR NOT NULL DEFAULT 'Brazil',
                              zip_code VARCHAR NOT NULL,
                              address_type VARCHAR,
                              latitude DECIMAL,
                              longitude DECIMAL,
                              person_id BIGINT NOT NULL REFERENCES totvs_person(id),
                              created_by VARCHAR(50) NOT NULL DEFAULT 'totvs_developer',
                              created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                              last_modified_by VARCHAR(50),
                              last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS totvs_client (
                                id BIGSERIAL PRIMARY KEY,
                                origin VARCHAR,
                                interest VARCHAR,
                                person_id BIGINT REFERENCES totvs_person(id),
                                created_by VARCHAR(50) NOT NULL DEFAULT 'totvs_developer',
                                created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                                last_modified_by VARCHAR(50),
                                last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

INSERT INTO totvs_authority(permission_name)
SELECT 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM totvs_authority WHERE permission_name = 'USER');

INSERT INTO totvs_authority(permission_name)
SELECT 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM totvs_authority WHERE permission_name = 'ADMIN');