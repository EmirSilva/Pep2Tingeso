import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import { useNavigate } from "react-router-dom";

const planes = [
  {
    vueltas: "10 vueltas o max 10 min",
    precio: "15.000",
    duracion: "30 min"
  },
  {
    vueltas: "15 vueltas o max 15 min",
    precio: "20.000",
    duracion: "35 min"
  },
  {
    vueltas: "20 vueltas o max 20 min",
    precio: "25.000",
    duracion: "40 min"
  }
];

export default function VerPlanes() {
  const navigate = useNavigate();

  const handleSeleccionar = (plan) => {
    navigate("/reservas");
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexWrap: "wrap",
        gap: 3,
        justifyContent: "center",
        p: 3
      }}
    >
      {planes.map((plan, index) => (
        <Card
          key={index}
          variant="outlined"
          sx={{ minWidth: 250, maxWidth: 300, borderRadius: 3, boxShadow: 3 }}
        >
          <CardContent>
            <Typography
              variant="h6"
              component="div"
              gutterBottom
              sx={{ color: "#1976d2" }}
            >
              Plan {index + 1}
            </Typography>
            <Typography variant="body1" sx={{ mb: 1 }}>
              <strong>Numero de vueltas o tiempo maximo permitido:</strong><br />{plan.vueltas}
            </Typography>
            <Typography variant="body1" sx={{ mb: 1 }}>
              <strong>Duracion total:</strong><br />{plan.duracion}
            </Typography>
            <Typography variant="body1" sx={{ mb: 1 }}>
              <strong>Precio:</strong><br />${plan.precio}
            </Typography>
          </CardContent>
          <CardActions>
            <Button
              size="small"
              variant="contained"
              fullWidth
              onClick={() => handleSeleccionar(plan)}
            >
              Reservar
            </Button>
          </CardActions>
        </Card>
      ))}
    </Box>
  );
}


