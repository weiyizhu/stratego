const ipAddress = "127.0.0.1";
const port = 8080;
export const url = `ws://${ipAddress}:${port}`;

const pieces = [
  0,
  1,
  ...Array(8).fill(2),
  ...Array(5).fill(3),
  ...Array(4).fill(4),
  ...Array(4).fill(5),
  ...Array(4).fill(6),
  ...Array(3).fill(7),
  ...Array(2).fill(8),
  9,
  10,
  ...Array(6).fill(11),
];
export const initBoard = (side) => {
  const oppSide = side * -1;
  const board = new Array(10);

  for (let i = 0; i < 10; i++) {
    board[i] = new Array(10).fill([0, 13]); // [(side, rank)], 13: empty
  }

  board[4][2] = [0, 12];
  board[4][3] = [0, 12];
  board[5][2] = [0, 12];
  board[5][3] = [0, 12];
  board[4][6] = [0, 12];
  board[4][7] = [0, 12];
  board[5][6] = [0, 12];
  board[5][7] = [0, 12];

  let k = 0;
  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 10; j++) {
      board[i][j] = [oppSide, 14]; // 14: unknown enemy piece
      k++;
    }
  }

  k = 0;
  for (let i = 6; i <= 9; i++) {
    for (let j = 0; j <= 9; j++) {
      board[i][j] = [side, pieces[k]];
      k++;
    }
  }
  return board;
};

export const getArrangedBoard = () => {
  const board = new Array(4);

  for (let i = 0; i < 4; i++) {
    board[i] = new Array(10);
  }

  for (let i = 0; i < 4; i++) {
    for (let j = 0; j < 10; j++) {
      const element = document.getElementById(`${i + 6},${j}`);
      const [_, rank] = element.getAttribute("piece-info").split(",");
      board[i][j] = parseInt(rank);
    }
  }

  return board;
};
