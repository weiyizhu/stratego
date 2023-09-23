import React, { useContext, useState } from "react";
import { AppContext, AppState } from "../App";
import Loading from "../components/Loading";
import { url } from "../constants";
import Button from "../components/Button";

function FindGame() {
  const [_, setAppInfo] = useContext(AppContext);
  const [loadingMsg, setLoadingMsg] = useState();
  const [errorMsg, setErrorMsg] = useState();

  const findGameHandler = () => {
    const socket = new WebSocket(`${url}/findGame`);
    socket.onmessage = (event) => {
      const msg = event.data;
      if (msg === "Waiting") {
        setLoadingMsg("Finding a game...");
      } else {
        socket.close();
        setLoadingMsg();
        setAppInfo({ state: AppState.Game, gameId: event.data });
      }
    };
    socket.onerror = (event) => {
      setErrorMsg(event.data);
      socket.close();
      setLoadingMsg();
    };
  };

  return (
    <div className="flex justify-center items-center h-screen">
      <div className="flex flex-col justify-around h-screen">
        <div className="text-center">
          <p className="text-4xl p-8">Welcome to Stratego</p>
          {loadingMsg ? (
            <Loading loadingMsg={loadingMsg} />
          ) : (
            <Button msg={"Find a Game"} onClickFn={findGameHandler} />
          )}

          {errorMsg && (
            <p className="text-lg text-rose-600	 p-8">Error: {errorMsg}</p>
          )}
        </div>
        <div></div>
      </div>
    </div>
  );
}

export default FindGame;
