import './App.css'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Navbar from "./components/Navbar"
import Home from './components/Home';
import VerPlanes from './components/VerPlanes';
import ReservationForm from './components/ReservationForm';
import Descuentos from './components/Descuentos';
import RackSemanal from './components/RackSemanal';
import ReservationList from './components/ReservationList';
import Reportes from './components/Reportes';
import NotFound from './components/NotFound';

function App() {
  return (
    <Router>
      <div className="container">
        <Navbar></Navbar>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/home" element={<Home />} />
          <Route path="/planes" element={<VerPlanes />} />
          <Route path="/reservas" element={<ReservationForm />} />
          <Route path="/descuentos" element={<Descuentos />} />
          <Route path="/rackSemanal" element={<RackSemanal />} />
          <Route path="/reservas/list" element={<ReservationList />} />
          <Route path="/reportes" element={<Reportes />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App
