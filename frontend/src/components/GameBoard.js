import React from "react";
import Piece from "./Piece";

function GameBoard({ side, board, socket }) {
  /*
    Board will be serialized as a 2-D array
    [
      [[side, rank], [side, rank]], ...
      [[side, rank], [side, rank]], ...
      ...
    ]

    side := {1, -1, 0} 
      - -1 => blue
      - 0 => neutral
      - 1 => red
    rank := {0-14}
      - corresponds to `Unit.java` in the backend
  */
  let sourceElement = null;
  let mouseOverElement = null;
  let dragEnterElement = null;

  const handleOnMouseOut = () => {
    if (mouseOverElement) {
      mouseOverElement.style.backgroundColor = "";
    }
    mouseOverElement = null;
  };

  const handleOnDragEnd = () => {
    if (dragEnterElement) {
      dragEnterElement.style.backgroundColor = "";
    }
    dragEnterElement = null;
  };

  const handleOnDragStart = (event) => {
    event.dataTransfer.effectAllowed = "move";
    const tdElement = getTdElement(event.target);
    tdElement.style.backgroundColor = "";
    sourceElement = tdElement;
  };

  const handleOnDragEnterDroppableElement = (event) => {
    event.preventDefault();
    const tdElement = getTdElement(event.target);
    tdElement.style.backgroundColor = "lightblue";
    if (dragEnterElement && tdElement !== dragEnterElement) {
      dragEnterElement.style.backgroundColor = "";
    }
    dragEnterElement = tdElement;
  };

  const handleOnDragEnterNonDroppableElement = (event) => {
    event.preventDefault();
    const tdElement = getTdElement(event.target);
    tdElement.style.backgroundColor = "";
    if (dragEnterElement) {
      dragEnterElement.style.backgroundColor = "";
    }
    dragEnterElement = null;
  };

  const handleOnDrop = (event) => {
    event.preventDefault();
    const targetElement = getTdElement(event.target);

    if (sourceElement && sourceElement !== targetElement) {
      // Swap td elements onDrop by changing their innerHTML and "piece-info" attribute
      const [source_i, source_j] = sourceElement.getAttribute("id").split(",");
      const [target_i, target_j] = targetElement.getAttribute("id").split(",");
      const currPos = [parseInt(source_i), parseInt(source_j)];
      const nextPos = [parseInt(target_i), parseInt(target_j)];
      sourceElement = null; // Reset the source element

      socket.send(JSON.stringify([currPos, nextPos]));
    }
  };
  const handleOnMouseOverDraggableElement = (event) => {
    const tdElement = getTdElement(event.target);
    tdElement.style.backgroundColor = "lightblue";
    tdElement.style.cursor = "grab";
    if (mouseOverElement && tdElement !== mouseOverElement) {
      mouseOverElement.style.backgroundColor = "";
    }
    mouseOverElement = tdElement;
  };

  const handleOnMouseOverNonDraggableElement = (event) => {
    const tdElement = getTdElement(event.target);
    tdElement.style.backgroundColor = "";
    tdElement.style.cursor = "";
    mouseOverElement = tdElement;
  };

  const getTdElement = (element) => {
    // Get the top level <td> element when onMouseOver or onDragEnter
    // The hierarchy is as such: <td> -> <div> -> <svg> -> <path>
    let tdElement = element;
    const tagName = element.tagName;
    if (tagName === "DIV") {
      tdElement = tdElement.parentNode;
    } else if (tagName === "svg") {
      tdElement = tdElement.parentNode.parentNode;
    } else if (tagName === "path") {
      tdElement = tdElement.parentNode.parentNode.parentNode;
    }
    return tdElement;
  };

  return (
    <div className="h-screen flex items-center justify-center">
      <table
        className="bg-slate-100"
        onMouseOut={handleOnMouseOut}
        onDragEnd={handleOnDragEnd}
      >
        <tbody>
          {board.map((row, row_index) => (
            <tr key={row_index}>
              {row.map((piece, col_index) => {
                const [pieceSide, rank] = piece;
                if (rank === 12) {
                  // not draggable and droppable
                  return (
                    <td
                      key={col_index}
                      id={`${row_index},${col_index}`}
                      piece-info={`${pieceSide},${rank}`}
                      className={`p-4 border bg-green-100`}
                    >
                      <Piece side={pieceSide} rank={rank} />
                    </td>
                  );
                } else {
                  if (pieceSide !== side) {
                    // not draggable but droppable
                    return (
                      <td
                        key={col_index}
                        id={`${row_index},${col_index}`}
                        piece-info={`${pieceSide},${rank}`}
                        className={`p-4 border`}
                        onDrop={handleOnDrop}
                        onDragOver={(event) => {
                          event.preventDefault();
                        }}
                        onDragEnter={handleOnDragEnterDroppableElement}
                        onMouseOver={handleOnMouseOverNonDraggableElement}
                      >
                        <Piece side={pieceSide} rank={rank} />
                      </td>
                    );
                  } else {
                    // draggable but not droppable
                    return (
                      <td
                        key={col_index}
                        id={`${row_index},${col_index}`}
                        className="p-4 border"
                        piece-info={`${pieceSide},${rank}`}
                        draggable={true}
                        onDragStart={handleOnDragStart}
                        onDragOver={(event) => {
                          event.preventDefault();
                        }}
                        onDragEnter={handleOnDragEnterNonDroppableElement}
                        onMouseOver={handleOnMouseOverDraggableElement}
                      >
                        <Piece side={pieceSide} rank={rank} />
                      </td>
                    );
                  }
                }
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default GameBoard;
