package com.akozlowski.domain;

import java.sql.SQLException;

public class DaoException extends RuntimeException {

    public DaoException(final SQLException sqlException) {
        super(sqlException);
    }
}
