package com.example.tddprac.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RatePointServiceTest {

    @InjectMocks
    private RatePointService ratePointService;

    @Test
    public void _10000원적립은100원() {
        //given
        final int price = 10000;
        //when
        final int result = ratePointService.calculateAmount(price);
        //then
        assertThat(result).isEqualTo(100);
    }
    @Test
    public void _20000원적립은200원() {
        //given
        final int price = 20000;
        //when
        final int result = ratePointService.calculateAmount(price);
        //then
        assertThat(result).isEqualTo(200);
    }
    @Test
    public void _30000원적립은300원() {
        //given
        final int price = 30000;
        //when
        final int result = ratePointService.calculateAmount(price);
        //then
        assertThat(result).isEqualTo(300);
    }
}
