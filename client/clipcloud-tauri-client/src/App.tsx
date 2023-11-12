import logo from "./assets/Logo.svg"
import "./App.css";
import Element from "./Element";

function App() {
  return (
    <div className="main-container">
      <div className="top-bar">
        <img src={logo} className="logo"/>
      </div>
      <div className="content">
        <h1>YOUR <br/><br/> ELEMENTS</h1>
        <br/>
        <Element content="testes"/>
      </div>
    </div>
  );
}

export default App;
