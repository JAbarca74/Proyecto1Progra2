--------------------------------------------------------
-- DATOS DE PRUEBA: TB_ROLES
--------------------------------------------------------
INSERT INTO UNA.TB_ROLES (ID, NAME) VALUES (ROLES_SEQ.NEXTVAL, 'ADMIN');
INSERT INTO UNA.TB_ROLES (ID, NAME) VALUES (ROLES_SEQ.NEXTVAL, 'USER');

--------------------------------------------------------
-- DATOS DE PRUEBA: TB_USUARIOS
--------------------------------------------------------
INSERT INTO UNA.TB_USUARIOS (
    ID, USERNAME, PASSWORD, ROLE_ID, IS_ACTIVE, VERSION, APELLIDO, CORREO_ELECTRONICO, NOMBRE
) VALUES (
    USUARIOS_SEQ.NEXTVAL,
    'admin',
    'admin',
    (SELECT ID FROM UNA.TB_ROLES WHERE NAME = 'ADMIN'),
    'A',
    1,
    'Ramirez',
    'admin@una.ac.cr',
    'Carlos'
);

INSERT INTO UNA.TB_USUARIOS (
    ID, USERNAME, PASSWORD, ROLE_ID, IS_ACTIVE, VERSION, APELLIDO, CORREO_ELECTRONICO, NOMBRE
) VALUES (
    USUARIOS_SEQ.NEXTVAL,
    'una',
    'una',
    (SELECT ID FROM UNA.TB_ROLES WHERE NAME = 'USER'),
    'A',
    1,
    'Sanchez',
    'user1@una.ac.cr',
    'Laura'
);

--------------------------------------------------------
-- DATOS DE PRUEBA: TB_SPACE_TYPES
--------------------------------------------------------
INSERT INTO UNA.TB_SPACE_TYPES (ID, TYPE_NAME) VALUES (SPACE_TYPES_SEQ.NEXTVAL, 'Oficina Privada');
INSERT INTO UNA.TB_SPACE_TYPES (ID, TYPE_NAME) VALUES (SPACE_TYPES_SEQ.NEXTVAL, 'Sala de Reuniones');
INSERT INTO UNA.TB_SPACE_TYPES (ID, TYPE_NAME) VALUES (SPACE_TYPES_SEQ.NEXTVAL, 'Área Común');

--------------------------------------------------------
-- DATOS DE PRUEBA: TB_SPACES
--------------------------------------------------------
INSERT INTO UNA.TB_SPACES (ID, NAME, CAPACITY) VALUES (SPACES_SEQ.NEXTVAL, 'Segundo Piso', 25);
INSERT INTO UNA.TB_SPACES (ID, NAME, CAPACITY) VALUES (SPACES_SEQ.NEXTVAL, 'Planta Baja', 40);

--------------------------------------------------------
-- DATOS DE PRUEBA: TB_COWORKING_SPACES
--------------------------------------------------------
INSERT INTO UNA.TB_COWORKING_SPACES (
    ID, SPACE_ID, TYPE_ID, NAME, CAPACITY
) VALUES (
    COWORKING_SPACES_SEQ.NEXTVAL,
    (SELECT ID FROM UNA.TB_SPACES WHERE NAME = 'Segundo Piso'),
    (SELECT ID FROM UNA.TB_SPACE_TYPES WHERE TYPE_NAME = 'Oficina Privada'),
    'Oficina 201',
    3
);

INSERT INTO UNA.TB_COWORKING_SPACES (
    ID, SPACE_ID, TYPE_ID, NAME, CAPACITY
) VALUES (
    COWORKING_SPACES_SEQ.NEXTVAL,
    (SELECT ID FROM UNA.TB_SPACES WHERE NAME = 'Planta Baja'),
    (SELECT ID FROM UNA.TB_SPACE_TYPES WHERE TYPE_NAME = 'Sala de Reuniones'),
    'Sala A',
    10
);

--------------------------------------------------------
-- DATOS DE PRUEBA: TB_RESERVATIONS
--------------------------------------------------------
INSERT INTO UNA.TB_RESERVATIONS (
    ID, USER_ID, COWORKING_SPACE_ID, START_TIME, END_TIME, IS_CANCELLED, VERSION
) VALUES (
    RESERVATIONS_SEQ.NEXTVAL,
    (SELECT ID FROM UNA.TB_USUARIOS WHERE USERNAME = 'admin'),
    (SELECT ID FROM UNA.TB_COWORKING_SPACES WHERE NAME = 'Oficina 201'),
    TO_TIMESTAMP('2025-06-01 08:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2025-06-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    'N',
    1
);

INSERT INTO UNA.TB_RESERVATIONS (
    ID, USER_ID, COWORKING_SPACE_ID, START_TIME, END_TIME, IS_CANCELLED, VERSION
) VALUES (
    RESERVATIONS_SEQ.NEXTVAL,
    (SELECT ID FROM UNA.TB_USUARIOS WHERE USERNAME = 'una'),
    (SELECT ID FROM UNA.TB_COWORKING_SPACES WHERE NAME = 'Sala A'),
    TO_TIMESTAMP('2025-06-02 13:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2025-06-02 15:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    'N',
    1
);

COMMIT;
