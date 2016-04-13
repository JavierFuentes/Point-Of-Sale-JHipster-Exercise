package asv.exercise.domain.util;

import org.hibernate.dialect.H2Dialect;

import java.sql.Types;

public class FixedH2Dialect extends H2Dialect {

    public FixedH2Dialect() {
        super();
        registerColumnType(Types.FLOAT, "double");  // change "real" for "double" for error in All my JHipster native Test!
    }
}
