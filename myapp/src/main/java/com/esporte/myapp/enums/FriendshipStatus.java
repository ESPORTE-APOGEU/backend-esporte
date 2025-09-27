package com.esporte.myapp.enums;

public enum FriendshipStatus {
    /**
     * Status: 0
     * Representa uma amizade que foi desfeita (exclusão lógica).
     * Esses registros não devem aparecer em listas de amigos.
     */
    INACTIVE,

    /**
     * Status: 1
     * Representa uma amizade ativa e válida.
     * Apenas amizades com este status devem ser consideradas nas lógicas do app.
     */
    ACTIVE;
}