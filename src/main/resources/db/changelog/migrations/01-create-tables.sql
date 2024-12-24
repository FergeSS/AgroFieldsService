-- liquibase formatted sql

-- changeset asavershin:createTables

CREATE TABLE IF NOT EXISTS crop
(
  crop_id   BIGSERIAL PRIMARY KEY,
  crop_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS field
(
  field_id              bigserial PRIMARY KEY,
  field_name            VARCHAR(100) NOT NULL,
  field_square_area     VARCHAR(40)  NOT NULL,
  field_geom            GEOMETRY     NOT NULL,
  field_description     VARCHAR(256),
  field_color           VARCHAR(6)   NOT NULL,
  field_activity_start  DATE,
  field_activity_end    DATE,
  field_organization_id INTEGER      NOT NULL
);

CREATE TABLE IF NOT EXISTS crop_rotation
(
  crop_rotation_id          BIGSERIAL PRIMARY KEY,
  field_id                  bigint NOT NULL,
  crop_id                   bigint NOT NULL,
  crop_rotation_start_date  DATE   NOT NULL,
  crop_rotation_end_date    DATE   NOT NULL,
  crop_rotation_description VARCHAR(256),
  FOREIGN KEY (field_id) REFERENCES field (field_id),
  FOREIGN KEY (crop_id) REFERENCES crop (crop_id)
);

CREATE TABLE IF NOT EXISTS soil
(
  soil_id                 bigserial PRIMARY KEY,
  field_id                bigint NOT NULL,
  soil_ph                 VARCHAR(10),
  soil_sample_date        DATE   NOT NULL,
  soil_organic_matter     VARCHAR(10),
  soil_mobile_p           VARCHAR(10),
  soil_mobile_k           VARCHAR(10),
  soil_mobile_s           VARCHAR(10),
  soil_nitrate_n          VARCHAR(10),
  soil_ammonium_n         VARCHAR(10),
  soil_hydrolytic_acidity VARCHAR(10),
  soil_ca_exchange        VARCHAR(10),
  soil_mg_exchange        VARCHAR(10),
  soil_b                  VARCHAR(10),
  soil_co                 VARCHAR(10),
  soil_mn                 VARCHAR(10),
  soil_zn                 VARCHAR(10),
  FOREIGN KEY (field_id) REFERENCES field (field_id)
);
