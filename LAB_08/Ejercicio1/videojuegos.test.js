const request = require('supertest');
const app = require('./app');

describe('Pruebas de Integración - Catálogo de Videojuegos', () => {

  // Aislamiento de estado: cada test arranca con la "base de datos" vacía,
  // para que ningún test dependa de datos dejados por otro.
  beforeEach(() => {
    app.resetVideojuegos();
  });

  // ---------------------------------------------------------------
  // 1. FLUJO DE PERSISTENCIA CRUZADA (POST -> GET usando el ID dinámico)
  // ---------------------------------------------------------------
  describe('Flujo de Persistencia Cruzada', () => {
    it('Debería crear un videojuego (POST) y luego leerlo (GET) usando el ID generado', async () => {
      const respuestaPost = await request(app)
        .post('/videojuegos')
        .send({ titulo: 'The Last Warrior', genero: 'Acción', stock: 10 });

      expect(respuestaPost.statusCode).toBe(201);
      expect(respuestaPost.headers['content-type']).toMatch(/json/);
      expect(respuestaPost.body).toHaveProperty('id');
      const idGenerado = respuestaPost.body.id;

      const respuestaGet = await request(app).get(`/videojuegos/${idGenerado}`);

      expect(respuestaGet.statusCode).toBe(200);
      expect(respuestaGet.headers['content-type']).toMatch(/json/);
      expect(respuestaGet.body.id).toBe(idGenerado);
      expect(respuestaGet.body.titulo).toBe('The Last Warrior');
      expect(respuestaGet.body.stock).toBe(10);
    });

    it('Debería devolver 404 al intentar leer un videojuego que no existe', async () => {
      const respuesta = await request(app).get('/videojuegos/9999');

      expect(respuesta.statusCode).toBe(404);
      expect(respuesta.body).toHaveProperty('mensaje');
    });

    it('Debería devolver 400 si el ID de la URL no es numérico', async () => {
      const respuesta = await request(app).get('/videojuegos/abc');

      expect(respuesta.statusCode).toBe(400);
      expect(respuesta.body).toHaveProperty('mensaje');
    });
  });

  // ---------------------------------------------------------------
  // 2. SIMULACIÓN DE MODIFICACIÓN DE ESTADO (venta -> resta stock -> GET confirma)
  // ---------------------------------------------------------------
  describe('Simulación de Modificación de Estado', () => {
    it('Debería restar el stock al vender una unidad y confirmar el cambio con un GET posterior', async () => {
      const post = await request(app)
        .post('/videojuegos')
        .send({ titulo: 'Racing Storm', genero: 'Carreras', stock: 5 });

      const id = post.body.id;

      const respuestaVenta = await request(app).patch(`/videojuegos/${id}/vender`);

      expect(respuestaVenta.statusCode).toBe(200);
      expect(respuestaVenta.body.stock).toBe(4);

      const respuestaGet = await request(app).get(`/videojuegos/${id}`);

      expect(respuestaGet.statusCode).toBe(200);
      expect(respuestaGet.body.stock).toBe(4);
    });

    it('Debería devolver 400 al intentar vender una unidad cuando el stock ya está en 0', async () => {
      const post = await request(app)
        .post('/videojuegos')
        .send({ titulo: 'Edición Limitada', genero: 'Estrategia', stock: 1 });

      const id = post.body.id;

      // Primera venta: agota el stock (1 -> 0)
      const primeraVenta = await request(app).patch(`/videojuegos/${id}/vender`);
      expect(primeraVenta.statusCode).toBe(200);
      expect(primeraVenta.body.stock).toBe(0);

      // Segunda venta: ya no hay stock disponible
      const segundaVenta = await request(app).patch(`/videojuegos/${id}/vender`);
      expect(segundaVenta.statusCode).toBe(400);
      expect(segundaVenta.body).toHaveProperty('mensaje');

      // Confirmamos que el stock se quedó en 0 y no bajó a negativo
      const respuestaGet = await request(app).get(`/videojuegos/${id}`);
      expect(respuestaGet.body.stock).toBe(0);
    });

    it('Debería devolver 404 al intentar vender un videojuego que no existe', async () => {
      const respuesta = await request(app).patch('/videojuegos/9999/vender');

      expect(respuesta.statusCode).toBe(404);
      expect(respuesta.body).toHaveProperty('mensaje');
    });
  });

  // ---------------------------------------------------------------
  // 3. VALIDACIÓN DE ROBUSTEZ (Edge Cases)
  // ---------------------------------------------------------------
  describe('Validación de Robustez (Edge Cases)', () => {
    it('Debería devolver 400 si el campo "stock" recibe un texto en lugar de un número', async () => {
      const respuesta = await request(app)
        .post('/videojuegos')
        .send({ titulo: 'Puzzle Master', genero: 'Puzzle', stock: 'diez' });

      expect(respuesta.statusCode).toBe(400);
      expect(respuesta.body).toHaveProperty('mensaje');
      expect(respuesta.body.mensaje).toMatch(/número/i);
    });

    it('Debería devolver 400 si falta el campo obligatorio "titulo"', async () => {
      const respuesta = await request(app)
        .post('/videojuegos')
        .send({ genero: 'Aventura', stock: 3 });

      expect(respuesta.statusCode).toBe(400);
      expect(respuesta.body).toHaveProperty('mensaje');
      expect(respuesta.body.mensaje).toMatch(/titulo/i);
    });

    it('Debería devolver 400 si el campo "titulo" viene vacío', async () => {
      const respuesta = await request(app)
        .post('/videojuegos')
        .send({ titulo: '   ', stock: 2 });

      expect(respuesta.statusCode).toBe(400);
    });

    it('Debería devolver 400 si el campo "stock" es un número negativo', async () => {
      const respuesta = await request(app)
        .post('/videojuegos')
        .send({ titulo: 'Chess Pro', genero: 'Mesa', stock: -5 });

      expect(respuesta.statusCode).toBe(400);
      expect(respuesta.body).toHaveProperty('mensaje');
      expect(respuesta.body.mensaje).toMatch(/negativo/i);
    });
  });
});