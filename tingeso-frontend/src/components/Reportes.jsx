import { useEffect, useState } from "react";
import reservationService from "../services/reservation.service";
import { styled } from '@mui/material/styles';
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import Alert from "@mui/material/Alert";

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    color: theme.palette.common.black,
    fontWeight: 'bold',
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover,
  },
  '&:last-child td, &:last-child th': {
    borderBottom: 0,
  },
}));

const Reportes = () => {
  const [reportesMensuales, setReportesMensuales] = useState([]);
  const [reportesPersonas, setReportesPersonas] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const obtenerReportes = async () => {
      setLoading(true);
      try {
        //obtener reporte por tipo de tarifa
        const responseTipoTarifa = await reservationService.getReporteIngresosPorMesTipoTarifa({
          yearInicio: 2025,
          monthInicio: 1,
          yearFin: 2025,
          monthFin: 12,
        });
        console.log("Respuesta del backend (tipo tarifa):", responseTipoTarifa.data);
        setReportesMensuales(responseTipoTarifa.data);

        //obtener reporte por número de personas
        const responsePersonas = await reservationService.getReporteIngresosPorMesNumeroPersonas({
          yearInicio: 2025,
          monthInicio: 1,
          yearFin: 2025,
          monthFin: 12,
        });
        console.log("Respuesta del backend (personas):", responsePersonas.data);
        setReportesPersonas(responsePersonas.data);

        setLoading(false);
      } catch (error) {
        console.error("Error al obtener los reportes:", error);
        setError("Error al cargar los reportes.");
        setLoading(false);
      }
    };

    obtenerReportes();
  }, []);

  if (loading) {
    return <Typography variant="h6">Cargando Reportes...</Typography>;
  }

  if (error) {
    return <Alert severity="error">{error}</Alert>;
  }

  const meses = ["ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE", "TOTAL"];
  const colSpanValue = meses.length + 1;

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", padding: "20px" }}>
      {/*tabla de reporte por tipo de tarifa*/}
      <TableContainer component={Paper} sx={{ width: "98%", overflowX: "auto", marginBottom: 4 }}>
        <Table sx={{ minWidth: 700 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <StyledTableCell colSpan={colSpanValue} align="center">
                <Typography variant="h6" style={{ fontWeight: 'bold' }}>
                  Reporte de ingresos por numero de vueltas o tiempo maximo
                </Typography>
              </StyledTableCell>
            </TableRow>
            <TableRow>
              <StyledTableCell>Numero de vueltas o tiempo maximo permitido</StyledTableCell>
              {meses.map((mes) => (
                <StyledTableCell key={mes} align="right">{mes}</StyledTableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {reportesMensuales.map((reporte) => (
              <StyledTableRow key={reporte.tipoTarifa}>
                <StyledTableCell component="th" scope="row">
                  {reporte.tipoTarifa}
                </StyledTableCell>
                {meses.map((mes) => (
                  <StyledTableCell key={`${reporte.tipoTarifa}-${mes}`} align="right">
                    {reporte[mes]
                      ? reporte[mes].toLocaleString("es-CL", {
                          style: "currency",
                          currency: "CLP",
                        })
                      : reporte.TOTAL && mes === "TOTAL"
                      ? reporte.TOTAL.toLocaleString("es-CL", {
                          style: "currency",
                          currency: "CLP",
                        })
                      : "$0"}
                  </StyledTableCell>
                ))}
              </StyledTableRow>
            ))}
            <StyledTableRow>
              <StyledTableCell style={{ fontWeight: 'bold' }}>TOTAL</StyledTableCell>
              {meses.slice(0, -1).map((mes) => (
                <StyledTableCell key={`total-tipo-tarifa-${mes}`} align="right" style={{ fontWeight: 'bold' }}>
                  {reportesMensuales.reduce((sum, reporte) => sum + (reporte[mes] || 0), 0).toLocaleString("es-CL", {
                    style: "currency",
                    currency: "CLP",
                  })}
                </StyledTableCell>
              ))}
              <StyledTableCell align="right" style={{ fontWeight: 'bold' }}>
                {reportesMensuales.reduce((sum, reporte) => sum + (reporte.TOTAL || 0), 0).toLocaleString("es-CL", {
                  style: "currency",
                  currency: "CLP",
                })}
              </StyledTableCell>
            </StyledTableRow>
          </TableBody>
        </Table>
      </TableContainer>

      {/*tabla de reporte por numero de personas */}
      <TableContainer component={Paper} sx={{ width: "98%", overflowX: "auto" }}>
        <Table sx={{ minWidth: 700 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <StyledTableCell colSpan={colSpanValue} align="center">
                <Typography variant="h6" style={{ fontWeight: 'bold' }}>
                  Reporte de ingresos por numero de personas
                </Typography>
              </StyledTableCell>
            </TableRow>
            <TableRow>
              <StyledTableCell>Numero de personas</StyledTableCell>
              {meses.map((mes) => (
                <StyledTableCell key={`personas-${mes}`} align="right">{mes}</StyledTableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {reportesPersonas.map((reporte) => (
              <StyledTableRow key={reporte.rangoPersonas}>
                <StyledTableCell component="th" scope="row">
                  {reporte.rangoPersonas}
                </StyledTableCell>
                {meses.map((mes) => (
                  <StyledTableCell key={`${reporte.rangoPersonas}-${mes}`} align="right">
                    {reporte[mes]
                      ? reporte[mes].toLocaleString("es-CL", {
                          style: "currency",
                          currency: "CLP",
                        })
                      : reporte.TOTAL && mes === "TOTAL"
                      ? reporte.TOTAL.toLocaleString("es-CL", {
                          style: "currency",
                          currency: "CLP",
                        })
                      : "$0"}
                  </StyledTableCell>
                ))}
              </StyledTableRow>
            ))}
            <StyledTableRow>
              <StyledTableCell style={{ fontWeight: 'bold' }}>TOTAL</StyledTableCell>
              {meses.slice(0, -1).map((mes) => (
                <StyledTableCell key={`total-personas-${mes}`} align="right" style={{ fontWeight: 'bold' }}>
                  {reportesPersonas.reduce((sum, reporte) => sum + (reporte[mes] || 0), 0).toLocaleString("es-CL", {
                    style: "currency",
                    currency: "CLP",
                  })}
                </StyledTableCell>
              ))}
              <StyledTableCell align="right" style={{ fontWeight: 'bold' }}>
                {reportesPersonas.reduce((sum, reporte) => sum + (reporte.TOTAL || 0), 0).toLocaleString("es-CL", {
                  style: "currency",
                  currency: "CLP",
                })}
              </StyledTableCell>
            </StyledTableRow>
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default Reportes;