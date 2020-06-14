CREATE SCHEMA testschema
    AUTHORIZATION testUser;

GRANT ALL ON SCHEMA testschema TO PUBLIC;

GRANT ALL ON SCHEMA testschema TO testUser;
