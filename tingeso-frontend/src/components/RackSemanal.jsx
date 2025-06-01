import React, { useState, useEffect } from "react";
import axios from "axios";
import "./RackSemanal.css";
import rackSemanalService from "../services/racksemanal.service";

const RackSemanal = () => {
  const getStartOfWeek = (date) => {
    const day = date.getDay();
    const diff = date.getDate() - day;
    return new Date(date.setDate(diff));
  };

  const [weeklyAvailability, setWeeklyAvailability] = useState({});
  const [currentWeekStart, setCurrentWeekStart] = useState(getStartOfWeek(new Date()));
  const [currentDate, setCurrentDate] = useState(new Date());
  const hoursOfDay = [10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22];
  const daysOfWeekShort = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];
  const months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
  const [isCreatingReservation] = useState(false);
  const [showCalendar, setShowCalendar] = useState(false);

  useEffect(() => {
    fetchWeeklyAvailability(currentWeekStart);
  }, [currentWeekStart]);

  const formatDateForBackend = (date) => {
    try {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    } catch (error) {
      console.error("Error formatting date:", date, error);
      return "";
    }
  };

  const getDatesOfWeek = (startDate) => {
    const dates = [];
    for (let i = 0; i < 7; i++) {
      const date = new Date(startDate);
      date.setDate(startDate.getDate() + i);
      dates.push(formatDateForBackend(date));
    }
    return dates;
  };

  const fetchWeeklyAvailability = async (startDateOfWeek) => {
    const weekDates = getDatesOfWeek(startDateOfWeek);
    const startDate = weekDates[0];
    const endDate = weekDates[6];
    try {
      const response = await rackSemanalService.getWeeklyAvailability(startDate, endDate);
      setWeeklyAvailability(response.data);

    } catch (error) {
      console.error("Error fetching weekly availability:", error);
    }
  };


  const getReservationDetails = (day, hour) => {
    const dateKey = getDatesOfWeek(currentWeekStart)[daysOfWeekShort.indexOf(day)];
    const hourKey = `${hour.toString().padStart(2, '0')}:00`;
    return weeklyAvailability[dateKey]?.[hourKey];
  };

  const isTimeSlotReserved = (day, hour) => {
    const details = getReservationDetails(day, hour);
    return details?.estado === "ocupado";
  };

  const getOcupadoText = (day, hour) => {
    const details = getReservationDetails(day, hour);
    if (details?.estado === "ocupado") {
      if (details?.reservadoPor) {
        return `Ocupado por: ${details.reservadoPor}`;
      } else {
        return "Ocupado";
      }
    }
    return null;
  };

  const nextWeek = () => {
    const next = new Date(currentWeekStart);
    next.setDate(currentWeekStart.getDate() + 7);
    setCurrentWeekStart(next);
    setShowCalendar(false);
  };

  const prevWeek = () => {
    const prev = new Date(currentWeekStart);
    prev.setDate(currentWeekStart.getDate() - 7);
    setCurrentWeekStart(prev);
    setShowCalendar(false);
  };

  const today = () => {
    setCurrentWeekStart(getStartOfWeek(new Date()));
    setCurrentDate(new Date());
    setShowCalendar(false);
  };

  const openCalendar = () => {
    setShowCalendar(!showCalendar);
  };

  const goToPrevMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
  };

  const goToNextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
  };

  const selectDate = (day) => {
    const newDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), day);
    setCurrentWeekStart(getStartOfWeek(newDate));
    setCurrentDate(newDate);
    setShowCalendar(false);
  };

  const renderCalendar = () => {
    if (!showCalendar) return null;

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDayOfMonth = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const days = [];

    for (let i = 0; i < firstDayOfMonth; i++) {
      days.push(<div key={`empty-${i}`} className="calendar-day empty"></div>);
    }

    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(year, month, day);
      const isSelected = formatDateForBackend(date) === formatDateForBackend(currentWeekStart);
      days.push(
        <div
          key={day}
          className={`calendar-day ${isSelected ? 'selected' : ''}`}
          onClick={() => selectDate(day)}
        >
          {day}
        </div>
      );
    }

    return (
      <div className="calendar-container">
        <div className="calendar-header">
          <button onClick={goToPrevMonth}>&lt;</button>
          <span>{months[month]} {year}</span>
          <button onClick={goToNextMonth}>&gt;</button>
        </div>
        <div className="calendar-days-header">
          {daysOfWeekShort.map((day) => (
            <div key={day}>{day}</div>
          ))}
        </div>
        <div className="calendar-grid">{days}</div>
      </div>
    );
  };

  return (
    <div className="weekly-calendar-container">
      <h2>RACK DE RESERVAS</h2>
      <div className="calendar-header">
        <button onClick={today}>Hoy</button>
        <button onClick={prevWeek}>&lt;</button>
        <span>{`Semana de ${formatDateForBackend(getDatesOfWeek(currentWeekStart)[0])}`}</span>
        <button onClick={nextWeek}>&gt;</button>
        <button onClick={openCalendar}>Calendario</button>
      </div>

      {renderCalendar()}

      <div className="calendar-grid" style={{ gridTemplateColumns: `repeat(8, 1fr)` }}>
        {daysOfWeekShort.map((day, index) => {
          const currentDateForDay = new Date(currentWeekStart);
          currentDateForDay.setDate(currentWeekStart.getDate() + index);
          const formattedDate = formatDateForBackend(currentDateForDay);

          return (
            <div key={day} className="day-column">
              <div className="day-header">{day} ({formattedDate.slice(-2)})</div>
              {hoursOfDay.map((hour) => {
                const isReserved = isTimeSlotReserved(day, hour);
                const ocupadoText = isReserved ? getOcupadoText(day, hour) : null;

                return (
                  <div
                    key={`${day}-${hour}`}
                    className={`hour-slot ${isReserved ? 'reserved' : 'available'}`}
                    style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}
                  >
                    {!isReserved && !isCreatingReservation && <div className="available-label">{`${hour}:00 Disponible`}</div>}
                    {isReserved && <div className="reserved-label">{`${hour}:00 ${ocupadoText}`}</div>}
                  </div>
                );
              })}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default RackSemanal;