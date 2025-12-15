package org.fdu;

import org.fdu.GuessResult;
import org.fdu.GameStatus;

public record GuessResponse(
        GuessResult guessResult,
        GameStatus gameStatus
) {}
