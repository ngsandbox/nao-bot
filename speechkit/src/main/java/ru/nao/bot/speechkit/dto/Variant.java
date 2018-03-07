package ru.nao.bot.speechkit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Variant {
    private double confidence;
    private String value;
}
