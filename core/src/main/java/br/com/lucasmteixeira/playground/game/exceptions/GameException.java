package br.com.lucasmteixeira.playground.game.exceptions;

public class GameException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3196480674773310620L;

	public GameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameException(String message) {
		super(message);
	}

	public GameException(Throwable cause) {
		super(cause);
	}
}
