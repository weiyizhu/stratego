import React, { useContext, useEffect, useState } from "react";
import { AppContext } from "../App";
import { initBoard, url } from "../constants";
import Loading from "../components/Loading";

const GameState = {
  ONCONNECTION: "OnConnection",
  PREP: "Prep",
  GAME: "Game",
  END: "End",
};

const MsgType = {
  INFO: "Info",
  SWITCH: "Switch",
  ERROR: "Error",
};

const Side = {
  RED: 1,
  BLUE: -1,
  NEUTRAL: 0,
};

const valToSide = new Map();
valToSide.set(-1, "Blue");
valToSide.set(0, "Neutral");
valToSide.set(1, "Red");

function Game() {
  const [appInfo, setAppInfo] = useContext(AppContext);
  const gameId = appInfo.gameId;
  const [side, setSide] = useState(Side.NEUTRAL);
  const [gameState, setGameState] = useState(GameState.ONCONNECTION);
  const [board, setBoard] = useState();
  const [errorMsg, setErrorMsg] = useState();
  const [socket, setSocket] = useState();

  const onError = (event) => {
    setErrorMsg(event.data);
  };
  const onMessage = (event) => {
    const { state, type, msg } = JSON.parse(event.data);
    switch (state) {
      case GameState.ONCONNECTION:
        if (type === MsgType.SWITCH) {
          const side = parseInt(msg);
          setSide(side);
          console.log("side: ", side);
          setBoard(initBoard(side));
          setGameState(GameState.PREP);
        }
        break;
      case GameState.PREP:
        if (type === MsgType.SWITCH) {
          setBoard(msg);
          setGameState(GameState.GAME);
        } else if (type === MsgType.ERROR) {
          setErrorMsg(msg);
        }
        break;
      case GameState.GAME:
        if (type === MsgType.SWITCH) {
          setGameState(GameState.END);
        } else if (type === MsgType.ERROR) {
          setErrorMsg(msg);
        } else {
          setBoard(msg);
        }
        break;
      default:
        setErrorMsg("Unknown server response");
    }
  };

  useEffect(() => {
    const socket = new WebSocket(`${url}/move?${gameId}`);

    socket.addEventListener("message", onMessage);
    socket.addEventListener("error", onError);
    setSocket(socket);

    return () => {
      if (socket && socket.readyState === WebSocket.OPEN) {
        socket.removeEventListener("message", onMessage);
        socket.removeEventListener("error", onError);
        socket.close();
        setSocket();
      }
    };
  }, [gameId]);

  return (
    <div className="h-screen flex items-center justify-center">
      {gameState === GameState.ONCONNECTION ? (
        <Loading loadingMsg={"Loading..."} />
      ) : (
        <>{gameState === GameState.PREP ? <p>prep</p> : <p>not prep</p>}</>
      )}
      {errorMsg && (
        <p className="text-lg text-rose-600	 p-8">Error: {errorMsg}</p>
      )}
    </div>
  );
}

export default Game;
