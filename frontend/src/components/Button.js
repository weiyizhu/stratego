import React from "react";

function Button({ msg, onClickFn }) {
  return (
    <button
      className="px-4 py-2 font-semibold text-sm bg-sky-500 text-white rounded-full shadow-sm"
      onClick={onClickFn}
    >
      {msg}
    </button>
  );
}

export default Button;
