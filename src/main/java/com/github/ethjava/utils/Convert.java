package com.github.ethjava.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Convert {

    /**
     * the unit of Ether convert to the unit of wei.
     * @param decimal
     * @return
     */
    public synchronized static String ConvertEtherToWei(BigDecimal decimal){
        if (Objects.isNull(decimal)){
            return null;
        }
        BigDecimal decimalWei = decimal.multiply(new BigDecimal(10).pow(18));
        return  decimalWei.toPlainString();
    }

    /**
     * the unit of Wei convert to the unit of Ether
     * @param decimal
     * @param precision the precision default is 18
     * @return
     */
    public synchronized static String ConvertWeiToEther(BigDecimal decimal,Integer precision){
        if (Objects.isNull(decimal)){
            return null;
        }
        if (precision==null){
            precision=18;
        }
        BigDecimal decimalEther = decimal.divide(new BigDecimal(10).pow(18));
        decimalEther = decimalEther.setScale(precision, RoundingMode.HALF_UP);
        return  decimalEther.toPlainString();
    }

    public synchronized static String ConvertWeiToEther(BigDecimal decimal){
        return ConvertWeiToEther(decimal,18);
    }



}
