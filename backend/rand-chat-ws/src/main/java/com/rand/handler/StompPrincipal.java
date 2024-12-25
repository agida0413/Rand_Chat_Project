package com.rand.handler;

import java.security.Principal;
//Stomp 통신에 사용되는 커스텀 Principal
class StompPrincipal implements Principal {
    String name;

    StompPrincipal(String name) { this.name = name; }

    @Override
    public String getName() { return name;}
}
