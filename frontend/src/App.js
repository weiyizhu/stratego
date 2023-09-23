import React, { useState, createContext } from "react";
import FindGame from "./screens/FindGame";
import Game from "./screens/Game";

export const AppState = {
  Home: 0,
  Game: 1,
};
export const AppContext = createContext();

function App() {
  const [appInfo, setAppInfo] = useState({
    state: AppState.Home,
    gameId: null,
  });

  return (
    <div>
      <AppContext.Provider value={[appInfo, setAppInfo]}>
        {appInfo.state === AppState.Home ? <FindGame /> : <Game />}
      </AppContext.Provider>
    </div>
  );
}

export default App;
