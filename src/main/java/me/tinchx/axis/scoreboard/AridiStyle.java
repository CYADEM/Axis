package me.tinchx.axis.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AridiStyle {

    MODERN(false, 1), KOHI(true, 15);

    private boolean descending;
    private int firstNumber;
}