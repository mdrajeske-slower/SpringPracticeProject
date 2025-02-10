package com.slower.springpractice.SpringPracticeProject.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GearSlot {
    private final String name;
    private final int level;
}
