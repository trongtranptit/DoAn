package com.trong.clas.model;

import java.io.Serializable;
import java.math.BigInteger;

public class MyRawTx implements Serializable {
    private BigInteger nonce;
    private String from;
    private String to;
    private BigInteger gasLimit;
    private BigInteger gasPrice;
    private BigInteger value;

    public MyRawTx() {
    }

    public MyRawTx(BigInteger nonce, String from, String to, BigInteger gasLimit, BigInteger gasPrice, BigInteger value) {
        this.nonce = nonce;
        this.from = from;
        this.to = to;
        this.gasLimit = gasLimit;
        this.gasPrice = gasPrice;
        this.value = value;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }
}
