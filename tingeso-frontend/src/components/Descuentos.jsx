import * as React from 'react';
import PropTypes from 'prop-types';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';

function TabPanel(props) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`vertical-tabpanel-${index}`}
            aria-labelledby={`vertical-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box sx={{ p: 3 }}>
                    {children}
                </Box>
            )}
        </div>
    );
}

TabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.number.isRequired,
    value: PropTypes.number.isRequired,
};

function a11yProps(index) {
    return {
        id: `vertical-tab-${index}`,
        'aria-controls': `vertical-tabpanel-${index}`,
    };
}

export default function VerticalTabs() {
    const navigate = useNavigate();
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    const descuentosNumPersona = [
        { personas: "1-2 personas", descuento: "0%" },
        { personas: "3-5 personas", descuento: "10%" },
        { personas: "6-10 personas", descuento: "20%" },
        { personas: "11-15 personas", descuento: "30%" }
    ];

    const descuentosCliFrecuente = [
        { cliente_frecuente: "Muy frecuente", num_visitas: "7 a mas veces", descuento: "30%" },
        { cliente_frecuente: "Frecuente", num_visitas: "5 a 6 veces", descuento: "20%" },
        { cliente_frecuente: "Regular", num_visitas: "2 a 4 veces", descuento: "10%" },
        { cliente_frecuente: "No frecuente", num_visitas: "0 a 1 vez", descuento: "0%" }
    ];

    const tarifaEspecial = [
        { tipo: "Cumpleaños", descuento: "50%", aplica: "En un grupo de 3 a 5 personas se aplica a una persona que cumple años" },
        { tipo: "Cumpleaños", descuento: "50%", aplica: "En un grupo de 6 a 10 personas se aplica hasta 2 personas que cumplen años" },
        { tipo: "Fin de semana", descuento: "10%", aplica: "Solo sabados y domingo" },
        { tipo: "Festivo", descuento: "15%", aplica: "Solo dias festivos" },
    ];

    return (
        <Box sx={{ flexGrow: 1, bgcolor: 'background.paper', display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: 400 }}>
            <Box sx={{ display: 'flex', width: '100%' }}>
                <Tabs
                    orientation="vertical"
                    variant="scrollable"
                    value={value}
                    onChange={handleChange}
                    aria-label="Vertical tabs example"
                    sx={{ borderRight: 1, borderColor: 'divider', minWidth: 250 }}
                >
                    <Tab label="Descuento por numero de personas" {...a11yProps(0)} />
                    <Tab label="Descuento para clientes frecuentes" {...a11yProps(1)} />
                    <Tab label="Tarifas para dias especiales" {...a11yProps(2)} />
                </Tabs>

                <Box sx={{ flex: 1 }}>
                    <TabPanel value={value} index={0}>
                        <TableContainer component={Paper} sx={{ maxWidth: 500 }}>
                            <Typography variant="h6" align="center" sx={{ mt: -1 }}>
                                Descuentos por numero de personas
                            </Typography>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell><strong>Numero de personas</strong></TableCell>
                                        <TableCell><strong>Descuento aplicado</strong></TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {descuentosNumPersona.map((fila, index) => (
                                        <TableRow key={index}>
                                            <TableCell>{fila.personas}</TableCell>
                                            <TableCell>{fila.descuento}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </TabPanel>

                    <TabPanel value={value} index={1}>
                        <TableContainer component={Paper} sx={{ maxWidth: 500 }}>
                            <Typography variant="h6" align="center" sx={{ mt: -1 }}>
                                Descuentos por cliente frecuente
                            </Typography>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell><strong>Cliente frecuente</strong></TableCell>
                                        <TableCell><strong>Numero de visitas al mes</strong></TableCell>
                                        <TableCell><strong>Descuento aplicado</strong></TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {descuentosCliFrecuente.map((fila, index) => (
                                        <TableRow key={index}>
                                            <TableCell>{fila.cliente_frecuente}</TableCell>
                                            <TableCell>{fila.num_visitas}</TableCell>
                                            <TableCell>{fila.descuento}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </TabPanel>

                    <TabPanel value={value} index={2}>
                        <TableContainer component={Paper} sx={{ maxWidth: 500 }}>
                            <Typography variant="h6" align="center" sx={{ mt: -1 }}>
                                Tarifas para dias especiales
                            </Typography>
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell><strong>Dia especial</strong></TableCell>
                                        <TableCell><strong>Descuento aplicado</strong></TableCell>
                                        <TableCell><strong>Aplica si...</strong></TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {tarifaEspecial.map((fila, index) => (
                                        <TableRow key={index}>
                                            <TableCell>{fila.tipo}</TableCell>
                                            <TableCell>{fila.descuento}</TableCell>
                                            <TableCell>{fila.aplica}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </TabPanel>
                </Box>
            </Box>
            <Box sx={{ mt: -7, textAlign: 'center', width: '100%' }}>
                <Button variant="contained" color="primary" onClick={() => navigate('/home')}>
                    Home
                </Button>
            </Box>
        </Box>
    );
}


