package com.posada.santiago.alphapostsandcomments.domain.values;

import co.com.sofka.domain.generic.ValueObject;

public class Favorite implements ValueObject<Boolean> {

    private final Boolean favorite;

    public Favorite(Boolean bool) {
        this.favorite = bool;
    }

    @Override
    public Boolean value() {
        return favorite;
    }
}
