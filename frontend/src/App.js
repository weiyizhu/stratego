import React, { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [isConnected, setIsConnected] = useState(false);
  const [socket, setSocket] = useState();
  const [message, setMessage] = useState("");
  const [error, setError] = useState();
  const [inputValue, setInputValue] = useState("");

  const ipAddress = "192.168.7.99"; // change to your localhost address
  const port = 8080;
  const url = `ws://${ipAddress}:${port}/move`;

  useEffect(() => {
    if (socket) {
      socket.addEventListener("open", onConnect);
      socket.addEventListener("close", onDisconnect);
      socket.addEventListener("error", onError);
      socket.addEventListener("message", onMessage);
    }
  }, [socket]);

  const handleSubmit = (event) => {
    event.preventDefault();
    if (socket.readyState === 1) socket.send(inputValue);
    else setError("Error sending message");
  };

  const onConnect = () => setIsConnected(true);
  const onDisconnect = () => setIsConnected(false);
  const onError = (event) => setError("Error connecting to server");
  const onMessage = (event) => setMessage(event.data);

  const handleInputChange = (event) => {
    setInputValue(event.target.value);
  };

  return (
    <div className="App">
      {isConnected ? (
        <button
          onClick={() => {
            socket.close();
            setIsConnected(false);
          }}
        >
          Disconnect from server
        </button>
      ) : (
        <button
          onClick={() => {
            setSocket(new WebSocket(url));
            setIsConnected(true);
          }}
        >
          Connect to server
        </button>
      )}
      <br />
      <br />
      <form onSubmit={handleSubmit}>
        <input type="text" value={inputValue} onChange={handleInputChange} />
        <button type="submit">Send Message</button>
      </form>
      <p>Server response: {message}</p>
      {error && <p>Error: {error}</p>}
    </div>
  );
}

export default App;
