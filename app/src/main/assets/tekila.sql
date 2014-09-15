create table grupos (
    _id integer primary key autoincrement,
    nombre text not null
);

create table usuarios (
    _id integer primary key autoincrement,
    nombre text not null
);

create table usuario_grupo (
    _id integer primary key autoincrement,
    id_usuario integer,
    id_grupo integer,
    foreign key(id_usuario) references usuarios(_id),
    foreign key(id_grupo) references grupos(_id)
);

create table compras (
    _id integer primary key autoincrement,
    id_grupo integer,
    nombre text not null,
    cantidad real not null,
    datetime integer,
    foreign key(id_grupo) references grupos(_id)
);

create table participaciones (
    _id integer primary key autoincrement,
    porcentaje real not null,
    id_usuario integer,
    id_compra integer,
    foreign key(id_usuario) references usuarios(_id),
    foreign key(id_compra) references compras(_id)
);

create table pagos (
    _id integer primary key autoincrement,
    cantidad real not null,
    id_usuario integer,
    id_compra integer,
    foreign key(id_usuario) references usuarios(_id),
    foreign key(id_compra) references compras(_id)
);