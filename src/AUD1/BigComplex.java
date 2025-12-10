package AUD1;

import java.math.BigDecimal;

public class BigComplex {
    private BigDecimal real;
    private BigDecimal imag;

    public BigComplex() {
    }

    public BigComplex(BigDecimal real, BigDecimal imag) {
        this.real = real;
        this.imag = imag;
    }

    public static BigComplex add(BigComplex b, BigComplex a) {
        return new BigComplex(a.real.add(b.real), a.imag.add(b.imag));
    }

    @Override
    public String toString() {
        return "BigComplex{" +
                "real=" + real +
                ", imag=" + imag +
                '}';
    }
}
