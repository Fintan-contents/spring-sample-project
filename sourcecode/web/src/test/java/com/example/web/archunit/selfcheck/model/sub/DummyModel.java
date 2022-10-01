package com.example.web.archunit.selfcheck.model.sub;

import org.springframework.data.domain.Pageable;

public class DummyModel {

    /** java.langパッケージへの依存は許容する。 */
    private String name;

    /** {@link Pageable}への依存は許容する。 */
    private Pageable pageable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}
