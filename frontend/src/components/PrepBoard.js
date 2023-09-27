import React from "react";
import Piece from "./Piece";

function PrepBoard({ side, board }) {
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

  const handleOnDragEnter = (event) => {
    event.preventDefault();
    const tdElement = getTdElement(event.target);
    tdElement.style.backgroundColor = "lightblue";
    if (dragEnterElement && tdElement !== dragEnterElement) {
      dragEnterElement.style.backgroundColor = "";
    }
    dragEnterElement = tdElement;
  };

  const handleOnDrop = (event) => {
    event.preventDefault();
    const targetElement = getTdElement(event.target);

    if (sourceElement && sourceElement !== targetElement) {
      // Swap td elements onDrop by changing their innerHTML and "piece-info" attribute
      const sourceElementPieceInfo = sourceElement.getAttribute("piece-info");
      const targetElementPieceInfo = targetElement.getAttribute("piece-info");
      const temp = sourceElement.innerHTML;
      sourceElement.innerHTML = targetElement.innerHTML;
      sourceElement.setAttribute("piece-info", targetElementPieceInfo);
      targetElement.innerHTML = temp;
      targetElement.setAttribute("piece-info", sourceElementPieceInfo);
      sourceElement = null; // Reset the source element
    }
  };
  const handleOnMouseOver = (event) => {
    const tdElement = getTdElement(event.target);
    tdElement.style.backgroundColor = "lightblue";
    tdElement.style.cursor = "grab";
    if (mouseOverElement && tdElement !== mouseOverElement) {
      mouseOverElement.style.backgroundColor = "";
    }
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
                const movable = pieceSide === side;
                if (!movable) {
                  return (
                    <td
                      key={col_index}
                      id={`${row_index},${col_index}`}
                      piece-info={`${pieceSide},${rank}`}
                      className={`p-4 border ${rank === 12 && "bg-green-100"}`}
                    >
                      <Piece side={pieceSide} rank={rank} />
                    </td>
                  );
                }
                return (
                  <td
                    key={col_index}
                    id={`${row_index},${col_index}`}
                    className="p-4 border"
                    // We will use the "piece-info" attribute to get what piece this td stores
                    // The backend will check for the integrity of the board in case malicious players try to change this attribute
                    piece-info={`${pieceSide},${rank}`}
                    draggable={true}
                    onDragStart={handleOnDragStart}
                    onDragOver={(event) => {
                      event.preventDefault();
                    }}
                    onDragEnter={handleOnDragEnter}
                    onDrop={handleOnDrop}
                    onMouseOver={handleOnMouseOver}
                  >
                    <Piece side={pieceSide} rank={rank} />
                  </td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default PrepBoard;
