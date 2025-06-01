import React, { useState, useEffect } from "react";
import axios from "axios";
import reservationService from "../services/reservation.service";
import kartService from "../services/kart.service";
import { useNavigate } from "react-router-dom";

const ReservationForm = () => {
    const navigate = useNavigate();

    const [user, setUser] = useState({ name: "", dateOfBirth: "", email: "" });
    const [users, setUsers] = useState([]);
    const [editingIndex, setEditingIndex] = useState(null);
    const [availableKarts, setAvailableKarts] = useState([]);
    const [reservationDate, setReservationDate] = useState("");
    const [reservationTime, setReservationTime] = useState("");
    const [numLaps, setNumLaps] = useState(0);
    const [duration, setDuration] = useState(0);
    const [totalPrice, setTotalPrice] = useState(null);
    const [isCalculatingPrice, setIsCalculatingPrice] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [message, setMessage] = useState("");
    const [timeValidationError, setTimeValidationError] = useState("");

    useEffect(() => {
        const fetchAvailableKarts = () => {
            console.log("Fetching available karts...");
            kartService.getAvailableKarts()
                .then(data => {
                    console.log("Available karts:", data);
                    setAvailableKarts(data.sort((a, b) => a.code.localeCompare(b.code)));
                })
                .catch(error => {
                    console.error("Error al obtener los karts disponibles: ", error);
                });
        };
        fetchAvailableKarts();
    }, []);

    useEffect(() => {
        const shouldCalculate =
            [...users, user].filter(u => u.name && u.dateOfBirth && u.email).length > 0 &&
            reservationDate &&
            reservationTime &&
            (numLaps > 0 || duration > 0);

        console.log("shouldCalculate:", shouldCalculate);
        console.log("users:", users);
        console.log("user:", user);
        console.log("reservationDate:", reservationDate);
        console.log("reservationTime:", reservationTime);
        console.log("numLaps:", numLaps);
        console.log("duration:", duration);


        if (shouldCalculate) {
            calculateTotalPrice();
        } else {
            setTotalPrice(null);
        }
    }, [users, user, reservationDate, reservationTime, numLaps, duration]);

    const calculateTotalPrice = async () => {
        setIsCalculatingPrice(true);
        console.log("Calculando el precio total...");
        const usersToSend = [...users];
        if (user.name && user.dateOfBirth && user.email) {
            usersToSend.push({ ...user });
        }
        const reservationData = {
            usuarios: usersToSend,
            reservationDate: reservationDate,
            reservationTime: reservationTime + ":00",
            numLaps: numLaps,
            duration: duration,
        };
        console.log("reservationData for price calculation:", reservationData);
        reservationService.calculateTotalPrice(reservationData)
            .then(response => {
                console.log("Price calculation response:", response.data);
                setTotalPrice(response.data);
            })
            .catch(error => {
                console.error("Error al calcular el precio: ", error);
                setTotalPrice(null);
            })
            .finally(() => {
                setIsCalculatingPrice(false);
            });
    };


    const handleUserChange = (e) => {
        const newUser = { ...user, [e.target.name]: e.target.value };
        setUser(newUser);
        console.log("handleUserChange - Updated user:", newUser);
    };

    const handleAddOrUpdateUser = () => {
        if (!user.name || !user.dateOfBirth || !user.email) return;

        if (editingIndex !== null) {
            const updated = [...users];
            updated[editingIndex] = user;
            setUsers(updated);
            setEditingIndex(null);
            console.log("handleAddOrUpdateUser - Updated users:", updated);
        } else {
            if (users.length < 15) {
                const newUsers = [...users, user];
                setUsers(newUsers);
                console.log("handleAddOrUpdateUser - Added user, new users:", newUsers);
            } else {
                setMessage("No se pueden agregar mas de 15 personas");
                return;
            }
        }

        setUser({ name: "", dateOfBirth: "", email: "" });
        setMessage("");
    };

    const handleEditUser = (index) => {
        const userToEdit = users[index];
        setUser(userToEdit);
        setEditingIndex(index);
        console.log("handleEditUser - Editing user at index:", index, "user:", userToEdit);
    };

    const handleRemoveUser = (indexToRemove) => {
        const updatedUsers = users.filter((_, index) => index !== indexToRemove);
        setUsers(updatedUsers);
        console.log("handleRemoveUser - Removed user at index:", indexToRemove, "new users:", updatedUsers);
        if (editingIndex === indexToRemove) {
            setEditingIndex(null);
            setUser({ name: "", dateOfBirth: "", email: "" });
        }
    };

    const validateTimeFormat = (time) => {
        const formatoHoraValido = /^([01]?[0-9]|2[0-3]):[0-5][0-9]$/;
        return formatoHoraValido.test(time);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setMessage("");
        setTimeValidationError("");
        console.log("Submitting form...");

        if (!validateTimeFormat(reservationTime)) {
            setTimeValidationError("Por favor, ingrese la hora en formato HH:MM (ej. 10:00, 15:30).");
            setIsSubmitting(false);
            console.log("handleSubmit - Invalid time format");
            return;
        }

        const allUsers = [...users];
        if (user.name && user.dateOfBirth && user.email) {
            allUsers.push({ ...user });
        }

        const reservationData = {
            usuarios: allUsers,
            reservationDate: reservationDate,
            reservationTime: reservationTime + ":00",
            numLaps,
            duration,
        };
        console.log("handleSubmit - Reservation data:", reservationData);

        reservationService.create(reservationData)
            .then(response => {
                console.log("handleSubmit - .then() - Response:", response);
                setMessage("Reserva realizada con éxito. Karts asignados:");
                if (response.data && response.data.userKartAssignments) {
                    let assignmentsMessage = "";
                    response.data.userKartAssignments.forEach(assignment => {
                        assignmentsMessage += `\nUsuario ID: ${assignment.user.id}, Kart Codigo: ${assignment.kart.code}`;
                    });
                    setMessage(prevMessage => prevMessage + assignmentsMessage);
                }
                setUsers([]);
                setUser({ name: "", dateOfBirth: "", email: "" });
                setReservationDate("");
                setReservationTime("");
                setNumLaps(0);
                setDuration(0);
                setTotalPrice(null);
                setTimeout(() => {
                    navigate("/home");
                }, 2000);
            })
            .catch(error => {
                console.error("handleSubmit - .catch() - Error:", error);
                setMessage("Error al realizar la reserva. Intenta nuevamente.");
            })
            .finally(() => {
                setIsSubmitting(false);
            });

    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Formulario de Reserva</h2>

            {message && (
                <div
                    style={{
                        margin: "10px 0",
                        padding: "10px",
                        borderRadius: "5px",
                        backgroundColor: message.includes("éxito") ? "#d4edda" : "#f8d7da",
                        color: message.includes("éxito") ? "#155724" : "#721c24",
                        border: message.includes("éxito") ? "1px solid #c3e6cb" : "1px solid #f5c6cb",
                    }}
                >
                    {message}
                </div>
            )}

            <div>
                <label>Nombre:</label>
                <input type="text" name="name" value={user.name} onChange={handleUserChange} required />
            </div>
            <div>
                <label>Fecha de nacimiento:</label>
                <input type="date" name="dateOfBirth" value={user.dateOfBirth} onChange={handleUserChange} required />
            </div>
            <div>
                <label>Email:</label>
                <input type="email" name="email" value={user.email} onChange={handleUserChange} required />
            </div>
            <button type="button" onClick={handleAddOrUpdateUser}>
                {editingIndex !== null ? "Actualizar Usuario" : "Agregar Usuario"}
            </button>

            {users.length > 0 && (
                <div>
                    <h3>Usuarios Agregados:</h3>
                    <p><strong>Tamaño del grupo:</strong> {[...users, user].filter(u => u.name && u.dateOfBirth && u.email).length} persona{[...users, user].filter(u => u.name && u.dateOfBirth && u.email).length !== 1 && 's'}</p>
                    {users.map((u, index) => (
                        <div key={index}>
                            <p>👤 {u.name} | 🎂 {u.dateOfBirth} | ✉️ {u.email}</p>
                            <button type="button" onClick={() => handleEditUser(index)}>Editar</button>
                            <button type="button" onClick={() => handleRemoveUser(index)}>Eliminar</button>
                        </div>
                    ))}
                </div>
            )}

            <hr />

            <div>
                <h3>Karts Disponibles:</h3>
                <ul>
                    {availableKarts.map(kart => (
                        <li key={kart.id}>Codigo: {kart.code}</li>
                    ))}
                </ul>
            </div>

            <div>
                <label>Fecha de reserva:</label>
                <input
                    type="date"
                    value={reservationDate}
                    onChange={(e) => setReservationDate(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Hora de reserva:</label>
                <input
                    type="time"
                    value={reservationTime}
                    onChange={(e) => setReservationTime(e.target.value)}
                    required
                />
                {timeValidationError && <p style={{ color: "red" }}>{timeValidationError}</p>}
            </div>
            <div>
                <label>Vueltas:</label>
                <input
                    type="number"
                    min="1"
                    value={numLaps}
                    onChange={(e) => setNumLaps(Number(e.target.value))}
                    required
                />
            </div>
            <div>
                <label>Duracion (min):</label>
                <input
                    type="number"
                    min="1"
                    value={duration}
                    onChange={(e) => setDuration(Number(e.target.value))}
                    required
                />
            </div>

            <div>
                <label>Precio total:</label>
                <input
                    type="text"
                    value={
                        isCalculatingPrice
                            ? "Calculando..."
                            : totalPrice !== null
                                ? `$${Number(totalPrice).toFixed(2)}`
                                : [...users, user].filter(u => u.name && u.dateOfBirth && u.email).length > 0
                                    ? "Calculando..."
                                    : "Agrega al menos un usuario"
                    }
                    readOnly
                    disabled
                />
            </div>

            <button type="submit" disabled={isSubmitting || [...users, user].filter(u => u.name && u.dateOfBirth && u.email).length === 0}>
                {isSubmitting ? "Procesando..." : "Realizar Reserva"}
            </button>
        </form>
    );
};

export default ReservationForm;
