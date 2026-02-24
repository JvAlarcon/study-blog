CREATE TABLE datomic_kvs (
  id TEXT NOT NULL,
  rev INTEGER,
  map TEXT,
  val BYTEA,
  CONSTRAINT pk_id PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE datomic_kvs OWNER TO blog;
GRANT ALL ON TABLE datomic_kvs TO blog;
