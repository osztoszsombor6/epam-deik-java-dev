package com.epam.training.webshop.finance.bank;

import java.util.Currency;
import java.util.Optional;

public interface Bank {

    Optional<Double> getExchangeRate(Currency from, Currency to);

}