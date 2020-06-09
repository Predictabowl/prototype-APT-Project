CREATE SCHEMA testschema
    AUTHORIZATION mario;

COMMENT ON SCHEMA testschema
    IS 'testing public schema';

GRANT ALL ON SCHEMA testschema TO PUBLIC;

GRANT ALL ON SCHEMA testschema TO mario;