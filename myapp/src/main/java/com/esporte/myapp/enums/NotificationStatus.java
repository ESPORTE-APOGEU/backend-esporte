package com.esporte.myapp.enums;


public enum NotificationStatus {
    NEW,       // criada e não lida
    READ,      // lida pelo usuário
    RESOLVED,  // “assunto resolvido” (ex.: pedido aceito/recusado)
    ARCHIVED   // arquivada manualmente (opcional)
}