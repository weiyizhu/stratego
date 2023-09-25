import React, { useContext, useEffect, useState } from "react";
import { AppContext, AppState } from "../App";
import { initBoard, url, getArrangedBoard } from "../helpers";
import Loading from "../components/Loading";
import GameBoard from "../components/GameBoard";
import Button from "../components/Button";
import PrepBoard from "../components/PrepBoard";

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

const sideToText = new Map();
sideToText.set(-1, "blue");
sideToText.set(0, "neutral");
sideToText.set(1, "red");

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

  const options = () => {
    if (gameState === GameState.PREP) {
      return (
        <>
          <p className="text-xl p-3">You are in a game!</p>
          <p className="text-xl p-3">
            You are {sideToText.get(side)}. Arrange your board
          </p>
          <p className="p-3">
            <Button
              msg={"I'm Ready"}
              onClickFn={() => {
                const board = getArrangedBoard();
                socket.send(JSON.stringify(board));
              }}
            />
          </p>
        </>
      );
    }
  };

  return (
    <div className="h-screen flex items-center justify-evenly">
      {gameState === GameState.ONCONNECTION ? (
        <Loading loadingMsg={"Loading..."} />
      ) : (
        <>
          <>
            {gameState === GameState.PREP ? (
              <PrepBoard side={side} board={board} />
            ) : (
              <GameBoard />
            )}
          </>
          <div className="h-screen flex flex-col items-center justify-evenly">
            <div className="flex flex-col items-center justify-center">
              <p className="text-5xl p-8 mt-8">Stratego</p>
              {options()}
              {errorMsg && (
                <p className="text-xl text-rose-600	 p-8">Error: {errorMsg}</p>
              )}
            </div>
            <div className="basis-1/2"></div>
          </div>
        </>
      )}
    </div>
  );
}

export default Game;
