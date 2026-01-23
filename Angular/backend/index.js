const express = require('express');
const mysql = require('mysql2');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(bodyParser.json());

// Configurar la conexión a la base de datos MySQL
const db = mysql.createConnection({
    host: 'localhost', // Dirección del servidor MySQL
    user: 'root', // Usuario de MySQL
    port: '3306',
    password: '', // Contraseña de MySQL
    database: 'db_eduelorrieta', // Nombre de tu base de datos
});

db.connect((err) => {
    if (err) {
        console.error('Error conectando a la base de datos:', err);
        return;
    }
    console.log('Conexión exitosa a la base de datos');
});

// Endpoints CRUD

//Crud Ciclos
app.get('/ciclos', (req, res) => {
    const query = 'SELECT * FROM ciclos';
    db.query(query, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});


app.post('/ciclos', (req, res) => {
    const newItem = req.body;
    const query = 'INSERT INTO ciclos SET ?';
    db.query(query, newItem, (err, results) => {
        if (err) throw err;
        res.send({ id: results.insertId, ...newItem });
    });
});

app.put('/ciclos/:id', (req, res) => {
    const { id } = req.params;
    const updatedItem = req.body;
    const query = 'UPDATE ciclos SET ? WHERE id = ?';
    db.query(query, [updatedItem, id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.delete('/ciclos/:id', (req, res) => {
    const { id } = req.params;
    const query = 'DELETE FROM ciclos WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.get('/ciclos/:id', (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM ciclos WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

//CRUD horarios

app.get('/horarios', (req, res) => {
    const query = 'SELECT * FROM horarios';
    db.query(query, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});


app.post('/horarios', (req, res) => {
    const newItem = req.body;
    const query = 'INSERT INTO horarios SET ?';
    db.query(query, newItem, (err, results) => {
        if (err) throw err;
        res.send({ id: results.insertId, ...newItem });
    });
});

app.put('/horarios/:id', (req, res) => {
    const { id } = req.params;
    const updatedItem = req.body;
    const query = 'UPDATE horarios SET ? WHERE id = ?';
    db.query(query, [updatedItem, id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.delete('/horarios/:id', (req, res) => {
    const { id } = req.params;
    const query = 'DELETE FROM horarios WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.get('/horarios/:id', (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM horarios WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

//CRUD matriculaciones

app.get('/matriculaciones', (req, res) => {
    const query = 'SELECT * FROM matriculaciones';
    db.query(query, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});


app.post('/matriculaciones', (req, res) => {
    const newItem = req.body;
    const query = 'INSERT INTO matriculaciones SET ?';
    db.query(query, newItem, (err, results) => {
        if (err) throw err;
        res.send({ id: results.insertId, ...newItem });
    });
});

app.put('/matriculaciones/:id', (req, res) => {
    const { id } = req.params;
    const updatedItem = req.body;
    const query = 'UPDATE matriculaciones SET ? WHERE id = ?';
    db.query(query, [updatedItem, id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.delete('/matriculaciones/:id', (req, res) => {
    const { id } = req.params;
    const query = 'DELETE FROM matriculaciones WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.get('/matriculaciones/:id', (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM matriculaciones WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

//CRUD modulos 

app.get('/modulos', (req, res) => {
    const query = 'SELECT * FROM modulos';
    db.query(query, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});


app.post('/modulos', (req, res) => {
    const newItem = req.body;
    const query = 'INSERT INTO modulos SET ?';
    db.query(query, newItem, (err, results) => {
        if (err) throw err;
        res.send({ id: results.insertId, ...newItem });
    });
});

app.put('/modulos/:id', (req, res) => {
    const { id } = req.params;
    const updatedItem = req.body;
    const query = 'UPDATE modulos SET ? WHERE id = ?';
    db.query(query, [updatedItem, id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.delete('/modulos/:id', (req, res) => {
    const { id } = req.params;
    const query = 'DELETE FROM modulos WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.get('/modulos/:id', (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM modulos WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

//CRUD Reuniones

app.get('/reuniones', (req, res) => {
    const query = 'SELECT * FROM reuniones';
    db.query(query, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});


app.post('/reuniones', (req, res) => {
    const newItem = req.body;
    const query = 'INSERT INTO reuniones SET ?';
    db.query(query, newItem, (err, results) => {
        if (err) throw err;
        res.send({ id: results.insertId, ...newItem });
    });
});

app.put('/reuniones/:id', (req, res) => {
    const { id } = req.params;
    const updatedItem = req.body;
    const query = 'UPDATE reuniones SET ? WHERE id = ?';
    db.query(query, [updatedItem, id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.delete('/reuniones/:id', (req, res) => {
    const { id } = req.params;
    const query = 'DELETE FROM reuniones WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.get('/reuniones/:id', (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM reuniones WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

//CRUD tipos

app.get('/tipos', (req, res) => {
    const query = 'SELECT * FROM tipos';
    db.query(query, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.post('/tipos', (req, res) => {
    const newItem = req.body;
    const query = 'INSERT INTO tipos SET ?';
    db.query(query, newItem, (err, results) => {
        if (err) throw err;
        res.send({ id: results.insertId, ...newItem });
    });
});

app.put('/tipos/:id', (req, res) => {
    const { id } = req.params;
    const updatedItem = req.body;
    const query = 'UPDATE tipos SET ? WHERE id = ?';
    db.query(query, [updatedItem, id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.delete('/tipos/:id', (req, res) => {
    const { id } = req.params;
    const query = 'DELETE FROM tipos WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.get('/tipos/:id', (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM tipos WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

//CRUD Users

app.get('/users', (req, res) => {
    const query = 'SELECT * FROM users';
    db.query(query, (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.post('/users', (req, res) => {
    const newItem = req.body;
    const query = 'INSERT INTO users SET ?';
    db.query(query, newItem, (err, results) => {
        if (err) throw err;
        res.send({ id: results.insertId, ...newItem });
    });
});

app.put('/users/:id', (req, res) => {
    const { id } = req.params;
    const updatedItem = req.body;
    const query = 'UPDATE users SET ? WHERE id = ?';
    db.query(query, [updatedItem, id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.delete('/users/:id', (req, res) => {
    const { id } = req.params;
    const query = 'DELETE FROM users WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

app.get('/users/:id', (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM users WHERE id = ?';
    db.query(query, [id], (err, results) => {
        if (err) throw err;
        res.send(results);
    });
});

// Iniciar el servidor
const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
