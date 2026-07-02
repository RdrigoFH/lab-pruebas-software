const express = require('express');
const app = express();
app.use(express.json());

// "Base de datos" en memoria para el catálogo de videojuegos
let videojuegos = [];
let nextId = 1;

// ---------------------------------------------------------
// POST /videojuegos - Crear un nuevo videojuego en el catálogo
// ---------------------------------------------------------
app.post('/videojuegos', (req, res) => {
  const { titulo, genero, stock } = req.body;

  // Validación de robustez (Edge Cases):
  // - titulo es obligatorio y debe ser string no vacío
  // - stock es obligatorio y debe ser numérico
  if (!titulo || typeof titulo !== 'string' || titulo.trim() === '') {
    return res.status(400).json({
      error: 'Campo inválido',
      mensaje: 'El campo "titulo" es obligatorio y debe ser un texto no vacío.'
    });
  }

  if (stock === undefined || typeof stock !== 'number' || isNaN(stock)) {
    return res.status(400).json({
      error: 'Campo inválido',
      mensaje: 'El campo "stock" es obligatorio y debe ser un número.'
    });
  }

  // Edge case semántico: un stock negativo es un número "válido" en JS
  // pero no tiene sentido de negocio, así que también se rechaza.
  if (stock < 0) {
    return res.status(400).json({
      error: 'Campo inválido',
      mensaje: 'El campo "stock" no puede ser negativo.'
    });
  }

  const nuevoVideojuego = {
    id: nextId++,
    titulo,
    genero: genero || 'Sin clasificar',
    stock
  };

  videojuegos.push(nuevoVideojuego);
  res.status(201).json(nuevoVideojuego);
});

// ---------------------------------------------------------
// GET /videojuegos/:id - Leer un videojuego por su ID
// ---------------------------------------------------------
app.get('/videojuegos/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);

  // Edge case sintáctico: el ID de la URL no es un número (ej. /videojuegos/abc)
  if (isNaN(id)) {
    return res.status(400).json({
      error: 'ID inválido',
      mensaje: 'El id proporcionado en la URL debe ser numérico.'
    });
  }

  const juego = videojuegos.find(v => v.id === id);

  if (!juego) {
    return res.status(404).json({
      error: 'No encontrado',
      mensaje: `No existe un videojuego con id ${id}.`
    });
  }

  res.status(200).json(juego);
});

// ---------------------------------------------------------
// PATCH /videojuegos/:id/vender - Simula la venta de una unidad
// (Modificación de estado: resta stock)
// ---------------------------------------------------------
app.patch('/videojuegos/:id/vender', (req, res) => {
  const id = parseInt(req.params.id, 10);
  const juego = videojuegos.find(v => v.id === id);

  if (!juego) {
    return res.status(404).json({
      error: 'No encontrado',
      mensaje: `No existe un videojuego con id ${id}.`
    });
  }

  if (juego.stock <= 0) {
    return res.status(400).json({
      error: 'Sin stock',
      mensaje: 'No hay unidades disponibles para vender.'
    });
  }

  juego.stock -= 1;
  res.status(200).json(juego);
});

// ---------------------------------------------------------
// Utilidad SOLO para pruebas: reinicia la "base de datos" en
// memoria para que cada test empiece desde un estado limpio.
// ---------------------------------------------------------
function resetVideojuegos() {
  videojuegos = [];
  nextId = 1;
}

// Exportamos la app para que Supertest la use sin necesidad de abrir un puerto
module.exports = app;
module.exports.resetVideojuegos = resetVideojuegos;