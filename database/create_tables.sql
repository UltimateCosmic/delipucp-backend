-- Eliminar tablas si existen (orden correcto por claves foráneas)
DROP TABLE IF EXISTS Reservas;
DROP TABLE IF EXISTS Historial;
DROP TABLE IF EXISTS Favoritos;
DROP TABLE IF EXISTS Menus;
DROP TABLE IF EXISTS Usuarios;
DROP TABLE IF EXISTS Promociones;
DROP TABLE IF EXISTS Local_Tipo;
DROP TABLE IF EXISTS TiposLocales;
DROP TABLE IF EXISTS Locales;

-- Tabla de locales
CREATE TABLE Locales
(
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          VARCHAR(50)  NOT NULL,
    ubicacion       VARCHAR(100) NOT NULL,
    horario         VARCHAR(50)  NOT NULL,
    horarioDespacho VARCHAR(50),
    imagenUrl       VARCHAR(100) NOT NULL,
    abierto         BOOLEAN      NOT NULL,
    telefono        VARCHAR(15),
    metodosPago     VARCHAR(100) NOT NULL
);

-- Tabla de tipos de locales
CREATE TABLE TiposLocales
(
    id     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(30) NOT NULL
);

-- Relación muchos a muchos: Locales - TiposLocales
CREATE TABLE Local_Tipo
(
    localId INTEGER NOT NULL REFERENCES Locales (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    tipoId  INTEGER NOT NULL REFERENCES TiposLocales (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    PRIMARY KEY (localId, tipoId)
);

-- Tabla de promociones
CREATE TABLE Promociones
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo      VARCHAR(50)    NOT NULL,
    descripcion VARCHAR(200)   NOT NULL,
    precio      DECIMAL(10, 2) NOT NULL,
    colorHex    VARCHAR(7)     NOT NULL,
    localId     INTEGER        NOT NULL REFERENCES Locales (id) ON UPDATE RESTRICT ON DELETE RESTRICT
);

-- Tabla de usuarios
CREATE TABLE Usuarios
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre     VARCHAR(50) NOT NULL,
    correo     VARCHAR(50) NOT NULL UNIQUE,
    tipo       VARCHAR(20) NOT NULL,
    userName   VARCHAR(30) NOT NULL UNIQUE,
    contrasena VARCHAR(50) NOT NULL,
    fotoPerfil VARCHAR(100)
);

-- Tabla de menús diarios
CREATE TABLE Menus
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre    VARCHAR(50)    NOT NULL,
    tipo      VARCHAR(20)    NOT NULL,
    entrada   VARCHAR(100),
    principal VARCHAR(100),
    postre    VARCHAR(100),
    bebida    VARCHAR(100),
    precio    DECIMAL(10, 2) NOT NULL,
    imagenUrl VARCHAR(100),
    fecha     DATE           NOT NULL,
    localId   INTEGER        NOT NULL REFERENCES Locales (id) ON UPDATE RESTRICT ON DELETE RESTRICT
);

-- Tabla de favoritos
CREATE TABLE Favoritos
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    usuarioId INTEGER NOT NULL REFERENCES Usuarios (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    localId   INTEGER NOT NULL REFERENCES Locales (id) ON UPDATE RESTRICT ON DELETE RESTRICT
);

-- Tabla de historial
CREATE TABLE Historial
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    usuarioId INTEGER      NOT NULL REFERENCES Usuarios (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    localId   INTEGER      NOT NULL REFERENCES Locales (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    fecha     VARCHAR(20)  NOT NULL,
    detalle   VARCHAR(200) NOT NULL
);

-- Tabla de reservas (mejorada)
CREATE TABLE Reservas
(
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    codigoReserva VARCHAR(20) NOT NULL UNIQUE,
    localId       INTEGER     NOT NULL REFERENCES Locales (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    usuarioId     INTEGER     NOT NULL REFERENCES Usuarios (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    menuId        INTEGER     NOT NULL REFERENCES Menus (id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    fecha         VARCHAR(20) NOT NULL,
    hora          VARCHAR(20) NOT NULL,
    estado        VARCHAR(20) NOT NULL,
    mesa          VARCHAR(20) NOT NULL,
    metodoPago    VARCHAR(20) NOT NULL
);