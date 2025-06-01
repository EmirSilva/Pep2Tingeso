import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div style={{ padding: '20px' }}>
      <h1>Karting Xtreme</h1>
      <p>
        Karting Xtreme es una plataforma web donde puedes realizar reservas para disfrutar de una experiencia única de karting.
        ¡Siente la emoción de la velocidad y haz tu reserva ahora! Sin necesidad de crear una cuenta, solo ingresa tus datos y listo.
      </p>
      <p>
        Para realizar tu reserva, simplemente haz clic en el siguiente botón y completa tus datos:
      </p>
      <Link to="/planes">
        <button style={{ marginRight: '10px' }}>Ver planes</button>
      </Link>
      <Link to="/reservas">
        <button>Haz tu reserva ahora</button>
      </Link>

      <div style={{ marginTop: '30px', backgroundColor: '#f4f4f4', padding: '10px', borderRadius: '8px' }}>
        <h2>Horario de Atención</h2>
        <ul>
          <li><strong>Lunes a Viernes:</strong> 14:00 a 22:00 horas</li>
          <li><strong>Sábados, Domingos y Feriados:</strong> 10:00 a 22:00 horas</li>
        </ul>
      </div>
    </div>
  );
};

export default Home;


