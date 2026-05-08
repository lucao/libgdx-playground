package br.com.lucasmteixeira.playground.network;

import br.com.lucasmteixeira.playground.model.NetMessage;

public interface NetworkListener {
    void onMessage(NetMessage message);
    void onDisconnected();
}
