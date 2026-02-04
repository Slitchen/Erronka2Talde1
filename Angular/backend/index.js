const express = require('express');
const mysql = require('mysql2');
//const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
app.use(cors());
//app.use(bodyParser.json());
app.use(express.json());

// Configurar la conexión a la base de datos MySQL

const db = mysql.createConnection({
    host: '10.5.104.131', // Dirección del servidor MySQL //localhost
    user: 'dataExchanger', // Usuario de MySQL // root
    port: '3306', //depende el puerto del servidor
    password: 'cuack12345', // Contraseña de MySQL // vacio ('')
    database: 'db_eduelorrieta', // Nombre de tu base de datos
});

/*
const db = mysql.createConnection({
    host: 'localhost', // Dirección del servidor MySQL //localhost
    user: 'root', // Usuario de MySQL // root
    port: '3306', //depende el puerto del servidor
    password: '', // Contraseña de MySQL // vacio ('')
    database: 'db_eduelorrieta', // Nombre de tu base de datos
});
*/

db.connect((err) => {
    if (err) {
        console.error('Error conectando a la base de datos:', err);
        return;
    }
    console.log('Conexión exitosa a la base de datos');
});

// LOGIN
app.post('/login', (req, res) => {
    const { username, password } = req.body;

    const query = `
        SELECT id, email, username, nombre, apellidos, tipo_id
        FROM users
        WHERE username = ? AND password = ?
        LIMIT 1
    `;

    db.query(query, [username, password], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }

        if (results.length === 0) {
            return res.status(401).json({ message: 'Usuario o contraseña incorrectos' });
        }

        res.json(results[0]);
    });
});

// COUNT DE PROFESORES
app.get('/profesores/count', (req, res) => {
    const query = `
        SELECT COUNT(*) AS total
        FROM users
        WHERE tipo_id = 3
    `;

    db.query(query, (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }

        res.json(results[0]);
    });
});

// COUNT DE ALUMNOS
app.get('/alumnos/count', (req, res) => {
    const query = `
        SELECT COUNT(*) AS total
        FROM users
        WHERE tipo_id = 4
    `;

    db.query(query, (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }

        res.json(results[0]); // { total: X }
    });
});

// COUNT DE REUNIONES PARA HOY
app.get('/reuniones/count/today', (req, res) => {
    const query = `
        SELECT COUNT(*) AS total
        FROM reuniones
        WHERE fecha = CURDATE()
    `;

    db.query(query, (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }

        res.json(results[0]); // { total: X }
    });
});

// DATOS DEL USUARIO LOGEADO
app.get('/users/:id', (req, res) => {
    const { id } = req.params;

    const query = `
        SELECT id, email, username, password, nombre, apellidos, dni, direccion, telefono1, telefono2, tipo_id, argazkia_url
        FROM users
        WHERE id = ?
        LIMIT 1
    `;

    db.query(query, [id], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }

        if (results.length === 0) {
            return res.status(404).json({ message: 'Profesor no encontrado' });
        }

        res.json(results[0]);
    });
});

// LISTADO DE PROFESORES
app.get('/profesores', (req, res) => {
    const query = `
        SELECT id, email, username, password, nombre, apellidos, dni, direccion, telefono1, telefono2, tipo_id, argazkia_url
        FROM users
        WHERE tipo_id = 3
    `;
    db.query(query, (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json(results);
    });
});

// LISTADO DE ALUMNOS
app.get('/alumnos', (req, res) => {
    const query = `
        SELECT id, email, username, password, nombre, apellidos, dni, direccion, telefono1, telefono2, tipo_id, argazkia_url
        FROM users
        WHERE tipo_id = 4
    `;
    db.query(query, (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json(results);
    });
});

// LISTADO DE ADMINISTRADORES
app.get('/administradores', (req, res) => {
    const query = `
        SELECT id, email, username, password, nombre, apellidos, dni, direccion, telefono1, telefono2, tipo_id, argazkia_url
        FROM users
        WHERE tipo_id = 2
    `;
    db.query(query, (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json(results);
    });
});

// EDITAR DATOS DE UN PROFESOR / ALUMNO
app.put('/users/:id', (req, res) => {
    const { id } = req.params;
    const updatedData = req.body;
    const query = `
        UPDATE users
        SET ?
        WHERE id = ?
    `;

    db.query(query, [updatedData, id], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json({ message: 'Datos del usuario actualizados correctamente' });
    });
});

// CREAR UN PROFESOR / ALUMNO
app.post('/users', (req, res) => {
    const newUser = req.body;
    console.log('Datos recibidos:', newUser); // VERIFICAR qué llega
    const query = 'INSERT INTO users SET ?';
    db.query(query, newUser, (err, results) => {
        if (err) {
            console.error('Error al crear usuario:', err);
            return res.status(500).json({ message: 'Error del servidor', error: err });
        }
        res.json({ message: 'Usuario creado correctamente', id: results.insertId });
    });
});

// BORRAR UN PROFESRO / ALUMNO
app.delete('/users/:id', (req, res) => {
    const { id } = req.params;

    const query = `
        DELETE FROM users
        WHERE id = ?
    `;

    db.query(query, [id], (err, results) => {
        if (err) {
            console.error('Error al borrar usuario:', err);
            return res.status(500).json({ message: 'Error del servidor' });
        }

        if (results.affectedRows === 0) {
            return res.status(404).json({ message: 'Usuario no encontrado' });
        }

        res.json({ message: 'Usuario eliminado correctamente' });
    });
});

// REUNIONES DE UN ALUMNO
app.get('/alumnos/:id/reuniones', (req, res) => {
    const { id } = req.params;
    const query = `
        SELECT *
        FROM reuniones
        WHERE alumno_id = ?
    `;
    db.query(query, [id], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json(results);
    });
});

// REUNIONES DE UN PROFESOR
app.get('/profesores/:id/reuniones', (req, res) => {
    const { id } = req.params;
    const query = `
        SELECT *
        FROM reuniones
        WHERE profesor_id = ?
    `;
    db.query(query, [id], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json(results);
    });
});

// REUNIONES CON EL NOMBRE DEL PROFESOR Y EL ALUMNO
app.get('/reuniones', (req, res) => {
    const query = `
    SELECT 
        r.*, 
        p.nombre AS profesor_nombre, 
        p.apellidos AS profesor_apellidos,
        a.nombre AS alumno_nombre, 
        a.apellidos AS alumno_apellidos
    FROM reuniones r
    JOIN users p ON r.profesor_id = p.id
    JOIN users a ON r.alumno_id = a.id 
    `;
        db.query(query, (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json(results);
    });
});

// HORARIOS DE UN ALUMNO
// DE USERS A MATRICULACIONES A CICLOS A MODULOS A HORARIOS
// SACAR NOMBRE DE PROFE_ID DE HORARIOS
// SACAR NOMBRE DE MODULO_ID DE HORARIOS
app.get('/alumnos/:id/horarios', (req, res) => {
    const { id } = req.params;
    const query = `
        SELECT h.*, m.nombre AS modulo_nombre, u.nombre AS profesor_nombre, u.apellidos AS profesor_apellidos
        FROM horarios h
        JOIN modulos m ON h.modulo_id = m.id
        JOIN users u ON h.profe_id = u.id
        JOIN ciclos c ON m.ciclo_id = c.id
        JOIN matriculaciones mt ON c.id = mt.ciclo_id
        WHERE mt.alum_id = ?
        ORDER BY FIELD(h.dia, 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'), h.hora
    `;
    db.query(query, [id], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json(results);
    });
});

// HORARIO DE UN PROFESOR
app.get('/profesores/:id/horarios', (req, res) => {
    const { id } = req.params;
    const query = `
        SELECT h.*, m.nombre AS modulo_nombre
        FROM horarios h
        JOIN modulos m ON h.modulo_id = m.id
        WHERE h.profe_id = ?
        ORDER BY FIELD(h.dia, 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'), h.hora
    `;
    db.query(query, [id], (err, results) => {
        if (err) {
            console.error(err);
            return res.status(500).json({ message: 'Error del servidor' });
        }
        res.json(results);
    });
});

// CREAR REUNION
app.post('/reuniones', (req, res) => {
    const newReunion = req.body;
    const query = 'INSERT INTO reuniones SET ?';
    db.query(query, newReunion, (err, results) => {
        if (err) {
            console.error('Error al crear reunión:', err);
            return res.status(500).json({ message: 'Error del servidor', error: err });
        }
        res.json({ message: 'Reunión creada correctamente', id: results.insertId });
    });
});

// Iniciar el servidor
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
