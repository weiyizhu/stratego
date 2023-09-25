import React, { useEffect, useState } from "react";

import {
  PiNumberOneBold,
  PiNumberTwoBold,
  PiNumberThreeBold,
  PiNumberFourBold,
  PiNumberFiveBold,
  PiNumberSixBold,
  PiNumberSevenBold,
  PiNumberEightBold,
  PiNumberNineBold,
} from "react-icons/pi";

import { GiSpy } from "react-icons/gi";
import { BiSolidBomb, BiSolidFlag } from "react-icons/bi";
import { TbChessFilled } from "react-icons/tb";

const rankToIcon = {
  0: <BiSolidFlag />,
  1: <GiSpy />,
  2: <PiNumberOneBold />,
  3: <PiNumberTwoBold />,
  4: <PiNumberThreeBold />,
  5: <PiNumberFourBold />,
  6: <PiNumberFiveBold />,
  7: <PiNumberSixBold />,
  8: <PiNumberSevenBold />,
  9: <PiNumberEightBold />,
  10: <PiNumberNineBold />,
  11: <BiSolidBomb />,
  12: <TbChessFilled visibility={"hidden"} />, // River
  13: <TbChessFilled visibility={"hidden"} />, // Empty
  14: <TbChessFilled />, // Unknown enemy piece
};

const sideToCSSColor = {
  1: "red",
  0: "initial",
  "-1": "blue",
};

function Piece({ side, rank }) {
  const color = sideToCSSColor[side];

  return <div style={{ color }}>{rankToIcon[rank]}</div>;
}

export default Piece;
