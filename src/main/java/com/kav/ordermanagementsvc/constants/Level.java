package com.kav.ordermanagementsvc.constants;

import static com.kav.ordermanagementsvc.constants.AppConstants.*;

public enum Level {
    REGULAR(REGULAR_DISCOUNT, 0),
    GOLD(GOLD_DISCOUNT, GOLD_THRESHOLD),
    PLATINUM(PLATINUM_DISCOUNT, PLATINUM_THRESHOLD)
    ;
    public final double discount;
    public  final int orderThreshold;
    Level(double discount, int orderThreshold){
        this.discount =discount;
        this.orderThreshold = orderThreshold;
    }
}
