create table if not exists seasons
(
  id              uuid    primary key,
  name            text,
  start_date      date    not null,
  end_date        date    not null,
  organization_id uuid    not null,
  archived        boolean not null default false
);

create table if not exists fields
(
  id          uuid    primary key,
  name        text    not null,
  description text,
  season_id   uuid    not null,
  foreign key (season_id) references seasons (id),
  archived    boolean not null default false
);

create table if not exists contours
(
  id          uuid     primary key,
  name        text     not null,
  square_area text     not null,
  geom        geometry not null,
  color       text     not null,
  field_id    uuid     not null,
  foreign key (field_id) references fields (id),
  archived    boolean  not null default false
);

create table if not exists crop_rotations
(
  id               uuid    primary key,
  contour_id       uuid    not null,
  foreign key (contour_id) references contours (id),
  start_date       date    not null,
  end_date         date,
  culture          text    not null,
  cultivar         text    not null,
  description      text,
  archived         boolean not null default false
);

create table if not exists soil_compositions
(
  id uuid    primary key,
  contour_id          uuid    not null,
  foreign key (contour_id) references contours (id),
  ph                  text,
  sample_date         date    not null,
  organic_matter      text,
  mobile_p            text,
  mobile_k            text,
  mobile_s            text,
  nitrate_n           text,
  ammonium_n          text,
  hydrolytic_acidity  text,
  ca_exchange         text,
  mg_exchange         text,
  b                   text,
  co                  text,
  mn                  text,
  zn                  text,
  archived            boolean not null default false,
  coordinates GEOMETRY(Point, 4321) not null
);

commit;