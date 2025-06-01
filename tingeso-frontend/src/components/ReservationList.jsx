import { useEffect, useState } from "react";
import reservationService from "../services/reservation.service";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import DeleteIcon from "@mui/icons-material/Delete";
import Typography from "@mui/material/Typography";
import Alert from "@mui/material/Alert";

const ReservationList = () => {
  const [reservations, setReservations] = useState([]);
  const [cancellationMessage, setCancellationMessage] = useState("");
  const [cancellationError, setCancellationError] = useState("");

  const init = () => {
    reservationService
      .getAll()
      .then((response) => {
        console.log("Mostrando listado de todas las reservas.", response.data);
        setReservations(response.data);
      })
      .catch((error) => {
        console.error("Error al obtener el listado de reservas.", error);
        setCancellationError("Error al cargar la lista de reservas.");
      });
  };

  useEffect(() => {
    init();
  }, []);

  const handleCancelar = (id) => {
    console.log("Cancelando reserva con ID:", id);
    const confirmarCancelacion = window.confirm(
      "¿Estas seguro que desea cancelar esta reserva?"
    );
    if (confirmarCancelacion) {
      setCancellationMessage("Cancelando reserva...");
      setCancellationError("");
      reservationService
        .cancel(id)
        .then((response) => {
          console.log("Reserva cancelada:", response.data);
          setCancellationMessage("Reserva cancelada exitosamente.");
          init();
          setTimeout(() => setCancellationMessage(""), 2000);
        })
        .catch((error) => {
          console.error("Error al cancelar la reserva:", error);
          setCancellationError("Error al cancelar la reserva.");
        });
    }
  };

  return (
    <TableContainer component={Paper}>
      <Typography variant="h4" component="div" sx={{ margin: "1rem" }}>
        Lista de Reservas
      </Typography>
      {cancellationMessage && <Alert severity="success" sx={{ margin: "1rem" }}>{cancellationMessage}</Alert>}
      {cancellationError && <Alert severity="error" sx={{ margin: "1rem" }}>{cancellationError}</Alert>}
      <Table sx={{ minWidth: 650 }} size="small" aria-label="Lista de Reservas">
        <TableHead>
          <TableRow>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              ID
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Fecha
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Hora
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Nro. Personas
            </TableCell>
            <TableCell align="left" sx={{ fontWeight: "bold" }}>
              Operaciones
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reservations.map((reservation) => (
            <TableRow
              key={reservation.id}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell align="left">{reservation.id}</TableCell>
              <TableCell align="left">{reservation.reservationDate}</TableCell>
              <TableCell align="left">{reservation.reservationTime}</TableCell>
              <TableCell align="left">{reservation.groupSize}</TableCell>
              <TableCell>
                <Button
                  variant="contained"
                  color="error"
                  size="small"
                  onClick={() => handleCancelar(reservation.id)}
                  style={{ marginLeft: "0.5rem" }}
                  startIcon={<DeleteIcon />}
                >
                  Cancelar
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default ReservationList;